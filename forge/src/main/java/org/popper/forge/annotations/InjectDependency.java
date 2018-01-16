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

package org.popper.forge.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.popper.forge.annotations.InjectDependency.InjectDependencyProcessor;
import org.popper.forge.api.DependencyResolver;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.annotations.ImplementedBy;

/**
 * Annotation providing a an object from {@link DependencyResolver}
 * 
 * @InjectDependency
 * SomeDependency someDependency();
 * 
 * will resolve SomeDependency.class by {@link DependencyResolver} and return the result
 * 
 * @author michael_bulla
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(InjectDependencyProcessor.class)
public @interface InjectDependency {
	public Class<?> type() default Object.class;
	
	public static class InjectDependencyProcessor implements IAnnotationProcessor<InjectDependency, Object> {

		@Override
		public Object processAnnotation(InjectDependency annotation, RuntimeContextInformation info,
				Object lastResult) throws ReEvalutateException {
			Class<?> classToResolve = annotation.type();
			if (classToResolve == Object.class) {
				classToResolve = info.getMethod().getReturnType();
			}
			return info.getDependencyResolver().resolveDependency(classToResolve);
		}
	}
}
