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

import org.junit.jupiter.api.Test;
import org.popper.forge.ClassForgeTest.SomeAnnotation.SomeAnnotationImplWithWrongGeneric;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ClassForgeTest {
	private ClassForge testee = new ClassForge();
	
	@Test
	public void shouldCreateObjectOutOfInterface() {
		SimpleBlankInterface blank = testee.createInstance(SimpleBlankInterface.class);
		assertEquals("Hello, world", blank.sayHello());
	}
	
	@Test
	public void shouldCreateObjectOutOfClass() {
		SimpleBlankClass blank = testee.createInstance(SimpleBlankClass.class);
		assertEquals("Hello, world", blank.sayHello());
	}
	
	@Test
	public void shouldCallConcreteMethods() {
		SimpleBlankClass blank = testee.createInstance(SimpleBlankClass.class);
		assertEquals("Bye, cruel world", blank.sayBye());
	}
	
	@Test
	public void shouldCallDefaultMethods() {
		SimpleBlankInterface blank = testee.createInstance(SimpleBlankInterface.class);
		assertEquals("Bye, cruel world", blank.sayBye());
	}
	
	@Test
	public void shouldFailOnWrongReturnTypeForAnnotation() {
		assertThrows(IllegalStateException.class, ()->{
			testee.createInstance(BlankUsingWrongReturnType.class);
		});
	}
	
	@Test
	public void shouldCreateInstanceMultipleTimes() {
		SimpleBlankInterface blank = testee.createInstance(SimpleBlankInterface.class);
		assertEquals("Hello, world", blank.sayHello());
		blank = testee.createInstance(SimpleBlankInterface.class);
		assertEquals("Hello, world", blank.sayHello());
	}
	
	@Test
	public void shouldProvideConfigurableClassNames() {
		testee = new ClassForge(new ClassForgeConfig().setClassNameProvider((c, cl) -> c.getName() + "GeilerMacker"));
		SimpleBlankInterface blank = testee.createInstance(SimpleBlankInterface.class);
		assertEquals(SimpleBlankInterface.class.getName() + "GeilerMacker", blank.getClass().getName());
	}
	
	@Test
	public void shouldProvideMeaningfulErrorMessageWhenUsingWrongAnnotationTypeInProcessor() {
		assertThrows(IllegalStateException.class, ()->{
			BlankForcingWrongAnnotationTypeInProcessor blank = testee.createInstance(BlankForcingWrongAnnotationTypeInProcessor.class);
			blank.sayHello();
		});
	}
	
	@Test
	public void shouldAddFurtherInterfaceToClass() {
		SomePublicInterface blank = testee.createInstance(SomePublicInterface.class, AnotherInterface.class);
		assertTrue(blank instanceof AnotherInterface);
		assertEquals("Hello, world", ((AnotherInterface) blank).anotherHello());
	}
	
	public static interface AnotherInterface {
		@HelloWorldAnnotation
		String anotherHello();
	}
	
	public static interface SomePublicInterface {
		@HelloWorldAnnotation
		String sayHello();
	}
	
	private static interface BlankUsingWrongReturnType {
		@HelloWorldAnnotation
		Integer sayHello();
	}
	
	private static interface SimpleBlankInterface {
		default String sayBye() {
			return "Bye, cruel world";
		}
		
		@HelloWorldAnnotation
		String sayHello();
	}

	private static abstract class SimpleBlankClass {
		@SuppressWarnings("unused")
		SimpleBlankClass() {
			
		}
		
		public String sayBye() {
			return "Bye, cruel world";
		}
		
		@HelloWorldAnnotation
		public abstract String sayHello();
	}
	
	
	private static interface BlankForcingWrongAnnotationTypeInProcessor {
		@SomeAnnotation
		String sayHello();
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@org.popper.forge.api.annotations.ImplementedBy(SomeAnnotationImplWithWrongGeneric.class)
	static @interface SomeAnnotation {
		public static class SomeAnnotationImplWithWrongGeneric implements IAnnotationProcessor<HelloWorldAnnotation, Integer> {
			private static int counter = 0;
			
			@Override
			public Integer processAnnotation(HelloWorldAnnotation locatorAnnotation, RuntimeContextInformation info,
					Integer lastResult) throws ReEvalutateException {
				return counter++;
			}
		}
	}
}
