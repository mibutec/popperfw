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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;
import org.popper.forge.TypesTest.NoOp.NoOpProcessor;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.annotations.AnalyzedBy;

public class TypesTest {
	private ClassForge testee = new ClassForge();
	
	@Test
	public void shouldHandlePrimitiveTypes() {
		BlankUsingAlotTypes blank = testee.createInstance(BlankUsingAlotTypes.class);
		assertEquals(true, blank.booleanMethod(true));
		assertEquals((byte) 1, blank.byteMethod((byte) 1));
		assertEquals((char) 1, blank.charMethod((char) 1));
		assertEquals((short) 1, blank.shortMethod((short) 1));
		assertEquals(1, blank.intMethod(1));
		assertEquals(1L, blank.longMethod(1L));
		assertEquals((float) 1, blank.floatMethod((float) 1), 0);
		assertEquals((double) 1, blank.doubleMethod((double) 1), 0);
	}
	
	@Test
	public void shouldHandleArrayTypes() {
		BlankUsingAlotArrayTypes blank = testee.createInstance(BlankUsingAlotArrayTypes.class);
		assertArrayEquals(new boolean[0], blank.booleanMethod(new boolean[0]));
		assertArrayEquals(new byte[0], blank.byteMethod(new byte[0]));
		assertArrayEquals(new char[0], blank.charMethod(new char[0]));
		assertArrayEquals(new short[0], blank.shortMethod(new short[0]));
		assertArrayEquals(new int[0], blank.intMethod(new int[0]));
		assertArrayEquals(new long[0], blank.longMethod(new long[0]));
		assertArrayEquals(new float[0], blank.floatMethod(new float[0]), 0);
		assertArrayEquals(new double[0], blank.doubleMethod(new double[0]), 0);
	}
	
	@AnalyzedBy(DefaultMethodAnalyzer.class)
	private static interface BlankUsingAlotTypes {
		@NoOp
		boolean booleanMethod(boolean s);
		
		@NoOp
		byte byteMethod(byte s);
		
		@NoOp
		char charMethod(char s);
		
		@NoOp
		short shortMethod(short s);
		
		@NoOp
		int intMethod(int s);
		
		@NoOp
		long longMethod(long s);
		
		@NoOp
		float floatMethod(float s);
		
		@NoOp
		double doubleMethod(double s);
		
		@NoOp
		void voidMethod();
	}
	
	@AnalyzedBy(DefaultMethodAnalyzer.class)
	private static interface BlankUsingAlotArrayTypes {
		@NoOp
		boolean[] booleanMethod(boolean[] s);
		
		@NoOp
		byte[] byteMethod(byte[] s);
		
		@NoOp
		char[] charMethod(char[] s);
		
		@NoOp
		short[] shortMethod(short[] s);
		
		@NoOp
		int[] intMethod(int[] s);
		
		@NoOp
		long[] longMethod(long[] s);
		
		@NoOp
		float[] floatMethod(float[] s);
		
		@NoOp
		double[] doubleMethod(double[] s);
		
		@NoOp
		Object[] voidMethod(Object[] s);
	}
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@org.popper.forge.api.annotations.ImplementedBy(NoOpProcessor.class)
	public static @interface NoOp {
		public static class NoOpProcessor implements IAnnotationProcessor<NoOp, Object> {

			@Override
			public Object processAnnotation(NoOp locatorAnnotation, RuntimeContextInformation info, Object lastResult)
					throws ReEvalutateException {
				return info.getParameters()[0];
			}
			
		}
		
	}
}
