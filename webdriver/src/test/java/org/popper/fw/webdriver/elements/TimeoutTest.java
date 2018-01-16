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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.popper.fw.webdriver.elements.impl.DefaultButton;
import org.popper.fw.webdriver.elements.impl.WebElementReference;
import org.popper.inttest.AbstractIntTest;

public class TimeoutTest extends AbstractIntTest {
	private static final int LONG_TIMEOUT = 800;

	@Before
	public void setup() {
		super.setup();
		factory.getContext().getDefaultConfig().setImplicitTimeout(LONG_TIMEOUT);
		factory.getContext().getDefaultConfig().setCheckNotPresentTimeout(0);
		factory.getContext().openRelativeUri("/elements/ElementTest.html");
	}
	
	@Test
	public void testTimeouts() {
		DefaultButton normalButton = new DefaultButton(new WebElementReference("Testbutton", null, By.id("button_notExist"), factory.getContext()));
		long runtime = getRuntime(() -> normalButton.getWebelement());
		Assert.assertTrue("expected runtime to take longer than " + LONG_TIMEOUT + "  ms, but was " + runtime + " ms", runtime >= LONG_TIMEOUT);

		runtime = getRuntime(() -> normalButton.getElementFast());
		Assert.assertTrue("expected runtime to take shorter than 300 ms, but was " + runtime + " ms", runtime < 300);
		
		runtime = getRuntime(() -> normalButton.getWebelement());
		Assert.assertTrue("expected runtime to take longer than " + LONG_TIMEOUT + "  ms, but was " + runtime + " ms", runtime >= LONG_TIMEOUT);
	}

	private long getRuntime(Runnable r) {
		long start = System.currentTimeMillis();
		try {
			r.run();
		} catch (Exception e) {
			// expected;
		}
		return System.currentTimeMillis() - start;
	}
}
