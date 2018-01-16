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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.popper.forge.annotations.Getter.GetterProcessor;
import org.popper.forge.api.IFieldProvidingAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.annotations.ImplementedBy;

/**
 * Annotation to provide a getter method for a created field
 * 
 * @Getter
 * String someProperty()
 * 
 * will create a field named someProperty of type String on the forged object. The method will act as getter  
 * 
 * @author michael_bulla
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(GetterProcessor.class)
public @interface Getter {
	String name() default "";
	
	public static class GetterProcessor implements IFieldProvidingAnnotationProcessor<Getter, Object> {

		@Override
		public Object processAnnotation(Getter annotation, RuntimeContextInformation info, Object lastResult)
				throws ReEvalutateException {
			return getField(info.getTarget(), fieldName(annotation, info.getMethod()));
		}

		@Override
		public Collection<ProvidedField> providedFields(Getter annotation, Method method,
				AnnotatedElement annotatedElement, Class<?> blankClass) {
			return Arrays.asList(new ProvidedField(fieldName(annotation, method), method.getReturnType()));
		}
		
		private String fieldName(Getter annotation, Method method) {
			String name = annotation.name();
			if (name == null || name.isEmpty()) {
				name = method.getName();
			}
			
			return name;
		}

	}
}
