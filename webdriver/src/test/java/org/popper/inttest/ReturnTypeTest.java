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
package org.popper.inttest;

import org.junit.Test;
import org.popper.fw.NoGenericTypeAllowedException;
import org.popper.fw.element.ILabel;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.locator.Locator;
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
import org.popper.testfactory.Superelement;
import org.popper.testpos.GenericPo.CorrectGenerichandling;
import org.popper.testpos.GenericPo.WrongGenerichandling;
import org.popper.testpos.ManyTypesPO;
import org.popper.testpos.ManyTypesWithElementFactoryPO;
import org.popper.testpos.ManyWebTypesPO;

import junit.framework.Assert;

public class ReturnTypeTest extends AbstractIntTest {
	@Test
	public void testDatatype() {
		ManyTypesPO manyTypes = factory.createPage(ManyTypesPO.class);
		manyTypes.open();
		
		Assert.assertEquals(DefaultButton.class, manyTypes.button().getClass());
		Assert.assertEquals(DefaultCheckbox.class, manyTypes.checkbox().getClass());
		Assert.assertEquals(DefaultFileupload.class, manyTypes.fileupload().getClass());
		Assert.assertEquals(DefaultLabel.class, manyTypes.label().getClass());
		Assert.assertEquals(DefaultImage.class, manyTypes.image().getClass());
		Assert.assertEquals(DefaultLink.class, manyTypes.link().getClass());
		Assert.assertEquals(DefaultRadioButton.class, manyTypes.radiobutton().getClass());
		Assert.assertEquals(DefaultSelectBox.class, manyTypes.select().getClass());
		Assert.assertEquals(DefaultTextBox.class, manyTypes.textbox().getClass());
		Assert.assertEquals(TextArea.class, manyTypes.textarea().getClass());
	}

	@Test
	public void testWebDatatype() {
		ManyWebTypesPO manyTypes = factory.createPage(ManyWebTypesPO.class);
		manyTypes.open();
		
		Assert.assertEquals(DefaultButton.class, manyTypes.button().getClass());
		Assert.assertEquals(DefaultCheckbox.class, manyTypes.checkbox().getClass());
		Assert.assertEquals(DefaultFileupload.class, manyTypes.fileupload().getClass());
		Assert.assertEquals(DefaultLabel.class, manyTypes.label().getClass());
		Assert.assertEquals(DefaultImage.class, manyTypes.image().getClass());
		Assert.assertEquals(DefaultLink.class, manyTypes.link().getClass());
		Assert.assertEquals(DefaultRadioButton.class, manyTypes.radiobutton().getClass());
		Assert.assertEquals(DefaultSelectBox.class, manyTypes.select().getClass());
		Assert.assertEquals(DefaultTextBox.class, manyTypes.textbox().getClass());
		Assert.assertEquals(TextArea.class, manyTypes.textarea().getClass());
	}

	@Test
	public void testElementFactory() {
		ManyTypesWithElementFactoryPO manyTypes = factory.createPage(ManyTypesWithElementFactoryPO.class);
		manyTypes.open();
		
		Assert.assertEquals(Superelement.class, manyTypes.button().getClass());
		Assert.assertEquals(Superelement.class, manyTypes.checkbox().getClass());
		Assert.assertEquals(Superelement.class, manyTypes.fileupload().getClass());
		Assert.assertEquals(Superelement.class, manyTypes.label().getClass());
		Assert.assertEquals(Superelement.class, manyTypes.image().getClass());
		Assert.assertEquals(Superelement.class, manyTypes.link().getClass());
		Assert.assertEquals(Superelement.class, manyTypes.radiobutton().getClass());
		Assert.assertEquals(Superelement.class, manyTypes.select().getClass());
		Assert.assertEquals(Superelement.class, manyTypes.textbox().getClass());
		Assert.assertEquals(TextArea.class, manyTypes.textarea().getClass());
	}
	
	@Test
	public void testNonAbstractReturnTypeWithoutTypeAnnotation() {
		ManyTypesPO manyTypes = factory.createPage(ManyTypesPO.class);
		manyTypes.open();
		
		Assert.assertEquals(TextArea.class, manyTypes.textareaNoTypeAnnotation().getClass());
	}
	
	@Test
	public void testGeneircReturnTypeIsProhibited() {
		WrongGenerichandling wrongPo = factory.createPage(WrongGenerichandling.class);
		CorrectGenerichandling correctPo = factory.createPage(CorrectGenerichandling.class);

		try {
			wrongPo.getSomeElement();
			Assert.fail("expected execption because of generic return type");
		} catch (NoGenericTypeAllowedException ngtae) {
			// expected result
		}

		correctPo.getSomeElement();
	}
	
	@Test
	public void testIsDisplayedWorks() {
		NotExistingPo notExistingPo = factory.createPage(NotExistingPo.class);
		Assert.assertFalse(notExistingPo.notExistingLabel().isDisplayed());
	}
	
	@Test
	public void testCanHandleDefaultMethods() {
		DefaultMethodPo defaultMethodPo = factory.createPage(DefaultMethodPo.class);
		Assert.assertEquals(defaultMethodPo.helloWorld(), "Hello world");
	}
	
    @Test
    public void testCreateProxyOnClass() {
        factory.createPage(ClassBasedPo.class);
    }

	@Page
	public static interface NotExistingPo {
		@Locator(id="notExisting")
		ILabel notExistingLabel();
	}
	
	@Page
	public static interface DefaultMethodPo {
		public default String helloWorld() {
			return "Hello world";
		}
	}

    @Page
    public static class ClassBasedPo {

    }
}
