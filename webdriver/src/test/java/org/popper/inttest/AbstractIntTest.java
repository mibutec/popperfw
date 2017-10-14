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
