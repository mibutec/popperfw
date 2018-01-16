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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Assert;
import org.junit.Test;
import org.popper.forge.ParameterTest.ParametrizedHelloWorldAnnotation.ParametrizedHelloWorldAnnotationProcessor;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;

public class ParameterTest {
	private ClassForge testee = new ClassForge();
	
	@Test
	public void shouldUseParameters() {
		ParamtetrizedBlank blank = testee.createInstance(ParamtetrizedBlank.class);
		Assert.assertEquals("Hello, Michael", blank.sayHello("Michael"));
	}
	
	private static interface ParamtetrizedBlank {
		@ParametrizedHelloWorldAnnotation
		String sayHello(String name);
	}

	@Inherited
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@org.popper.forge.api.annotations.ImplementedBy(ParametrizedHelloWorldAnnotationProcessor.class)
	static @interface ParametrizedHelloWorldAnnotation {
		static class ParametrizedHelloWorldAnnotationProcessor implements IAnnotationProcessor<ParametrizedHelloWorldAnnotation, Object> {

			@Override
			public Object processAnnotation(ParametrizedHelloWorldAnnotation locatorAnnotation, RuntimeContextInformation info, Object lastResult)
					throws ReEvalutateException {
				return "Hello, " + info.getParameters()[0];
			}
		}
	}
}
