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
 * JavaBean providing runtime information to {@link IAnnotationProcessor}
 * 
 * @author michael_bulla
 *
 */
public class RuntimeContextInformation extends ContextInformation {
	private final Object target;
	
	private final Object[] parameters;
	
	public RuntimeContextInformation(Object target, Method method, AnnotatedElement annotationHolder,
			Object[] parameters, Class<?> basicClass, DependencyResolver dependencyResolver) {
		super(method, annotationHolder, basicClass, dependencyResolver);
		this.target = target;
		this.parameters = parameters;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public Object getTarget() {
		return target;
	}
}
