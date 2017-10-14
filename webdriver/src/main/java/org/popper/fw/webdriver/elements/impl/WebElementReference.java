package org.popper.fw.webdriver.elements.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.webdriver.WebdriverContext;

public class WebElementReference {
	private final WebdriverContext context;
	
	/**
	 * Name of that element for logging and error-handling reasons
	 */
	private final String name;
	
	/**
	 * parent of this element
	 */
	private final PageObjectImplementation parent;
	
	/**
	 * Locator of this element
	 */
	private final By by;
	
	/**
	 * To prohibit calling context.getElement() everytime the element is needed, the element can be
	 * cached here for further use
	 */
	private WebElement cachedElement;
	
	public WebElementReference(String name, PageObjectImplementation parent, By by, WebdriverContext context, WebElement cachedElement) {
		this.name = name;
		this.parent = parent;
		this.by = by;
		this.context = context;
		this.cachedElement = cachedElement;
	}

	public WebElementReference(String name, PageObjectImplementation parent, By by, WebdriverContext context) {
		this(name, parent, by, context, null);
	}

	public WebdriverContext getContext() {
		return context;
	}

	public String getName() {
		return name;
	}

	public PageObjectImplementation getParent() {
		return parent;
	}
	
	/**
	 * Get the WebDriver-WebElement, that represents this Element. 
	 * @return WebElement
	 */
	public WebElement getElement() {
		if (cachedElement == null) {
			cachedElement = context.getElement(name, by, parent);
		}
		
		return cachedElement;
	}
}
