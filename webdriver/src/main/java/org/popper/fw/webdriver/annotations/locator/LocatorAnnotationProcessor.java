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
package org.popper.fw.webdriver.annotations.locator;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.webdriver.WebdriverContext;

/**
 * {@link IAnnotationProcessor} processing {@link Locator}-annotation
 * @author Michael
 *
 */
public class LocatorAnnotationProcessor extends AbstractLocatorAnnotationProcessor<Locator> {

	public LocatorAnnotationProcessor(WebDriver webdriver, WebdriverContext context, IPoFactory factory) {
		super(webdriver, context, factory);
	}

	@Override
	protected By getBy(Locator annotation, LocatorContextInformation info) {
		return createBy(annotation.cssSelector(), annotation.xpath(), annotation.id(), info.getParameters());
	}

	@Override
	protected String getName(Method method, Locator locator) {
		String ret = locator.name();
		if ("".equals(ret)) {
			ret = super.getName(method, locator);
		}
		return ret;
	}

	public static By createBy(String cssSelector, String xpath, String id, Object[] args) {
		if (!StringUtils.isEmpty(id)) {
			return By.id(WebdriverContext.replaceVariables(id, args));
		} else if (!StringUtils.isEmpty(cssSelector)) {
			return By.cssSelector(WebdriverContext.replaceVariables(cssSelector, args));
		} else {
			return By.xpath(WebdriverContext.replaceVariables(xpath, args));
		}
	}
}
