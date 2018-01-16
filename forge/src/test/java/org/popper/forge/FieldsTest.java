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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.popper.forge.FieldsTest.LastValueProvider.LastValueProviderImpl;
import org.popper.forge.annotations.Getter;
import org.popper.forge.annotations.Setter;
import org.popper.forge.api.IFieldProvidingAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;

public class FieldsTest {
	private ClassForge testee = new ClassForge();
	
	@Test
	public void shouldCreateFields() throws Exception {
		BlankProvidingFields blank = testee.createInstance(BlankProvidingFields.class);
		assertEquals(boolean.class, blank.getClass().getDeclaredField("someBoolean").getType());
		assertEquals(Object.class, blank.getClass().getDeclaredField("someObject").getType());
		assertEquals(String.class, blank.getClass().getDeclaredField("someString").getType());
		assertNull(blank.getClass().getDeclaredField("someObject").get(blank));
		
		assertNull(blank.lastValue("42"));
		assertEquals("42", blank.getClass().getDeclaredField("someObject").get(blank));
		assertEquals("42", blank.lastValue("43"));
	}
	
	@Test
	public void shouldCreateGetterSetters() throws Exception {
		GetterSetterBlank blank = testee.createInstance(GetterSetterBlank.class).string("someString").integer(42);
		assertEquals("someString", blank.string());
		assertEquals(42, blank.integer().intValue());
		assertEquals(String.class, blank.getClass().getDeclaredField("string").getType());
		assertEquals(Integer.class, blank.getClass().getDeclaredField("__integer").getType());
		
	}
	
	private static interface GetterSetterBlank {
		@Getter
		String string();
		
		@Setter
		GetterSetterBlank string(String string);
		
		@Getter(name="__integer")
		Integer integer();
		
		@Setter(name="__integer")
		GetterSetterBlank integer(Integer integer);

	}
	
	private static interface BlankProvidingFields {
		@LastValueProvider
		String lastValue(String str);
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@org.popper.forge.api.annotations.ImplementedBy(LastValueProviderImpl.class)
	static @interface LastValueProvider {
		static class LastValueProviderImpl implements IFieldProvidingAnnotationProcessor<LastValueProvider, Object> {
			@Override
			public Object processAnnotation(LastValueProvider locatorAnnotation, RuntimeContextInformation info,
				Object lastResult) throws ReEvalutateException {
				Object ret = getField(info.getTarget(), "someObject");
				setField(info.getTarget(), "someObject", info.getParameters()[0]);
				return ret;
			}

			@Override
			public Collection<ProvidedField> providedFields(LastValueProvider annotation, Method method,
					AnnotatedElement annotatedElement, Class<?> blankClass) {
				return Arrays.asList(new ProvidedField("someString", String.class), new ProvidedField("someBoolean", boolean.class), new ProvidedField("someObject", Object.class));
			}
		}
	}
}
