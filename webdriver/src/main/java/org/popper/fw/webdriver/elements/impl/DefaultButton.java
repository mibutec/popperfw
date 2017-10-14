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

package org.popper.fw.webdriver.elements.impl;



import org.popper.fw.webdriver.elements.IWebButton;

/**
 * Implementation of a button that's represented by a default button control
 * @author Michael
 *
 */
public class DefaultButton extends AbstractWebElement implements IWebButton {
	
	public DefaultButton(WebElementReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.IButton#click()
	 */
	@Override
	public void click() {
		if (!isEditable()) {
			throw new RuntimeException("May not interact with not editable element");
		}

		getElement().click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.IButton#text()
	 */
	@Override
	public String text() {
		return getElement().getText();
	}
	
	public boolean isEditable() {
		return getWebelement().isEnabled();
	}
}
