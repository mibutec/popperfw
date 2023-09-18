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
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.annotations.ImplementedBy;
import org.popper.forge.api.annotations.RunAfter;
import org.popper.forge.api.annotations.RunBefore;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnotationOrderTest {
	private ClassForge testee = new ClassForge();
	
	@Test
	public void testOrder() {
		OrderBlank blank = testee.createInstance(OrderBlank.class);
		assertEquals(Integer.valueOf(3), blank.order1());
		assertEquals(Integer.valueOf(3), blank.order2());
		assertEquals(Integer.valueOf(3), blank.order3());
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	private static @interface Order1 {
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	@RunAfter(Order1.class)
	@RunBefore(Order3.class)
	private static @interface Order2 {
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	private static @interface Order3 {
	}
	
	private static class OrderedImpl implements IAnnotationProcessor<Annotation, Integer> {
		@Override
		public Integer processAnnotation(Annotation annotation, RuntimeContextInformation info,
				Integer lastResult) throws ReEvalutateException {
			Integer value;
			if (annotation instanceof Order1) {
				value = -1;
			} else if (annotation instanceof Order2) {
				value = 1;
			} else {
				value = 2;
			}
			
			if (lastResult == null && value == -1) {
				return 1;
			}
			
			assertEquals(Integer.valueOf(value), lastResult);				
			return ++lastResult;
		}
	}

	private interface OrderBlank {
		@Order1
		@Order2
		@Order3
		public Integer order1();
		
		@Order3
		@Order2
		@Order1
		public Integer order2();
		
		@Order2
		@Order3
		@Order1
		public Integer order3();
	}
}
