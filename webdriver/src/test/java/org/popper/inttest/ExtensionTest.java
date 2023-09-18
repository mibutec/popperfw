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
package org.popper.inttest;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.annotations.RunAfter;
import org.popper.fw.annotations.RunBefore;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.interfaces.ReEvalutateException;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.inttest.ExtensionTest.Counting.CountingImpl;
import org.popper.inttest.ExtensionTest.Revaluating.RevaluatingImpl;
import org.popper.testpos.SelfImplementedLocator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtensionTest extends AbstractIntTest {
	@Test
	public void testOwnLocator() {
		SelfImplementedLocator selfImplementedLocator = factory.createPage(SelfImplementedLocator.class);
		selfImplementedLocator.open();
		
		assertEquals("first idLocator", selfImplementedLocator.xxx().text());
		assertEquals("second idLocator", selfImplementedLocator.xxxWithParam("1").text());
	}
	
	@Test
	public void testReevalutate() {
		RevaluatingPO po = factory.createPage(RevaluatingPO.class);
		assertEquals(Integer.valueOf(3), po.value());
		assertEquals(Integer.valueOf(4), po.value());
	}
	
	@Test
	public void testOrder() {
		OrderPO po = factory.createPage(OrderPO.class);
		assertEquals(Integer.valueOf(3), po.order1());
		assertEquals(Integer.valueOf(3), po.order2());
		assertEquals(Integer.valueOf(3), po.order3());
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(CountingImpl.class)
	public static @interface Counting {
		public static class CountingImpl implements IAnnotationProcessor<Counting, Integer> {
			private static int counter = 0;
			
			@Override
			public Integer processAnnotation(Counting locatorAnnotation, LocatorContextInformation info,
					Integer lastResult) throws ReEvalutateException {
				return counter++;
			}
		}
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(RevaluatingImpl.class)
	public static @interface Revaluating {
		public static class RevaluatingImpl implements IAnnotationProcessor<Revaluating, Integer> {
			@Override
			public Integer processAnnotation(Revaluating annotation, LocatorContextInformation info,
					Integer lastResult) throws ReEvalutateException {
				if (lastResult < 3) {
					throw new ReEvalutateException();
				}
				
				return lastResult;
			}
		}
	}
	
	@Page
	public interface RevaluatingPO {
		@Counting
		@Revaluating
		public Integer value();
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	public static @interface Order1 {
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	@RunAfter(Order1.class)
	@RunBefore(Order3.class)
	public static @interface Order2 {
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@ImplementedBy(OrderedImpl.class)
	public static @interface Order3 {
	}
	
	public static class OrderedImpl implements IAnnotationProcessor<Annotation, Integer> {
		@Override
		public Integer processAnnotation(Annotation annotation, LocatorContextInformation info,
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

	@Page
	public interface OrderPO {
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
