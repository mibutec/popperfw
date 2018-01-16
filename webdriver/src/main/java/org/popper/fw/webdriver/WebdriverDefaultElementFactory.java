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
package org.popper.fw.webdriver;

import java.util.HashSet;
import java.util.Set;

import org.popper.fw.interfaces.IElementFactory;
import org.popper.fw.webdriver.elements.impl.DefaultButton;
import org.popper.fw.webdriver.elements.impl.DefaultCheckbox;
import org.popper.fw.webdriver.elements.impl.DefaultFileupload;
import org.popper.fw.webdriver.elements.impl.DefaultImage;
import org.popper.fw.webdriver.elements.impl.DefaultLabel;
import org.popper.fw.webdriver.elements.impl.DefaultLink;
import org.popper.fw.webdriver.elements.impl.DefaultRadioButton;
import org.popper.fw.webdriver.elements.impl.DefaultSelectBox;
import org.popper.fw.webdriver.elements.impl.DefaultTextBox;

public class WebdriverDefaultElementFactory implements IElementFactory {
	private static final Set<Class<?>> impls = new HashSet<Class<?>>();
	static {
		impls.add(DefaultButton.class);
		impls.add(DefaultCheckbox.class);
		impls.add(DefaultFileupload.class);
		impls.add(DefaultImage.class);
		impls.add(DefaultLabel.class);
		impls.add(DefaultLink.class);
		impls.add(DefaultRadioButton.class);
		impls.add(DefaultSelectBox.class);
		impls.add(DefaultTextBox.class);
	}
	
	@Override
	public Class<?> getImplClassForElement(Class<?> clazz) {
		for (Class<?> defaultImpl : impls) {
			if (clazz.isAssignableFrom(defaultImpl)) {
				return defaultImpl;
			}
		}
		return null;
	}

	public void addImplClassForElement(
			Class<?> targetClass) {
		impls.add(targetClass);
	}
}
