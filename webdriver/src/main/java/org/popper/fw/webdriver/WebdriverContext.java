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

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.popper.fw.impl.AbstractPopperContext;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.interfaces.IClassResolver;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.webdriver.WebdriverPageObjectHelper.WebdriverPoExtension;

/**
 * This class holds the global WebDriver instance for reuse between different
 * adapters and different tables on one FitNesse page. The default values point
 * to the GVS system test stage
 * 
 * @author Michael Bulla
 */
public class WebdriverContext extends AbstractPopperContext implements IClassResolver {
	/*
	 * a logger instance for this class
	 */
	private static final transient Logger logger = Logger
			.getLogger(WebdriverContext.class);
	
	private IWebdriverConfig config = new DefaultWebdriverConfig();
	
	public WebdriverContext() {
		setDefaultElementFactory(new WebdriverDefaultElementFactory());
		setClassResolver(this);
	}

	/**
	 * The used webDriver instance
	 */
	private ThreadLocal<WebDriver> drivers = new ThreadLocal<WebDriver>();
	
	@Override
	public <T> T resolveStaticClass(Class<T> clazz) {
		if (AbstractPopperContext.class.isAssignableFrom(clazz)) {
			return clazz.cast(this);
		} else if (IPoFactory.class.isAssignableFrom(clazz)) {
			return clazz.cast(getFactory());
		} else if (WebDriver.class.isAssignableFrom(clazz)) {
			return clazz.cast(getDriver());
		} else {
			return null;
		}
	}
	
	public void openRelativeUri(String uri) {
		String baseUrl = config.getBaseUrl();
		if (baseUrl == null) {
			throw new RuntimeException("baseUrl must be set to call openUrl");
		}
		if (!config.getBaseUrl().endsWith("/")) {
			baseUrl = baseUrl += "/";
		}
		
		if (uri.startsWith("/")) {
			uri = uri.substring(1);
		}
		
		String newUrl = baseUrl + uri;
		logger.debug("opening url: " + newUrl);

		getDriver().get(newUrl);
	}
	
	public static WebdriverFactory getLocalFileFactory(String baseResourcePath) {
		WebdriverContext context = new WebdriverContext();
		context.getDefaultConfig().setBrowser(Browser.HTML_UNIT);
		context.getDefaultConfig().setBaseUrl("file://" + WebdriverContext.class.getResource(baseResourcePath).getFile());
		return context.getFactory();
	}

	public Browser getBrowser() {
		return config.getBrowser();
	}
	
	public WebElement getElement(String name, By by, PageObjectImplementation parent) {
		SearchContext searchContext = getDriver();
		if (parent != null) {
			searchContext = WebdriverPageObjectHelper.getSearchContext(parent);
		}
		
		logger.debug("trying to get element: " + by + " in " + searchContext);
		try {
			return searchContext.findElement(by);
		} catch (NoSuchElementException e) {
			String message = name + " (" + by + ")";
			while (parent != null) {
				String messagePart = parent.getName();
				By searchContextBy = parent.getExtension(WebdriverPoExtension.class).searchContextBy;
				if (searchContextBy != null) {
					messagePart += " (" + searchContextBy + ")";
				}
				message = messagePart + " -> " + message;
				parent = parent.getParent();
			}

			throw new RuntimeException("Object not found: " + message);
		}
	}
	
	public List<WebElement> getElements(String name, By by, PageObjectImplementation parent) {
		SearchContext searchContext = getDriver();
		if (parent != null) {
			searchContext = WebdriverPageObjectHelper.getSearchContext(parent);
		}
		
		logger.debug("trying to get elements: " + by + " in " + searchContext);
		return searchContext.findElements(by);
	}
	
	public WebDriver getDriver() {
		if (drivers.get() == null) {
			drivers.set(config.createDriver());
		}
		return drivers.get();
	}
	
	@Override
	public WebdriverFactory getFactory() {
		return new WebdriverFactory(this);
	}
	
	public void closeDriver() {
		if (drivers.get() != null) {
			logger.info("closing driver");
			drivers.get().close();
		}
	}
	
	public void resetDriver() {
		logger.info("resetting driver");
		drivers.remove();
	}
	
	public IWebdriverConfig getConfig() {
		return config;
	}
	
	public DefaultWebdriverConfig getDefaultConfig() {
		if (config instanceof DefaultWebdriverConfig) {
			return (DefaultWebdriverConfig) config;
		}
		
		throw new RuntimeException("cant use default-config: its overwritten by another config of type " + config.getClass().getName());
	}

	public void setConfig(IWebdriverConfig config) {
		this.config = config;
	}
}
