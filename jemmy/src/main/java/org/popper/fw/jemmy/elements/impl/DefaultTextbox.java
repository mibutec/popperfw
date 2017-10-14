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

import java.awt.TextComponent;

import org.netbeans.jemmy.operators.TextComponentOperator;
import org.popper.fw.jemmy.elements.IJemmyTextbox;

/**
 * Implementation of {@link IJemmyTextbox}
 *
 * @author Michael
 */
public class DefaultTextbox extends AbstractJemmyInput<TextComponentOperator> implements IJemmyTextbox {
    public DefaultTextbox(JemmyElementReference reference) {
        super(reference, TextComponentOperator.class, TextComponent.class);
    }

    /** (non-Javadoc)
     * @see org.popper.fw.element.ITextBox#type(java.lang.String)
     */
    @Override
    public void type(String text) {
        TextComponentOperator operator = getOperator();
        for (int i = 0; i < text.length(); i++) {
            operator.typeKey(text.charAt(i));
        }
    }

    /** (non-Javadoc)
     * @see org.popper.fw.element.ITextBox#clear()
     */
    @Override
    public void clear() {
        getOperator().setText("");
    }

    /** (non-Javadoc)
     * @see org.popper.fw.element.ITextBox#text(java.lang.String)
     */
    @Override
    public void text(String text) {
        getOperator().setText(text);
    }

    /** (non-Javadoc)
     * @see org.popper.fw.element.ITextBox#getText()
     */
    @Override
    public String getText() {
        return getOperator().getText();
    }
}