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

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.WindowOperator;
import org.popper.fw.impl.PageObjectImplementation;

public class JemmyPageObjectHelper {
    public static PageObjectImplementation createDefaultPageObjectImplementation(Class<?> basicClass, String name,
            PageObjectImplementation parentPo, JemmyContext context, ComponentChooser chooser) {
        PageObjectImplementation poi = new PageObjectImplementation(context, basicClass, name, parentPo);
        poi.addExtension(new DefaultPoExtension(poi, chooser, context));

        return poi;
    }

    public static PageObjectImplementation createWindowPageObjectImplementation(Class<?> basicClass, String name,
            JemmyContext context, ComponentChooser chooser) {
        PageObjectImplementation poi = new PageObjectImplementation(context, basicClass, name, null);
        poi.addExtension(new WindowPoExtension(chooser, context));

        return poi;
    }

    public static PageObjectImplementation createLegacyPageObjectImplementation(Class<?> basicClass, String name,
            PageObjectImplementation parentPo, JemmyContext context, ContainerOperator operator) {
        PageObjectImplementation poi = new PageObjectImplementation(context, basicClass, name, parentPo);
        poi.addExtension(new LegacyPoExtension(operator, context));

        return poi;
    }

    public interface SearchContextProvider {
        public ContainerOperator getSearchContext();

        public boolean searchesForDialog();

        default Container getContainer() {
            return (Container) getSearchContext().getSource();
        }
    }

    private static class DefaultPoExtension implements SearchContextProvider {
        private final PageObjectImplementation poi;

        private final ComponentChooser chooser;

        private final JemmyContext context;

        protected DefaultPoExtension(PageObjectImplementation poi, ComponentChooser chooser, JemmyContext context) {
            this.chooser = chooser;
            this.poi = poi;
            this.context = context;
        }

        @Override
        public ContainerOperator getSearchContext() {
            ContainerOperator ret;
            if (poi.getParent() == null) {
                context.waitForEvents();
                ret = new ContainerOperator(WindowOperator.waitWindow(chooser));
            } else {
                ContainerOperator parentOperator = poi.getParent().getExtension(SearchContextProvider.class)
                        .getSearchContext();
                ret = new ContainerOperator(parentOperator, chooser);

            }

            context.flashComponent(ret);
            return ret;
        }

        @Override
        public boolean searchesForDialog() {
            return false;
        }
    }

    public static class WindowPoExtension implements SearchContextProvider {
        public ComponentChooser chooser;
        private final JemmyContext context;

        public WindowPoExtension(ComponentChooser chooser, JemmyContext context) {
            super();
            this.chooser = chooser;
            this.context = context;
        }

        @Override
        public WindowOperator getSearchContext() {
            context.waitForEvents();
            WindowOperator ret = new WindowOperator(WindowOperator.waitWindow(chooser));
            context.flashComponent(ret);
            return ret;
        }

        @Override
        public boolean searchesForDialog() {
            return true;
        }
    }

    private static class LegacyPoExtension implements SearchContextProvider {
        public final ContainerOperator operator;

        private final JemmyContext context;

        protected LegacyPoExtension(ContainerOperator operator, JemmyContext context) {
            this.operator = operator;
            this.context = context;
        }

        @Override
        public ContainerOperator getSearchContext() {
            context.flashComponent(operator);
            return operator;
        }

        @Override
        public boolean searchesForDialog() {
            return false;
        }
    }

    public static class AllWindowsSearchContextProvider implements SearchContextProvider {

        @Override
        public ContainerOperator getSearchContext() {
            return new ContainerOperator(getContainer());
        }

        @Override
        public Container getContainer() {
            return new Container() {
                @Override
                public Component[] getComponents() {
                    return Window.getWindows();
                }
            };
        }

        @Override
        public boolean searchesForDialog() {
            return true;
        }

    }
}
