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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.popper.forge.ReevaluationTest.Counting.CountingImpl;
import org.popper.forge.ReevaluationTest.Revaluating.RevaluatingImpl;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReevaluationTest {
	private ClassForge testee = new ClassForge();
	
	@Test
	public void testReevalutate() {
		RevaluatingBlank blank = testee.createInstance(RevaluatingBlank.class);
		assertEquals(Integer.valueOf(3), blank.value());
		assertEquals(Integer.valueOf(4), blank.value());
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@org.popper.forge.api.annotations.ImplementedBy(CountingImpl.class)
	static @interface Counting {
		public static class CountingImpl implements IAnnotationProcessor<Counting, Integer> {
			private static int counter = 0;
			
			@Override
			public Integer processAnnotation(Counting locatorAnnotation, RuntimeContextInformation info,
					Integer lastResult) throws ReEvalutateException {
				return counter++;
			}
		}
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@org.popper.forge.api.annotations.ImplementedBy(RevaluatingImpl.class)
	@interface Revaluating {
		public static class RevaluatingImpl implements IAnnotationProcessor<Revaluating, Integer> {
			@Override
			public Integer processAnnotation(Revaluating annotation, RuntimeContextInformation info,
					Integer lastResult) throws ReEvalutateException {
				if (lastResult < 3) {
					throw new ReEvalutateException();
				}
				
				return lastResult;
			}
		}
	}
	
	private static interface RevaluatingBlank {
		@Counting
		@Revaluating
		public Integer value();
	}
}
