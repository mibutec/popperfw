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
package org.popper.forge;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.popper.forge.api.DependencyResolver;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.IMethodAnalyzer.AnnotationProcessorTupel;

/**
 * Manager invoking {@link IAnnotationProcessor}s for an abstract method
 * 
 * @author michael_bulla
 *
 */
public class InvokationHandler {
	private final Map<Method, List<AnnotationProcessorTupel>> analyzedMethods = Collections.synchronizedMap(new HashMap<>());
	
	private final DependencyResolver dependencyResolver;
	
	public InvokationHandler(DependencyResolver dependencyResolver) {
		this.dependencyResolver = dependencyResolver;
	}
	
    public Object handleMethodCall(Object target, Class<?> blankClass, Method method, Object[] args) {
    	List<AnnotationProcessorTupel> processors = analyzedMethods.get(method);
    	if (processors == null) {
    		throw new IllegalStateException(method + " has not been initialized, can't call");
    	}
    	return executeMethod(target, blankClass, method, args, processors);
    }
    
    public void init(Method m, List<AnnotationProcessorTupel> tupels) {
    	analyzedMethods.put(m, tupels);
    }
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object executeMethod(Object target, Class<?> blankClass, Method method, Object[] args, List<AnnotationProcessorTupel> processorClasses) {
		try {

			Object result = null;
			boolean evaluate = true;
			while (evaluate) {
				evaluate = false;
				try {
					for (AnnotationProcessorTupel annotationProcessorTupel : processorClasses) {
						RuntimeContextInformation info = new RuntimeContextInformation(target,
								method, annotationProcessorTupel.annotatedElement, args, blankClass, dependencyResolver);
		
						IAnnotationProcessor annotationProcessor = dependencyResolver.resolveDependency(annotationProcessorTupel.processorToUse);
						result = annotationProcessor.processAnnotation(annotationProcessorTupel.annotation, info, result);
					}
				} catch (ReEvalutateException ex) {
					evaluate = true;
				}
			}
			
			return result;
		} catch (SecurityException | IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
	}
}

