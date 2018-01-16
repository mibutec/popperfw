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
package org.popper.testfactory;

import org.popper.fw.element.IButton;
import org.popper.fw.element.ICheckbox;
import org.popper.fw.element.IFileupload;
import org.popper.fw.element.IImage;
import org.popper.fw.element.ILabel;
import org.popper.fw.element.ILink;
import org.popper.fw.element.IRadioButton;
import org.popper.fw.element.ISelectBox;
import org.popper.fw.element.ITextBox;
import org.popper.fw.webdriver.elements.impl.AbstractWebElement;
import org.popper.fw.webdriver.elements.impl.WebElementReference;

public class Superelement extends AbstractWebElement implements IButton, ICheckbox, IFileupload, IImage, ILabel, ILink, IRadioButton, ISelectBox, ITextBox {

	public Superelement(WebElementReference reference) {
		super(reference);
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public void type(String text) {
		
	}

	@Override
	public void clear() {
		
	}

	@Override
	public void text(String text) {
		
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public void selectByText(String text) {
		
	}

	@Override
	public String getSelectedText() {
		
		return null;
	}

	@Override
	public void select() {
		
		
	}

	@Override
	public boolean isSelected() {
		
		return false;
	}

	@Override
	public String href() {
		
		return null;
	}

	@Override
	public boolean isRendered() {
		
		return false;
	}

	@Override
	public String getUrl() {
		
		return null;
	}

	@Override
	public void uploadFromClasspath(String resourcename) {
		
		
	}

	@Override
	public String filename() {
		
		return null;
	}

	@Override
	public void check() {
		
		
	}

	@Override
	public void uncheck() {
		
		
	}

	@Override
	public void toggle() {
		
		
	}

	@Override
	public boolean ischecked() {
		
		return false;
	}

	@Override
	public void click() {
		
		
	}

	@Override
	public String text() {
		
		return null;
	}
}
