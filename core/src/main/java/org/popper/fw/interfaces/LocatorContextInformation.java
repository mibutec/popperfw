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
package org.popper.fw.interfaces;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.popper.fw.impl.AbstractPopperContext;
import org.popper.fw.impl.PageObjectImplementation;

/**
 * JavaBean providing runtime informatino to {@link IAnnotationProcessor}
 * 
 * @author michael_bulla
 *
 */
public class LocatorContextInformation {
	private PageObjectImplementation parent;

	private final Method method;
	
	private final Object[] parameters;
	
	private final Class<?> basicClass;
	
	private final AbstractPopperContext popperContext;
	
	private final AnnotatedElement annotationHolder;
	
	public LocatorContextInformation(PageObjectImplementation parent, Method method, AnnotatedElement annotationHolder,
			Object[] parameters, Class<?> basicClass, AbstractPopperContext popperContext) {
		super();
		this.parent = parent;
		this.method = method;
		this.parameters = parameters;
		this.basicClass = basicClass;
		this.popperContext = popperContext;
		this.annotationHolder = annotationHolder;
	}

	public AnnotatedElement getAnnotationHolder() {
		return annotationHolder;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public Class<?> getBasicClass() {
		return basicClass;
	}

	public PageObjectImplementation getParent() {
		return parent;
	}
	
	public AbstractPopperContext getPopperContext() {
		return popperContext;
	}

	public void setParent(PageObjectImplementation parent) {
		this.parent = parent;
	}
}
