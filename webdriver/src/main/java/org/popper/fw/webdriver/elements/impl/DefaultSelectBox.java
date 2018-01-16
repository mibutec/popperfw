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


import org.openqa.selenium.support.ui.Select;
import org.popper.fw.webdriver.elements.IWebSelectBox;

/**
 * Definition of a Selectbox based on defaiult-webcontrols
 * @author Michael
 *
 */
public class DefaultSelectBox extends AbstractInput implements IWebSelectBox {
	public DefaultSelectBox(WebElementReference reference) {
		super(reference);
	}

	protected Select getSelect() {
		return new Select(getElement());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ISelectBox#selectByText(java.lang.String)
	 */
	@Override
	public void selectByText(String text) {
		checkEditability();
		getSelect().selectByVisibleText(text);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ISelectBox#selectByValue(java.lang.String)
	 */
	@Override
	public void selectByValue(String value) {
		checkEditability();
		getSelect().selectByValue(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ISelectBox#getSelectedValue()
	 */
	@Override
	public String getSelectedValue() {
		return getSelect().getFirstSelectedOption().getAttribute("value");
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ISelectBox#getSelectedText()
	 */
	@Override
	public String getSelectedText() {
		return getSelect().getFirstSelectedOption().getText();
	}
}
