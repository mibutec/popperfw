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
package org.popper.forge.api;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * JavaBean providing context information to {@link IAnnotationProcessor}
 * 
 * @author michael_bulla
 *
 */
public class ContextInformation {
	private final Method method;
	
	private final Class<?> basicClass;
	
	private final AnnotatedElement annotationHolder;
	
	private final DependencyResolver dependencyResolver;
	
	public ContextInformation(Method method, AnnotatedElement annotationHolder,
			Class<?> basicClass, DependencyResolver dependencyResolver) {
		this.method = method;
		this.basicClass = basicClass;
		this.annotationHolder = annotationHolder;
		this.dependencyResolver = dependencyResolver;
	}

	public AnnotatedElement getAnnotationHolder() {
		return annotationHolder;
	}

	public Method getMethod() {
		return method;
	}

	public Class<?> getBasicClass() {
		return basicClass;
	}

	public DependencyResolver getDependencyResolver() {
		return dependencyResolver;
	}
}
