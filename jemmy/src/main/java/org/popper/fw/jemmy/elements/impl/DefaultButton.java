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
package org.popper.fw.jemmy.elements.impl;

import org.netbeans.jemmy.operators.JButtonOperator;
import org.popper.fw.jemmy.elements.IJemmyButton;

public class DefaultButton extends AbstractJemmyElement<JButtonOperator> implements IJemmyButton {

    public DefaultButton(JemmyElementReference reference) {
        super(reference, JButtonOperator.class);
    }

    @Override
    public void click() {
        getOperator().push();
    }

    @Override
    public String text() {
        return getOperator().getText();
    }
    
    public boolean isEditable() {
    	return getOperator().isEnabled();
    }
}
