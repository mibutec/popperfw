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


import org.popper.fw.webdriver.elements.IWebTextBox;

/**
 * Implementation of a TExtbox based on a Textarea
 * @author Michael
 *
 */
public class TextArea extends AbstractInput implements IWebTextBox {
	
	public TextArea(WebElementReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ITextBox#type(java.lang.String)
	 */
	@Override
	public void type(String text) {
		getElement().sendKeys(text);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ITextBox#clear()
	 */
	@Override
	public void clear() {
		getElement().clear();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ITextBox#text(java.lang.String)
	 */
	@Override
	public void text(String text) {
		clear();
		type(text);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ITextBox#getText()
	 */
	@Override
	public String getText() {
		return getElement().getAttribute("value");
	}
}
