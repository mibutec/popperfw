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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.Operator;
import org.popper.fw.impl.ImplHolder;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.impl.ReturnTypeHandler;
import org.popper.fw.impl.ReturnTypeHandler.ReturnTypeFactory;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.jemmy.JemmyContext;
import org.popper.fw.jemmy.JemmyPageObjectHelper.WindowPoExtension;
import org.popper.fw.jemmy.elements.impl.JemmyElementReference;

/**
 * Base class for all {@link IAnnotationProcessor}s providing page objects or elements by locators

 * @author michael_bulla
 */
public abstract class AbstractLocatorAnnotationProcessor<A extends Annotation>
        implements IAnnotationProcessor<A, Object>, ReturnTypeFactory<A, ContainerOperator> {

    protected final Logger log = LogManager.getLogger(getClass());

    protected final JemmyContext context;

    protected final IPoFactory factory;

    protected final ReturnTypeHandler<A, ContainerOperator> returnTypeHandler = new ReturnTypeHandler<>(this);

    public AbstractLocatorAnnotationProcessor(JemmyContext context, IPoFactory factory) {
        this.context = context;
        this.factory = factory;
    }

    @Override
    public Object processAnnotation(A annotation, LocatorContextInformation info, Object lastResult) {
        try {
            if (lastResult != null) {
                if (lastResult instanceof ImplHolder) {
                    PageObjectImplementation poi = ((ImplHolder) lastResult).getImpl();
                    poi.getExtension(WindowPoExtension.class).chooser = getChooser(annotation, info);
                    return lastResult;
                } else {
                    throw new RuntimeException("There was a lastResult given to @"
                            + annotation.annotationType().getSimpleName() + " of type "
                            + lastResult.getClass().getName() + ". lastResult for this annotation may only be of type "
                            + ImplHolder.class.getSimpleName() + " or null.");
                }
            }
            return returnTypeHandler.createReturnObject(info, annotation);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract ComponentChooser getChooser(A annotation, LocatorContextInformation info);

    @Override
    public List<ContainerOperator> handleCollectionType(A annotation, LocatorContextInformation info) {
        throw new IllegalStateException("not upported by jemmy");
    }

    @Override
    public Object handleCountType(LocatorContextInformation info, A annotation) {
        throw new IllegalStateException("not upported by jemmy");
    }

    protected String getName(Method method, A locator) {
        String[] split = StringUtils.splitByCharacterTypeCamelCase(method.getName());
        return Arrays.stream(split).collect(Collectors.joining(" "));
    }

	@Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object createObject(A annotation, LocatorContextInformation info, Class<?> type,
            ContainerOperator operator) {
        final String locatorName = getName(info.getMethod(), annotation);
        PageObjectImplementation parent = info.getParent();
        ComponentChooser chooser = getChooser(annotation, info);
        Class<?> givenImplClass = context.getImplementingClassFor(info.getMethod(), type);
        if (Operator.class.isAssignableFrom(type)) {
        	return ((JemmyContext) info.getPopperContext()).createOperator((Class) type, info.getParent(), chooser);
        } else if (givenImplClass == null) {
            return context.getFactory().createPo(type, locatorName, chooser, parent);
        } else {
            JemmyElementReference reference = new JemmyElementReference(locatorName, parent, chooser, context);
            Map<Class<?>, Object> resolver = new HashMap<>();
            resolver.put(JemmyElementReference.class, reference);
            resolver.put(ComponentChooser.class, chooser);
            resolver.put(String.class, locatorName);
            resolver.put(PageObjectImplementation.class, parent);
            return context.instantiateObject(givenImplClass, resolver);
        }
    }
}
