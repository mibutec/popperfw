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
package org.popper.fw.jemmy.elements.impl;

import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.popper.fw.jemmy.elements.IJemmyCheckbox;

/**
 * Implementation of {@link IJemmyCheckbox} based on {@link JCheckBoxOperator}
 *
 * @author michael
 */
public class DefaultCheckbox extends AbstractJemmyInput<JCheckBoxOperator> implements IJemmyCheckbox {

    public DefaultCheckbox(JemmyElementReference reference) {
        super(reference, JCheckBoxOperator.class);
    }

    @Override
    public void check() {
        getOperator().changeSelection(true);

    }

    @Override
    public void uncheck() {
        getOperator().changeSelection(false);
    }

    /** (non-Javadoc)
     * @see org.popper.fw.element.ICheckbox#toggle()
     */
    @Override
    public void toggle() {
        JCheckBoxOperator op = getOperator();
        op.changeSelection(!op.isSelected());
    }

    /** (non-Javadoc)
     * @see org.popper.fw.element.ICheckbox#ischecked()
     */
    @Override
    public boolean ischecked() {
        return getOperator().isSelected();
    }
}
