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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.popper.forge.ClassForge;
import org.popper.forge.ClassForgeConfig;
import org.popper.forge.HelloWorldAnnotation.HelloWorldAnnotationProcessor;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.IMethodAnalyzer;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.annotations.AnalyzedBy;

public class ExtensionTest {
	@Test
	public void shouldAllowChangingProcessor() {
		ClassForge testee = new ClassForge(new ClassForgeConfig().addAnnotationProcessorOverride(HelloWorldAnnotation.class, MyHelloWorldAnnotationProcessor.class));
		SimpleBlank blank = testee.createInstance(SimpleBlank.class);
		Assert.assertEquals("Good morning, world", blank.sayHello());
	}
	
	@Test
	public void shouldCreateInstanceWithDifferentBehaviorWhenConfiguredDifferent() {
		ClassForge testee = new ClassForge();
		ClassForge otherAssembler = new ClassForge(new ClassForgeConfig().addAnnotationProcessorOverride(HelloWorldAnnotation.class, DifferentSayHelloImplementation.class));
		
		SimpleBlank simpleBlank = testee.createInstance(SimpleBlank.class);
		SimpleBlank otherBlank = otherAssembler.createInstance(SimpleBlank.class);
		
		assertEquals("Hello, world", simpleBlank.sayHello());
		assertEquals("Good morning, world", otherBlank.sayHello());
	}
	
	@Test
	public void shouldUseCustomAnalyzers() {
		ClassForge testee = new ClassForge();
		BlankUsingCustomAnalyzer blank = testee.createInstance(BlankUsingCustomAnalyzer.class);
		
		assertEquals("Hello, world", blank.unAnnotatedMethod());
	}

	@AnalyzedBy(MyMethodAnlyzer.class)
	private static interface BlankUsingCustomAnalyzer {
		String unAnnotatedMethod();
	}
	
	private static class MyMethodAnlyzer implements IMethodAnalyzer {
		@Override
		public List<AnnotationProcessorTupel> findProcessorsForMethod(Method method) {
			AnnotationProcessorTupel ret = new AnnotationProcessorTupel(new HelloWorldAnnotation() {

				@Override
				public Class<? extends Annotation> annotationType() {
					return HelloWorldAnnotation.class;
				}}, method, HelloWorldAnnotationProcessor.class);
			
			return Arrays.asList(ret);
		}
	}
	
	private interface SimpleBlank {
		@HelloWorldAnnotation
		String sayHello();
	}
	
	private static class MyHelloWorldAnnotationProcessor implements IAnnotationProcessor<HelloWorldAnnotation, String> {
		@Override
		public String processAnnotation(HelloWorldAnnotation locatorAnnotation, RuntimeContextInformation info,
				String lastResult) throws ReEvalutateException {
			return "Good morning, world";
		}
	}
	
	private static class DifferentSayHelloImplementation implements IAnnotationProcessor<HelloWorldAnnotation, String> {
		
		@Override
		public String processAnnotation(HelloWorldAnnotation locatorAnnotation, RuntimeContextInformation info,
				String lastResult) throws ReEvalutateException {
			return "Good morning, world";
		}
	}
}
