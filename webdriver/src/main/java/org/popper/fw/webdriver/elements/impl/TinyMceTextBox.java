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


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.popper.fw.webdriver.elements.IWebTextBox;

/**
 * Implementation of a Textbox based on a TinyMCE Richedit-Texteditor
 * @author Michael
 *
 */
public class TinyMceTextBox extends AbstractInput implements IWebTextBox {
	private String id;
	
	public TinyMceTextBox(WebElementReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ITextBox#type(java.lang.String)
	 */
	@Override
	public void type(String text) {
		getDriver().switchTo().frame(id + "_ifr");
		getTextElement().sendKeys(text);
		getDriver().switchTo().defaultContent();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ITextBox#clear()
	 */
	@Override
	public void clear() {
		throw new RuntimeException("Methode clear nicht implementiert");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ITextBox#text(java.lang.String)
	 */
	@Override
	public void text(String text) {
//		clear();
		type(text);
	}
	
	private WebElement getTextElement() {
		return getDriver().findElement(By.id("tinymce"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.ITextBox#getText()
	 */
	@Override
	public String getText() {
		getDriver().switchTo().frame(id + "_ifr");
		String text = getTextElement().getText();
		getDriver().switchTo().defaultContent();
		
		return text;
	}
}
