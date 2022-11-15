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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyFactory;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.popper.fw.helper.AnnotationFinder;
import org.popper.fw.impl.AbstractPopperContext.ObjectInstantiator;
import org.popper.fw.impl.ImplHolder;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.webdriver.WebdriverPageObjectHelper.WebdriverPoExtension;
import org.popper.fw.webdriver.annotations.Page;

public class WebdriverFactory implements IPoFactory {
	private final WebdriverContext context;

	public WebdriverContext getContext() {
		return context;
	}

	public WebdriverFactory(WebdriverContext context) {
		this.context = context;
	}

	public <T extends Object> T createPo(Class<T> type, String name, By by,
			PageObjectImplementation parent, SearchContext searchContext) {
		PageObjectImplementation poi = WebdriverPageObjectHelper.createPageObjectImplementation(type, by, name, parent, context,
					searchContext);
		
		return createProxy(type, poi);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T createProxy(Class<T> type, PageObjectImplementation poi) {
		// TODO: Now using a mix of Java dynamic proxy and cglib => should use one this both
		if (!type.isInterface()) {
			ProxyFactory factory = new ProxyFactory();
			factory.setSuperclass(type);
			factory.setInterfaces(new Class<?>[] {ImplHolder.class});
			factory.setFilter(new MethodFilter() {
				@Override
				public boolean isHandled(Method method) {
					return Modifier.isAbstract(method.getModifiers());
				}
			});

			try {
				Map<Class<?>, Object> resolver = new HashMap<Class<?>, Object>();
				By searchContextBy = poi.getExtension(WebdriverPoExtension.class).searchContextBy;
				if (searchContextBy != null) {
					resolver.put(By.class, searchContextBy);
				}
				if (poi.getParent() != null && WebdriverPageObjectHelper.getSearchContext(poi.getParent()) != null) {
					resolver.put(SearchContext.class, searchContextBy);
				} else {
					resolver.put(SearchContext.class, context.getDriver());
				}

				ObjectInstantiator instantiator = new ObjectInstantiator();
				return (T) factory.create(instantiator.getConstructorDefinition(type), instantiator.getParameters(type, context.getClassResolver(), resolver), poi);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class<?>[] { type, ImplHolder.class }, poi);
		}

	}


	public <T> T createLegacyPage(Class<T> type, String name, SearchContext searchContext, By by) {
		if (name == null) {
			Page pageAnnotation = type.getAnnotation(Page.class);
			if (pageAnnotation == null) {
				throw new RuntimeException(
						"createPage may only be used for classes annotated with @Page." + type.getName() + " doesn't have. Use createPage(Class<T> type, String name) if you can't provide @Page annotation.");
			}
			name = nameFromPage(pageAnnotation, type);
		}
		
		PageObjectImplementation parent = null;
		if (searchContext != null) {
			parent = WebdriverPageObjectHelper.createPageObjectImplementation(Object.class, null, name + "'s parent", null, context, null);
		}
		return createPo(type, name, by, parent, null);
	}

	public <T> T createLegacyPage(Class<T> type, String name, By by) {
		return createLegacyPage(type, name, null, by);
	}


	public <T> T createLegacyPage(Class<T> type, String name) {
		return createLegacyPage(type, name, null, null);
	}

	@Override
	public <T> T createPage(Class<T> type) {
		Page pageAnnotation = AnnotationFinder.findAnnotation(type, Page.class);;
		if (pageAnnotation == null) {
			throw new RuntimeException(
					"createPage may only be used for classes annotated with @Page." + type.getName() + " doesn't have. Use createPage(Class<T> type, String name) if you can't provide @Page annotation.");
		}
		String name = nameFromPage(pageAnnotation, type);
		
		return createPo(type, name, null, null, null);
	}

	private String nameFromPage(Page page, Class<?> clazz) {
		String ret = page.name();
		if ("".equals(ret)) {
			String[] split = StringUtils.splitByCharacterTypeCamelCase(clazz.getName());
			ret = Arrays.stream(split).collect(Collectors.joining(" "));
		}
		return ret;
	}
}
