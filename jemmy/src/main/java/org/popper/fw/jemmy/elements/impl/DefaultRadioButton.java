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

import org.netbeans.jemmy.operators.JRadioButtonOperator;
import org.popper.fw.jemmy.elements.IJemmyRadioButton;

/**
 * Implementation of {@link IJemmyRadioButton} based on {@link JRadioButtonOperator}
 *
 * @author michael
 */
public class DefaultRadioButton extends AbstractJemmyInput<JRadioButtonOperator> implements IJemmyRadioButton {

    public DefaultRadioButton(JemmyElementReference reference) {
        super(reference, JRadioButtonOperator.class);
    }

    /** (non-Javadoc)
     * @see org.popper.fw.jemmy.elements.IJemmyRadioButton#isSelected()
     */
    @Override
    public boolean isSelected() {
        return getOperator().isSelected();
    }

    /** (non-Javadoc)
     * @see org.popper.fw.jemmy.elements.IJemmyRadioButton#select()
     */
    @Override
    public void select() {
        getOperator().doClick();
    }
}
