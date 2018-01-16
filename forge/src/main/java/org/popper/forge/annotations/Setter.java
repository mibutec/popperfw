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

import org.popper.forge.annotations.Setter.SetterProcessor;
import org.popper.forge.api.IFieldProvidingAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.annotations.ImplementedBy;

/**
 * Annotation to provide a setter method for a created field
 * 
 * @Setter
 * void someProperty(String someProperty)
 * 
 * will create a field named someProperty of type String on the forged object. The method will act as setter
 * 
 * For builder style getters you may use the blank name as return type. This will return the this-instance of the forges object
 * 
 * @author michael_bulla
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ImplementedBy(SetterProcessor.class)
@Documented
public @interface Setter {
	String name() default "";
	
	public static class SetterProcessor implements IFieldProvidingAnnotationProcessor<Setter, Object> {

		@Override
		public Object processAnnotation(Setter annotation, RuntimeContextInformation info, Object lastResult)
				throws ReEvalutateException {
			setField(info.getTarget(), fieldName(annotation, info.getMethod()), info.getParameters()[0]);
			return info.getTarget();
		}

		@Override
		public Collection<ProvidedField> providedFields(Setter annotation, Method method,
				AnnotatedElement annotatedElement, Class<?> blankClass) {
			return Arrays.asList(new ProvidedField(fieldName(annotation, method), method.getParameterTypes()[0]));
		}
		
		private String fieldName(Setter annotation, Method method) {
			String name = annotation.name();
			if (name == null || name.isEmpty()) {
				name = method.getName();
			}
			
			return name;
		}
	}
}
