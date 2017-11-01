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
package org.popper.fw.jemmy.annotations.locator;

import java.awt.Component;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

import org.netbeans.jemmy.ComponentChooser;
import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.impl.AbstractPopperContext;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.jemmy.JemmyContext;
import org.popper.fw.jemmy.JemmyPageObjectHelper.AllWindowsSearchContextProvider;
import org.popper.fw.jemmy.JemmyPageObjectHelper.SearchContextProvider;
import org.popper.fw.jemmy.annotations.locator.Locator.LocatorProcessor;

@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(LocatorProcessor.class)
public @interface Locator {
    Class<?> componentClass() default Object.class;

    String name() default "";

    String title() default "";

    String xpath() default "";

    int index() default -1;

    public static class LocatorProcessor extends AbstractLocatorAnnotationProcessor<Locator> {

        public LocatorProcessor(JemmyContext context, IPoFactory factory) {
            super(context, factory);
        }

        @Override
        protected ComponentChooser getChooser(Locator annotation, LocatorContextInformation info) {
            SearchContextProvider scp = info.getParent().getExtension(SearchContextProvider.class);
            return annotationToChooser(annotation, info.getParameters(), scp,
                    info.getAnnotationHolder().getAnnotation(Window.class) != null);
        }

        public static ComponentChooser annotationToChooser(Locator annotation, Object[] parameters,
                SearchContextProvider searchContextProvider, boolean isFakeBlaScheiss) {
            if (isFakeBlaScheiss) {
                searchContextProvider = new AllWindowsSearchContextProvider();
            }
            if (annotation.componentClass() != Object.class) {
                return new ComponentByClassNameChooser(annotation.componentClass());
            } else if (!annotation.name().isEmpty()) {
                return propChooser(searchContextProvider, "name", annotation.name(), parameters);
            } else if (!annotation.title().isEmpty()) {
                return propChooser(searchContextProvider, "title", annotation.title(), parameters);
            } else if (!annotation.xpath().isEmpty()) {
                return new ComponentByXpathChooser(searchContextProvider,
                        AbstractPopperContext.replaceVariables(annotation.xpath(), parameters),
                        searchContextProvider.searchesForDialog());
            } else if (annotation.index() >= 0) {
                return new ComponentByIndexChooser(annotation.index());
            } else {
                throw new RuntimeException("When using @" + annotation.annotationType().getSimpleName()
                        + " you need to set at least one of its parameters.");
            }
        }

        private static ComponentChooser propChooser(SearchContextProvider searchContextProvider, String name,
                String value, Object[] parameters) {
            return new ComponentByXpathChooser(searchContextProvider,
                    AbstractPopperContext.replaceVariables("//*[@" + name + "='" + value + "']", parameters),
                    searchContextProvider.searchesForDialog());
        }
    }

    /**
     * When using {@link ComponentChooser} a chooser is used several times for the same components. You never know when a new round
     * is started. A {@link AbstractResetableChooser} tries to find out when a new round is started and allows you to reset the state
     * of your chooser.
     *
     * @author Michael
     *
     */
    public static abstract class AbstractResetableChooser implements ComponentChooser {
        private final Set<Component> checkedComponents = new HashSet<>();

        /**
         * Same as org.netbeans.jemmy.ComponentChooser.checkComponent(Component)
         */
        protected abstract boolean checkComponentWithReset(Component comp);

        protected abstract void reset();

        /**
         * (non-Javadoc)
         *
         * @see org.netbeans.jemmy.ComponentChooser#checkComponent(java.awt.Component)
         */
        @Override
        public final boolean checkComponent(Component comp) {
            // the same chooser is used several time to check all components. We
            // have to reset the counter when starting a new round
            if (checkedComponents.contains(comp)) {
                checkedComponents.clear();
                reset();
            } else {
                checkedComponents.add(comp);
            }

            return checkComponentWithReset(comp);
        }
    }

    public static class ComponentByClassNameChooser implements ComponentChooser {

        /** ClassName of component **/
        private final Class<?> clazz;

        /**
         * Constructor
         *
         * @param clazz
         *            name of class which should be find.
         */
        public ComponentByClassNameChooser(final Class<?> clazz) {
            this.clazz = clazz;
        }

        /**
         * @see org.netbeans.jemmy.ComponentChooser#getDescription()
         */
        @Override
        public String getDescription() {
            return ComponentByClassNameChooser.class.getSimpleName() + " (" + clazz.getName() + ")";
        }

        /**
         * @see org.netbeans.jemmy.ComponentChooser#checkComponent(java.awt.Component)
         */
        @Override
        public boolean checkComponent(final java.awt.Component comp) {
            if (clazz.isAssignableFrom(comp.getClass())) {
                return true;
            }

            return false;
        }
    }

    public static class ComponentByIndexChooser extends AbstractResetableChooser {
        private final int indexToChoose;

        private int indexCount = 0;

        public ComponentByIndexChooser(int indexToChoose) {
            super();
            this.indexToChoose = indexToChoose;
        }

        @Override
        public String getDescription() {
            return ComponentByIndexChooser.class.getSimpleName() + " (" + indexToChoose + ")";
        }

        /**
         * (non-Javadoc)
         *
         * @see org.netbeans.jemmy.ComponentChooser#checkComponent(java.awt.Component)
         */
        @Override
        public boolean checkComponentWithReset(Component comp) {
            return indexCount++ == indexToChoose;
        }

        @Override
        protected void reset() {
            indexCount = 0;
        }
    }

    public static class ComponentByXpathChooser extends AbstractResetableChooser {
        private final String expression;

        private final SearchContextProvider searchContextProvider;

        private final boolean forceVisible;

        private ContainerToDocumentTransformer transformerCache;

        private ContainerToDocumentTransformer getContainerToDocumentTransformer() {
            if (transformerCache == null) {
                transformerCache = new ContainerToDocumentTransformer(searchContextProvider.getContainer(), expression);
            }

            return transformerCache;
        }

        protected ComponentByXpathChooser(SearchContextProvider searchContextProvider, String expression,
                boolean forceVisible) {
            this.expression = expression;
            this.forceVisible = forceVisible;
            this.searchContextProvider = searchContextProvider;
        }

        @Override
        public String getDescription() {
            return ComponentByXpathChooser.class.getSimpleName() + " (" + expression + ")";
        }

        @Override
        public boolean checkComponentWithReset(Component comp) {
            if (forceVisible && !comp.isVisible()) {
                return false;
            }
            return getContainerToDocumentTransformer().findComponentMatching(expression).contains(comp);
        }

        @Override
        protected void reset() {
            transformerCache = null;
        }
    }
}
