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

import java.util.List;

import org.netbeans.jemmy.operators.ChoiceOperator;
import org.popper.fw.jemmy.elements.IJemmySelectbox;

/**
 * Implementation of {@link IJemmySelectbox}
 *
 * @author Michael
 */
public class DefaultSelectbox extends AbstractJemmyInput<ChoiceOperator> implements IJemmySelectbox {
    public DefaultSelectbox(JemmyElementReference elementReference) {
        super(elementReference, ChoiceOperator.class);
    }

    /** (non-Javadoc)
     * @see org.popper.fw.element.ISelectBox#selectByText(java.lang.String)
     */
    @Override
    public void selectByText(String text) {
        getOperator().selectItem(text);

    }

    /** (non-Javadoc)
     * @see org.popper.fw.element.ISelectBox#getSelectedText()
     */
    @Override
    public String getSelectedText() {
        return getOperator().getSelectedItem();
    }

    /** (non-Javadoc)
     * @see org.popper.fw.jemmy.elements.IJemmySelectbox#allValues()
     */
    @Override
    public List<String> allValues() {
        throw new IllegalStateException("not implemented");
    }
}
