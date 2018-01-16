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


import org.popper.fw.webdriver.elements.IWebCheckbox;

/**
 * Implementation of a button that's represented by a default checkbox control
 * @author Michael
 *
 */
public class DefaultCheckbox extends AbstractInput implements IWebCheckbox {
	
	public DefaultCheckbox(WebElementReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ICheckbox#check()
	 */
	@Override
	public void check() {
		checkEditability();
		if (!getElement().isSelected()) {
			getElement().click();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ICheckbox#uncheck()
	 */
	@Override
	public void uncheck() {
		checkEditability();
		if (getElement().isSelected()) {
			getElement().click();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ICheckbox#toggle()
	 */
	@Override
	public void toggle() {
		checkEditability();
		getElement().click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ICheckbox#ischecked()
	 */
	@Override
	public boolean ischecked() {
		return getElement().isSelected();
	}
}
