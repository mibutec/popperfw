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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

import org.popper.forge.DefaultMethodAnalyzer;
import org.popper.forge.api.annotations.AnalyzedBy;

/**
 * By default a blank class is analyzed by {@link DefaultMethodAnalyzer}. This one looks at the annotations on a method
 * to find the {@link IAnnotationProcessor} to use.
 * 
 * You may want to write an own rules to for example find {@link IAnnotationProcessor} via method names. Such rules can
 * be implemented as a {@link IMethodAnalyzer} and added to a blank via {@link AnalyzedBy}
 * 
 * @author michael_bulla
 *
 */
public interface IMethodAnalyzer {
	/**
	 * Analyzes an abstract method and determines the {@link IAnnotationProcessor}s to use for handling that method
	 * @param method abstract method to analyze
	 * @return all {@link IAnnotationProcessor}s to use for handling in correct order of processing
	 */
    List<AnnotationProcessorTupel> findProcessorsForMethod(Method method);
    
	public static class AnnotationProcessorTupel {
		public final Annotation annotation;
		public final AnnotatedElement annotatedElement;
		public final Class<? extends IAnnotationProcessor<?, ?>> processorToUse;
		
		public AnnotationProcessorTupel(Annotation annotation, AnnotatedElement annotatedElement,
				Class<? extends IAnnotationProcessor<?, ?>> processorToUse) {
			super();
			this.annotation = annotation;
			this.annotatedElement = annotatedElement;
			this.processorToUse = processorToUse;
		}
	}
}
