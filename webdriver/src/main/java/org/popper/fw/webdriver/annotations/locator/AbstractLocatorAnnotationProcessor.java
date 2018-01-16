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
package org.popper.fw.webdriver.annotations.locator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.popper.fw.impl.ImplHolder;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.impl.ReturnTypeHandler;
import org.popper.fw.impl.ReturnTypeHandler.ReturnTypeFactory;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.webdriver.WebdriverContext;
import org.popper.fw.webdriver.WebdriverPageObjectHelper;
import org.popper.fw.webdriver.annotations.VerifyBy;
import org.popper.fw.webdriver.elements.impl.WebElementReference;

public abstract class AbstractLocatorAnnotationProcessor<A extends Annotation> implements
		IAnnotationProcessor<A, Object>, ReturnTypeFactory<A, WebElement> {
	
	protected final Logger log = Logger.getLogger(getClass());
	
	protected final WebdriverContext context;
	
	protected final IPoFactory factory;
	
	protected final ReturnTypeHandler<A, WebElement> returnTypeHandler = new ReturnTypeHandler<A, WebElement>(this);

	
	public AbstractLocatorAnnotationProcessor(WebDriver webdriver, WebdriverContext context, IPoFactory factory) {
		this.context = context;
		this.factory = factory;
	}

	@Override
	public Object processAnnotation(A annotation, LocatorContextInformation info, Object lastResult) {
		verifyPage(info.getMethod().getDeclaringClass());
		try {
			if (lastResult != null) {
				if (lastResult instanceof ImplHolder) {
					info.setParent(((ImplHolder) lastResult).getImpl());
				} else {
					throw new RuntimeException("There was a lastResult given to @" + Locator.class.getSimpleName()
							+ " of type " + lastResult.getClass().getName()
							+ ". lastResult for this annotation may only be of type "
							+ ImplHolder.class.getSimpleName() + " or null.");
				}
			}
			return returnTypeHandler.createReturnObject(info, annotation);
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract By getBy(A annotation, LocatorContextInformation info);
	
	@Override
	public List<WebElement> handleCollectionType(A annotation, LocatorContextInformation info) {
		By by = getBy(annotation, info);
		return context.getElements(getName(info.getMethod(), annotation), by, info.getParent());
	}
	
	@Override
	public Object handleCountType(LocatorContextInformation info, A annotation) {
		return context.getElements(getName(info.getMethod(), annotation), getBy(annotation, info), info.getParent()).size();
	}

	protected String getName(Method method, A locator) {
		String[] split = StringUtils.splitByCharacterTypeCamelCase(method.getName());
		return Arrays.stream(split).collect(Collectors.joining(" "));
	}
	
	public Object createObject(A annotation, LocatorContextInformation info, Class<?> type, WebElement webElement) {
		final String locatorName = getName(info.getMethod(), annotation);
		PageObjectImplementation parent = info.getParent();
		By by = getBy(annotation, info);
		if (type == WebElement.class) {
			if (webElement != null) {
				return webElement;
			} else {
				return WebdriverPageObjectHelper.getSearchContext(parent).findElement(by);
			}
		}
		
		Class<?> givenImplClass = context.getImplementingClassFor(info.getMethod(), type);
		if (givenImplClass == null) {
			return context.getFactory().createPo(type, locatorName, by, parent, webElement);
		} else {
			WebElementReference reference = new WebElementReference(locatorName, parent, by, context, webElement);
			Map<Class<?>, Object> resolver = new HashMap<Class<?>, Object>();
			resolver.put(WebElementReference.class, reference);
			resolver.put(By.class, by);
			resolver.put(String.class, locatorName);
			resolver.put(PageObjectImplementation.class, parent);
			return context.instantiateObject(givenImplClass, resolver);
		}
	}

	protected void verifyPage(Class<?> clazz) {
		VerifyBy verifyBy = clazz.getAnnotation(VerifyBy.class);

		if (verifyBy != null && !StringUtils.isEmpty(verifyBy.title())) {
			String title = this.context.getDriver().getTitle();
			if (!title.matches(verifyBy.title())) {
				throw new RuntimeException("could not verify Page by Title, expected: " + verifyBy.title()
						+ ", found: " + title);
			}
		}
	}
}
