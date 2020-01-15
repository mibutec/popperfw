/*
 * Copyright (C) 2013 - 2018 Michael Bulla [michaelbulla@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.popper.fw.jemmy.annotations.locator;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Transforms a {@link Container} into a XML-{@link Document} to allow xpath
 * search for elements
 *
 * @author Michael
 *
 */
public class ContainerToDocumentTransformer {
    private static final String COMPONENT_KEY = "componentKey";

    private final Document doc;

    private Map<String, Collection<Component>> resultCache = new HashMap<>();

    private ContainerToDocumentTransformer(Container container, String expression) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            doc.appendChild(containerToElement(doc, container));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(baos, "UTF-8")));
            return new String(baos.toByteArray(), "UTF-8");
        } catch (TransformerException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public Collection<Component> findComponentMatching(String xpathExpression) {
        Collection<Component> resultFromCache = resultCache.get(xpathExpression);
        if (resultFromCache != null) {
            return resultFromCache;
        }

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        try {
            XPathExpression expr = xpath.compile(xpathExpression);
            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            Set<Component> ret = new HashSet<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                ret.add((Component) nodeList.item(i).getUserData(COMPONENT_KEY));
            }
            resultCache.put(xpathExpression, ret);
            return ret;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private Element containerToElement(Document doc, Container container) {
        Element ret = componentToElement(doc, container);
        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                ret.appendChild(containerToElement(doc, (Container) comp));
            } else {
                ret.appendChild(componentToElement(doc, comp));
            }
        }

        return ret;
    }

    private Element componentToElement(Document doc, Component component) {
        String tagName = component.getClass().getName().substring(component.getClass().getName().lastIndexOf('.') + 1)
                .replaceAll("\\$", "_");
        Element ret = doc.createElement(tagName);
        ret.setUserData(COMPONENT_KEY, component, null);
        Map<String, Object> foundValues = getFields(component);
        foundValues = getMethods(foundValues, component);

        for (Entry<String, Object> entry : foundValues.entrySet()) {
            String valueAsString = entry.getValue().toString();
            ret.setAttribute(entry.getKey(), valueAsString);
        }
        return ret;
    }

    private Map<String, Object> getFields(Component component) {
        Map<String, Object> ret = new HashMap<>();
        Class<?> clazz = component.getClass();
        while (clazz != Object.class) {
            for (Field field : clazz.getFields()) {
                if (isNoticable(field.getType()) && !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(component);
                        ret.put(field.getName(), value);
                    } catch (Exception e) {
                        // not invokable => cant be searched for
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return ret;
    }

    private Map<String, Object> getMethods(Map<String, Object> existingValues, Component component) {
        for (Method method : component.getClass().getMethods()) {
            if (isNoticable(method.getReturnType()) && !Modifier.isStatic(method.getModifiers())
                    && (method.getName().startsWith("get") || method.getName().startsWith("is"))
                    && (method.getParameterTypes().length == 0)) {
                try {
                    String attributeName;
                    if (method.getName().startsWith("get")) {
                        attributeName = method.getName().substring(3);
                    } else {
                        attributeName = method.getName().substring(2);
                    }
                    attributeName = attributeName.substring(0, 1).toLowerCase() + attributeName.substring(1);
                    if (!existingValues.containsKey(attributeName)) {
                        method.setAccessible(true);
                        Object value = method.invoke(component);
                        if (value != null) {
                            existingValues.put(attributeName, value);
                        }
                    }
                } catch (Exception e) {
                    // not invokable => cant be searched for
                }
            }
        }

        return existingValues;
    }

    private static final List<Class<?>> interestingTypes = Arrays.asList(Boolean.class, Byte.class, Character.class,
            Double.class, Float.class, Integer.class, Long.class, Short.class, String.class);

    private boolean isNoticable(Class<?> type) {
        return type.isEnum() || type.isPrimitive() || interestingTypes.contains(type);
    }

    public static ContainerToDocumentTransformer getInstance(Container container, String expression) {
        if (SwingUtilities.isEventDispatchThread()) {
            return new ContainerToDocumentTransformer(container, expression);
        } else {
            Executor edt = EventQueue::invokeLater;
            try {
                return CompletableFuture
                        .supplyAsync(() -> new ContainerToDocumentTransformer(container, expression), edt).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
