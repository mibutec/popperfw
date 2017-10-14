/*
 * Copyright [2013] [Michael Bulla, michaelbulla@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.popper.fw.jemmy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.popper.fw.helper.AnnotationFinder;
import org.popper.fw.impl.AbstractPopperContext.ObjectInstantiator;
import org.popper.fw.impl.ImplHolder;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.jemmy.JemmyPageObjectHelper.AllWindowsSearchContextProvider;
import org.popper.fw.jemmy.annotations.Dialog;
import org.popper.fw.jemmy.annotations.Frame;
import org.popper.fw.jemmy.annotations.locator.Locator;
import org.popper.fw.jemmy.annotations.locator.Locator.LocatorProcessor;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyFactory;

public class JemmyPoFactory implements IPoFactory {
    private final JemmyContext context;

    public JemmyContext getContext() {
        return context;
    }

    public JemmyPoFactory(JemmyContext context) {
        this.context = context;
    }

    public <T extends Object> T createPo(Class<T> type, String name, ComponentChooser chooser,
            PageObjectImplementation parent) {
        PageObjectImplementation poi = JemmyPageObjectHelper.createDefaultPageObjectImplementation(type, name, parent,
                context, chooser);

        return createProxy(type, poi);
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T createProxy(Class<T> type, PageObjectImplementation poi) {
        // TODO: Now using a mix of Java dynamic proxy and cglib => should use one this both
        if (!type.isInterface()) {
            ProxyFactory factory = new ProxyFactory();
            factory.setSuperclass(type);
            factory.setInterfaces(new Class<?>[] {ImplHolder.class});
            factory.setFilter(new MethodFilter() {
                @Override
                public boolean isHandled(Method method) {
                    return Modifier.isAbstract(method.getModifiers());
                }
            });

            try {
                Map<Class<?>, Object> resolver = new HashMap<>();
                ObjectInstantiator instantiator = new ObjectInstantiator();
                return (T) factory.create(instantiator.getConstructorDefinition(type),
                        instantiator.getParameters(type, context.getClassResolver(), resolver), poi);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] {type, ImplHolder.class},
                    poi);
        }
    }

    public <T> T createLegacyPage(Class<T> type, ContainerOperator representingContainer) {
        String name = nameFromPage(null, type);
        PageObjectImplementation parent = null;
        PageObjectImplementation poi = JemmyPageObjectHelper.createLegacyPageObjectImplementation(type, name, parent,
                context, representingContainer);

        return createProxy(type, poi);
    }

    @Override
    public <T> T createPage(Class<T> type) {
        String name = null;
        ComponentChooser chooser = null;

        Dialog dialogAnnotation = AnnotationFinder.findAnnotation(type, Dialog.class);
        if (dialogAnnotation != null) {
            name = nameFromPage(dialogAnnotation.name(), type);
            chooser = LocatorProcessor.annotationToChooser(type.getAnnotation(Locator.class), new Object[0],
                    new AllWindowsSearchContextProvider(), false);
        }

        Frame frameAnnotation = AnnotationFinder.findAnnotation(type, Frame.class);
        if (frameAnnotation != null) {
            name = nameFromPage(frameAnnotation.name(), type);
            chooser = LocatorProcessor.annotationToChooser(type.getAnnotation(Locator.class), new Object[0],
                    new AllWindowsSearchContextProvider(), false);
        }

        if (chooser != null) {
            PageObjectImplementation poi = JemmyPageObjectHelper.createWindowPageObjectImplementation(type, name,
                    context, chooser);
            return createProxy(type, poi);
        }

        throw new RuntimeException("createPage may only be used for classes annotated with @Frame or @Dialog."
                + type.getName() + " doesn't have.");

    }

    private String nameFromPage(String name, Class<?> clazz) {
        if ((name == null) || name.isEmpty()) {
            String[] split = StringUtils.splitByCharacterTypeCamelCase(clazz.getName());
            return Arrays.stream(split).collect(Collectors.joining(" "));
        }
        return name;
    }
}