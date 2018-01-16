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
package org.popper.fw.webdriver.elements.impl;


import org.popper.fw.webdriver.elements.IWebRadioButton;

/**
 * Implementation of a RadioButton as a Default-Web-Control
 * @author Michael
 *
 */
public class DefaultRadioButton extends AbstractInput implements IWebRadioButton {
	
	public DefaultRadioButton(WebElementReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.IRadioButton#select()
	 */
	@Override
	public void select() {
		checkEditability();
		getElement().click();
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.IRadioButton#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return getElement().isSelected();
	}
}
