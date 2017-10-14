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

import java.util.HashSet;
import java.util.Set;

import org.popper.fw.interfaces.IElementFactory;
import org.popper.fw.jemmy.elements.impl.DefaultButton;
import org.popper.fw.jemmy.elements.impl.DefaultCheckbox;
import org.popper.fw.jemmy.elements.impl.DefaultLabel;
import org.popper.fw.jemmy.elements.impl.DefaultMenuBar;
import org.popper.fw.jemmy.elements.impl.DefaultRadioButton;
import org.popper.fw.jemmy.elements.impl.DefaultSelectbox;
import org.popper.fw.jemmy.elements.impl.DefaultTextbox;

public class JemmyDefaultElementFactory implements IElementFactory {
    private static final Set<Class<?>> impls = new HashSet<>();
    static {
        impls.add(DefaultButton.class);
        impls.add(DefaultLabel.class);
        impls.add(DefaultSelectbox.class);
        impls.add(DefaultTextbox.class);
        impls.add(DefaultMenuBar.class);
        impls.add(DefaultRadioButton.class);
        impls.add(DefaultCheckbox.class);
    }

    @Override
    public Class<?> getImplClassForElement(Class<?> clazz) {
        for (Class<?> defaultImpl : impls) {
            if (clazz.isAssignableFrom(defaultImpl)) {
                return defaultImpl;
            }
        }
        return null;
    }

    public void addImplClassForElement(Class<?> targetClass) {
        impls.add(targetClass);
    }
}
