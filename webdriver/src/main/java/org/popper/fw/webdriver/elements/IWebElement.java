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

package org.popper.fw.webdriver.elements;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.popper.fw.element.IElement;

/**
 * Main-Interface of all Webelements containing methods only useful when in webcontext.
 * @author Michael
 *
 */
public interface IWebElement extends IElement {
	/**
	 * Works like {@link WebElement#getTagName()}
	 */
	String getTagName();
	
	/**
	 * Works like {@link WebElement#getAttribute(String)}
	 */
	String getAttribute(String name);

	/**
	 * Works like {@link WebElement#isEnabled()}
	 */
	boolean isEnabled();
	
	/**
	 * Works like {@link WebElement#getText()}
	 */
	String getInnerText();
	
	/**
	 * Works like {@link WebElement#getLocation()}
	 */
	Point getLocation();

	/**
	 * Works like {@link WebElement#getSize()}
	 */
	Dimension getSize();

	/**
	 * Works like {@link WebElement#getCssValue(String)}
	 */
	String getCssValue(String propertyName);
	
	/**
	 * returns WebDriver's {@link WebElement} this Element is based on
	 */
	WebElement getWebelement();
}