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

import junit.framework.Assert;

import org.junit.Before;
import org.popper.fw.webdriver.WebdriverContext;
import org.popper.fw.webdriver.WebdriverFactory;

public abstract class AbstractIntTest {
	protected WebdriverFactory factory;
	
	@Before
	public void setup() {
		factory = WebdriverContext.getLocalFileFactory("/");
	}
	
	protected void assertEndsWith(String full, String ending) {
		if (!full.endsWith(ending)) {
			Assert.fail("Expected " + full + " to end with " + ending + ". But it doesn't");
		}
	}
	
	protected void assertContains(String full, String pin) {
		if (!full.contains(pin)) {
			Assert.fail("Expected " + full + " to contain " + pin + ". But it doesn't");
		}
	}

}
