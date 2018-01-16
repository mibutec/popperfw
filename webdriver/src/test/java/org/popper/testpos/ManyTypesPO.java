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
package org.popper.testpos;

import org.popper.fw.annotations.Type;
import org.popper.fw.element.IButton;
import org.popper.fw.element.ICheckbox;
import org.popper.fw.element.IFileupload;
import org.popper.fw.element.IImage;
import org.popper.fw.element.ILabel;
import org.popper.fw.element.ILink;
import org.popper.fw.element.IRadioButton;
import org.popper.fw.element.ISelectBox;
import org.popper.fw.element.ITextBox;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.PageAccessor;
import org.popper.fw.webdriver.annotations.locator.Locator;
import org.popper.fw.webdriver.elements.impl.TextArea;

@Page(name="Login")
public interface ManyTypesPO {
	@PageAccessor(uri="/pages/severalTypes.html")
	void open();
	
	@Locator(name="Button", id="button_normal")
	IButton button();
	
	@Locator(name="Checkbox", id="checkbox_normal")
	ICheckbox checkbox();
	
	@Locator(name="Fileupload", id="file_normal")
	IFileupload fileupload();
	
	@Locator(name="Label", id="label_normal")
	ILabel label();
	
	@Locator(name="Image", id="image_normal")
	IImage image();
	
	@Locator(name="Link", id="link_normal")
	ILink link();
	
	@Locator(name="Radio", id="radio1")
	IRadioButton radiobutton();
	
	@Locator(name="Select", id="normal_select")
	ISelectBox select();
	
	@Locator(name="Textbox", id="textbox_normal")
	ITextBox textbox();
	
	@Locator(name="Textarea", id="textarea_normal")
	@Type(TextArea.class)
	ITextBox textarea();

	@Locator(id="textarea_normal")
	TextArea textareaNoTypeAnnotation();
}



