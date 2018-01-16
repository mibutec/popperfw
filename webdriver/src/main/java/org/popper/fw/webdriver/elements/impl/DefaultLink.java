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


import org.popper.fw.webdriver.elements.IWebLink;

/**
 * Impelementation of a link
 * @author Michael
 *
 */
public class DefaultLink extends AbstractWebElement implements IWebLink {
	
	public DefaultLink(WebElementReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ILink#click()
	 */
	@Override
	public void click() {
		getElement().click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ILink#text()
	 */
	@Override
	public String text() {
		String ret = getElement().getText();
		if (ret == null) {
			return ret;
		}
		
		return ret.trim();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ILink#href()
	 */
	@Override
	public String href() {
		return getElement().getAttribute("href");
	}
}
