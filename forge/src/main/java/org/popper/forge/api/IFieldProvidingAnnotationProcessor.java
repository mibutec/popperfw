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
import java.util.Collection;
import java.util.Objects;

/**
 * Extension to {@link IAnnotationProcessor} allowing a processor to define fields to be added to forged classes
 * 
 * @author michael_bulla
 *
 */
public interface IFieldProvidingAnnotationProcessor<A extends Annotation, R> extends IAnnotationProcessor<A, R> {
	Collection<ProvidedField> providedFields(A annotation, Method method, AnnotatedElement annotatedElement, Class<?> blankClass);
	
	@SuppressWarnings("unchecked")
	default <T> T getField(Object target, String name) {
		try {
			return (T) target.getClass().getField(name).get(target);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	default void setField(Object target, String name, Object value) {
		try {
			target.getClass().getField(name).set(target, value);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static class ProvidedField {
		public final String name;
		public final Class<?> type;

		public ProvidedField(String name, Class<?> type) {
			super();
			this.name = name;
			this.type = type;
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, type);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !obj.getClass().equals(getClass())) {
				return false;
			}

			ProvidedField other = (ProvidedField) obj;

			return Objects.equals(name, other.name) && Objects.equals(type,  other.type);
		}
	}
}
