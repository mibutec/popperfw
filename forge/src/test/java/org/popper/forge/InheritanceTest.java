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

import org.junit.Test;
import org.popper.forge.ClassForge;

import static org.junit.Assert.*;

public class InheritanceTest {
	ClassForge testee = new ClassForge();
	
	@Test
	public void shouldInheritInterfacesToInterface() {
		ExtendedInterface blank = testee.createInstance(ExtendedInterface.class);
		assertEquals("Hello, world", blank.sayHello());
	}
	
	@Test
	public void shouldInheritInterfacesToClass() {
		ClassExtendingInterface blank = testee.createInstance(ClassExtendingInterface.class);
		assertEquals("Hello, world", blank.sayHello());
	}
	
	@Test
	public void shouldInheritClassesToClass() {
		ClassExtendingClass blank = testee.createInstance(ClassExtendingClass.class);
		assertEquals("Hello, world", blank.sayHello());
	}
	
	@Test
	public void shouldInheritAnnotationFromParentMethod() {
		OverridingAnnotatedMethod blank = testee.createInstance(OverridingAnnotatedMethod.class);
		assertEquals("Hello, world", blank.sayHello());
	}
	
	private static interface BaseInterface {
		@HelloWorldAnnotation
		String sayHello();
	}
	
	private static abstract class BaseClass {
		@HelloWorldAnnotation
		public abstract String sayHello();
	}
	
	private static interface ExtendedInterface extends BaseInterface {
		
	}
	
	private static abstract class ClassExtendingInterface implements BaseInterface {
		@SuppressWarnings("unused")
		ClassExtendingInterface() {
			
		}
	}
	
	private static abstract class ClassExtendingClass extends BaseClass {
		@SuppressWarnings("unused")
		ClassExtendingClass() {
			
		}
		
	}
	
	private static interface OverridingAnnotatedMethod extends BaseInterface {
		@Override
		String sayHello();
	}
}
