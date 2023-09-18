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
package org.popper.fw.webdriver.elements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.popper.fw.webdriver.WebdriverPageObjectHelper;
import org.popper.fw.webdriver.elements.impl.DefaultButton;
import org.popper.fw.webdriver.elements.impl.DefaultCheckbox;
import org.popper.fw.webdriver.elements.impl.DefaultFileupload;
import org.popper.fw.webdriver.elements.impl.DefaultImage;
import org.popper.fw.webdriver.elements.impl.DefaultLabel;
import org.popper.fw.webdriver.elements.impl.DefaultLink;
import org.popper.fw.webdriver.elements.impl.DefaultRadioButton;
import org.popper.fw.webdriver.elements.impl.DefaultSelectBox;
import org.popper.fw.webdriver.elements.impl.DefaultTextBox;
import org.popper.fw.webdriver.elements.impl.TextArea;
import org.popper.fw.webdriver.elements.impl.WebElementReference;
import org.popper.inttest.AbstractIntTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ElementTest extends AbstractIntTest {
	@BeforeEach
	public void setup() {
		super.setup();
		factory.getContext().openRelativeUri("/elements/ElementTest.html");
	}
	
	@Test
	public void testDefaultButton() {
		DefaultButton normalButton = new DefaultButton(new WebElementReference("Testbutton", null, By.id("button_normal"), factory.getContext()));
		assertEquals("normal", normalButton.text());
		assertTrue(normalButton.isEditable());
		
		DefaultButton disabledButton = new DefaultButton(new WebElementReference("Testbutton", null, By.id("button_disabled"), factory.getContext()));
		assertEquals("disabled", disabledButton.text());
		assertFalse(disabledButton.isEditable());
		try {
			disabledButton.click();
			fail("interacting with disabled button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
		assertEquals("", factory.getContext().getDriver().findElement(By.id("actionArea")).getText());

		DefaultButton clickableButton = new DefaultButton(new WebElementReference("Testbutton", null, By.id("button_clickable"), factory.getContext()));
		assertEquals("clickable", clickableButton.text());
		assertTrue(clickableButton.isEditable());
		clickableButton.click();
		assertEquals("Hallo", factory.getContext().getDriver().findElement(By.id("actionArea")).getText());
		
		DefaultButton imageButton = new DefaultButton(new WebElementReference("Testbutton", null, By.id("button_image"), factory.getContext()));
		assertEquals("", imageButton.text());
		
		DefaultButton innerButton = new DefaultButton(new WebElementReference("Testbutton", WebdriverPageObjectHelper.createPageObjectImplementation(Object.class, By.id("parent"), "Parent", null, factory.getContext(), null), By.id("button_inner"), factory.getContext()));
		assertEquals("inner", innerButton.text());
	}
	
	@Test
	public void testDefaultCheckbox() {
		DefaultCheckbox normalCheckbox = new DefaultCheckbox(new WebElementReference("Testcheckbox", null, By.id("checkbox_normal"), factory.getContext()));
		assertFalse(normalCheckbox.ischecked());
		normalCheckbox.check();
		assertTrue(normalCheckbox.ischecked());
		normalCheckbox.check();
		assertTrue(normalCheckbox.ischecked());
		normalCheckbox.uncheck();
		assertFalse(normalCheckbox.ischecked());
		normalCheckbox.uncheck();
		assertFalse(normalCheckbox.ischecked());
		normalCheckbox.toggle();
		assertTrue(normalCheckbox.ischecked());
		normalCheckbox.toggle();
		assertFalse(normalCheckbox.ischecked());
		
		DefaultCheckbox disabledCheckbox = new DefaultCheckbox(new WebElementReference("Testcheckbox", null, By.id("checkbox_disabled"), factory.getContext()));
		assertFalse(disabledCheckbox.ischecked());
		assertFalse(disabledCheckbox.isEditable());
		try {
			disabledCheckbox.toggle();
			fail("interacting with disabled checkbox not prohibited");
		} catch (RuntimeException re) {
			// OK
		}

		try {
			disabledCheckbox.check();
			fail("interacting with disabled checkbox not prohibited");
		} catch (RuntimeException re) {
			// OK
		}

		try {
			disabledCheckbox.uncheck();
			fail("interacting with disabled checkbox not prohibited");
		} catch (RuntimeException re) {
			// OK
		}

		assertFalse(disabledCheckbox.ischecked());
		
		DefaultCheckbox innerCheckbox = new DefaultCheckbox(new WebElementReference("Testcheckbox", WebdriverPageObjectHelper.createPageObjectImplementation(Object.class, By.id("parent"), "Parent", null, factory.getContext(), null), By.id("checkbox_inner"), factory.getContext()));
		assertFalse(innerCheckbox.ischecked());
	}
	
	@Test
	public void testDefaultFileupload() {
		DefaultFileupload normalFileupload = new DefaultFileupload(new WebElementReference("Testfileupload", null, By.id("file_normal"), factory.getContext()));
		assertNull(normalFileupload.filename());
		normalFileupload.uploadFromClasspath("/elements/en.png");
		assertEndsWith(normalFileupload.filename(), "en.png");
		
		DefaultFileupload disabledFileupload = new DefaultFileupload(new WebElementReference("Testfileupload", null, By.id("file_disabled"), factory.getContext()));
		assertFalse(disabledFileupload.isEditable());
		try {
			disabledFileupload.uploadFromClasspath("/elements/en.png");
			fail("interacting with disabled file upload not prohibited");
		} catch (RuntimeException re) {
			// OK
		}

		assertNull(disabledFileupload.filename());
		
		DefaultFileupload innerFileupload = new DefaultFileupload(new WebElementReference("Testcheckbox", WebdriverPageObjectHelper.createPageObjectImplementation(Object.class, By.id("parent"), "Parent", null, factory.getContext(), null), By.id("file_inner"), factory.getContext()));
		assertNull(innerFileupload.filename());
		innerFileupload.uploadFromClasspath("/elements/en.png");
		assertTrue(innerFileupload.filename().endsWith("en.png"));
	}
	
	@Test
	public void testDefaultimage() {
		DefaultImage normalImage = new DefaultImage(new WebElementReference("Testimage", null, By.id("image_normal"), factory.getContext()));
		assertEndsWith(normalImage.getUrl(), "en.png");
//		assertTrue(normalImage.isRendered()); cannot be tested in HTMLUnit
		
		DefaultImage brokenImage = new DefaultImage(new WebElementReference("Testimage", null, By.id("image_broken"), factory.getContext()));
		assertEndsWith(brokenImage.getUrl(), "notexisting.jpg");
//		assertFalse(brokenImage.isRendered()); cannot be tested in HTMLUnit
	}
	
	@Test
	public void testDefaultLabel() {
		DefaultLabel normalLabel = new DefaultLabel(new WebElementReference("Testlabel", null, By.id("label_normal"), factory.getContext()));
		assertEquals("some Text", normalLabel.text());

		DefaultLabel emptyLabel = new DefaultLabel(new WebElementReference("Testlabel", null, By.id("label_empty"), factory.getContext()));
		assertEquals("", emptyLabel.text());

		DefaultLabel tagLabel = new DefaultLabel(new WebElementReference("Testlabel", null, By.id("label_tags"), factory.getContext()));
		assertEquals("Text with HTML-Tags", tagLabel.text());
	}
	
	@Test
	public void testDefaultLink() {
		DefaultLink normalLink = new DefaultLink(new WebElementReference("Testlink", null, By.id("link_normal"), factory.getContext()));
		assertEquals("link", normalLink.text());
		assertEndsWith(normalLink.href(), "otherpage.html");
		normalLink.click();
		assertContains(factory.getContext().getDriver().getPageSource(), "!otherPage.html!");
	}
	
	@Test
	public void testDefaultRadioButton() {
		DefaultRadioButton radio1 = new DefaultRadioButton(new WebElementReference("Testradio", null, By.id("radio1"), factory.getContext()));
		DefaultRadioButton radio2 = new DefaultRadioButton(new WebElementReference("Testradio", null, By.id("radio2"), factory.getContext()));
		DefaultRadioButton radio3 = new DefaultRadioButton(new WebElementReference("Testradio", null, By.id("radio3"), factory.getContext()));
		
		assertFalse(radio1.isSelected());
		assertTrue(radio1.isEditable());
		assertFalse(radio2.isSelected());
		assertFalse(radio2.isEditable());
		assertFalse(radio3.isSelected());
		assertTrue(radio3.isEditable());
		
		radio1.select();
		assertTrue(radio1.isSelected());
		assertFalse(radio2.isSelected());
		assertFalse(radio3.isSelected());
		
		try {
			radio2.select();
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
		assertTrue(radio1.isSelected());
		assertFalse(radio2.isSelected());
		assertFalse(radio3.isSelected());
		
		radio3.select();
		assertFalse(radio1.isSelected());
		assertFalse(radio2.isSelected());
		assertTrue(radio3.isSelected());

	}
	
	@Test
	public void testDefaultTextbox() {
		DefaultTextBox normalTextbox = new DefaultTextBox(new WebElementReference("Testtextbox", null, By.id("textbox_normal"), factory.getContext()));
		assertTrue(normalTextbox.isEditable());
		assertEquals("", normalTextbox.getText());
		normalTextbox.type("Hello");
		assertEquals("Hello", normalTextbox.getText());
		normalTextbox.text("world");
		assertEquals("world", normalTextbox.getText());
		normalTextbox.clear();
		assertEquals("", normalTextbox.getText());
		
		DefaultTextBox disabledTextbox = new DefaultTextBox(new WebElementReference("Testtextbox", null, By.id("disabled_textbox"), factory.getContext()));
		try {
			disabledTextbox.type("Hello");
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
		try {
			disabledTextbox.text("Hello");
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
		try {
			disabledTextbox.clear();
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
	}
	
	@Test
	public void testDefaultSelectbox() {
		DefaultSelectBox defaultSelect = new DefaultSelectBox(new WebElementReference("Testselect", null, By.id("normal_select"), factory.getContext()));
		assertEquals("text1", defaultSelect.getSelectedText());
		assertEquals("val1", defaultSelect.getSelectedValue());
		
		defaultSelect.selectByText("text2");
		assertEquals("text2", defaultSelect.getSelectedText());
		assertEquals("val2", defaultSelect.getSelectedValue());
		
		defaultSelect.selectByValue("val1");
		assertEquals("text1", defaultSelect.getSelectedText());
		assertEquals("val1", defaultSelect.getSelectedValue());
		
		try {
			defaultSelect.selectByValue("notexisting");
			fail("interacting with disabled radio button not prohibited");
		} catch (NoSuchElementException nsee) {
			// OK
		}
		assertEquals("text1", defaultSelect.getSelectedText());
		assertEquals("val1", defaultSelect.getSelectedValue());
		
		DefaultSelectBox disabledSelect = new DefaultSelectBox(new WebElementReference("Testselect", null, By.id("disabled_select"), factory.getContext()));
		assertEquals("text1", disabledSelect.getSelectedText());
		assertEquals("val1", disabledSelect.getSelectedValue());
		try {
			disabledSelect.selectByText("text1");
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
		try {
			disabledSelect.selectByValue("val1");
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
	}
	
	@Test
	public void testTextarea() {
		TextArea normalTextbox = new TextArea(new WebElementReference("Testtextarea", null, By.id("textarea_normal"), factory.getContext()));
		assertTrue(normalTextbox.isEditable());
		assertEquals("", normalTextbox.getText());
		normalTextbox.type("Hello");
		assertEquals("Hello", normalTextbox.getText());
		normalTextbox.text("world");
		assertEquals("world", normalTextbox.getText());
		normalTextbox.clear();
		assertEquals("", normalTextbox.getText());
		
		TextArea disabledTextbox = new TextArea(new WebElementReference("Testtextbox", null, By.id("disabled_textarea"), factory.getContext()));
		try {
			disabledTextbox.type("Hello");
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
		try {
			disabledTextbox.text("Hello");
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
		try {
			disabledTextbox.clear();
			fail("interacting with disabled radio button not prohibited");
		} catch (RuntimeException re) {
			// OK
		}
	}
	
	@Test
	public void testTimeouts() {
		DefaultButton normalButton = new DefaultButton(new WebElementReference("Testbutton", null, By.id("button_normal"), factory.getContext()));
		normalButton.getWebelement();
		normalButton.getElementFast();
	}

	
	@Test
	public void testTinyMcsTextbox() {
		// cant test TinyMce because it doesn't run in HTMLUnit
	}
}
