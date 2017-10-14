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

package org.popper.testpos;

import org.popper.fw.annotations.Type;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.PageAccessor;
import org.popper.fw.webdriver.annotations.locator.Locator;
import org.popper.fw.webdriver.elements.IWebButton;
import org.popper.fw.webdriver.elements.IWebCheckbox;
import org.popper.fw.webdriver.elements.IWebFileupload;
import org.popper.fw.webdriver.elements.IWebImage;
import org.popper.fw.webdriver.elements.IWebLabel;
import org.popper.fw.webdriver.elements.IWebLink;
import org.popper.fw.webdriver.elements.IWebRadioButton;
import org.popper.fw.webdriver.elements.IWebSelectBox;
import org.popper.fw.webdriver.elements.IWebTextBox;
import org.popper.fw.webdriver.elements.impl.TextArea;

@Page(name="Login")
public interface ManyWebTypesPO {
	@PageAccessor(uri="/pages/severalTypes.html")
	void open();
	
	@Locator(name="Button", id="button_normal")
	IWebButton button();
	
	@Locator(name="Checkbox", id="checkbox_normal")
	IWebCheckbox checkbox();
	
	@Locator(name="Fileupload", id="file_normal")
	IWebFileupload fileupload();
	
	@Locator(name="Label", id="label_normal")
	IWebLabel label();
	
	@Locator(name="Image", id="image_normal")
	IWebImage image();
	
	@Locator(name="Link", id="link_normal")
	IWebLink link();
	
	@Locator(name="Radio", id="radio1")
	IWebRadioButton radiobutton();
	
	@Locator(name="Select", id="normal_select")
	IWebSelectBox select();
	
	@Locator(name="Textbox", id="textbox_normal")
	IWebTextBox textbox();
	
	@Locator(name="Textarea", id="textarea_normal")
	@Type(TextArea.class)
	IWebTextBox textarea();

	@Locator(id="textarea_normal")
	TextArea textareaNoTypeAnnotation();
}



