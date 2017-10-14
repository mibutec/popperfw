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


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.interactions.Actions;
import org.popper.fw.webdriver.WebdriverContext;
import org.popper.fw.webdriver.elements.IWebElement;

/**
 * Abstract Superclass for all Elements on a Website that can be interacted with. Interaction may mean
 * readonly actions (see ILabel) or read-write action (see AbstractInput).
 * 
 * This class is responsible to handle location of elements and handle errormessages (with help of the 
 * getElement-method). It provides some convenience-methods for child-classes like flahsing a webelement or a sleep without
 * checked Exceptions.
 * 
 * When implementing a new WebElement, it should be a subclass of AbstractWebElement.
 * 
 * WebElements may have no children. Children are only allowed for PageObjects.
 * 
 * @author Michael
 *
 */
public abstract class AbstractWebElement implements IWebElement {
	
	private final WebElementReference reference;
	
	private static final String FLASH_COLOR = "#66FF33";

	protected AbstractWebElement(WebElementReference reference) {
		this.reference = reference;
	}
	
	protected WebdriverContext getContext() {
		return reference.getContext();
	}
	
	protected WebDriver getDriver() {
		return getContext().getDriver();
	}
	
	protected WebElement getElement() {
		return reference.getElement();
	}
	
	public WebElement getElementFast() {
		Timeouts timeouts = reference.getContext().getDriver().manage().timeouts();
		try {
			timeouts.implicitlyWait(reference.getContext().getConfig().checkNotPresentTimeout(), TimeUnit.MILLISECONDS);
			return getElement();
		} finally {
			timeouts.implicitlyWait(reference.getContext().getConfig().implicitTimeout(), TimeUnit.MILLISECONDS);
		}
	}
	
	/**
	 * Convenience-method that implements a click-method, that can be used for mehtods, where the "normal" click
	 * doesn't work. In many cases that kind of clicking can help.
	 * 
	 */
	protected void clickWorkaround() {
		Actions builder = new Actions(getDriver());
		builder.moveToElement(getElement()).perform();
		builder.click().perform();
	}
	
	/**
	 * Convenience-method to flash this webElement for a given time. This method should only be used
	 * for development-reasons
	 * @param timeInMillis time to flash the element. The test is blocked and won't continue for the
	 * duration of flashing
	 * 
	 */
	public void flash(long timeInMillis) {
		String bgcolor  = getElement().getCssValue("backgroundColor");
		changeColor(FLASH_COLOR);
		sleep(timeInMillis);
		changeColor(bgcolor);
	}
	
	/**
	 * Changes the color of this webelement by using javascript
	 * @param color the new color as CSS-color (like '#66FF33')
	 */
	private void changeColor(String color) {
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.backgroundColor = '" + color + "'",  getElement());
	}
	
	/**
	 * Convenience-method
	 * @param timeInMillis time to sleep in Milliseconds
	 */
	protected void sleep(long timeInMillis) {
		try {
			Thread.sleep(timeInMillis);
		}  catch (InterruptedException e) {
			// nothing to do
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#isDisplayed()
	 */
	@Override
	public boolean isDisplayed() {
		try {
			WebElement ele = getElementFast();
			return ele.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#getTagName()
	 */
	@Override
	public String getTagName() {
		return getElement().getTagName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute(String name) {
		return getElement().getAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return getElement().isSelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return getElement().isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#getInnerText()
	 */
	@Override
	public String getInnerText() {
		return getElement().getText();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#getLocation()
	 */
	@Override
	public Point getLocation() {
		return getElement().getLocation();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#getSize()
	 */
	@Override
	public Dimension getSize() {
		return getElement().getSize();
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#getCssValue(java.lang.String)
	 */
	@Override
	public String getCssValue(String propertyName) {
		return getElement().getCssValue(propertyName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.webdriver.elements.IWebElement#getWebelement()
	 */
	@Override
	public WebElement getWebelement() {
		return getElement();
	}
}
