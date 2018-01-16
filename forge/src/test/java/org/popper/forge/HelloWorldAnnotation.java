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

import org.popper.forge.HelloWorldAnnotation.HelloWorldAnnotationProcessor;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.ReEvalutateException;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.annotations.ImplementedBy;

@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(HelloWorldAnnotationProcessor.class)
public @interface HelloWorldAnnotation {
	public static class HelloWorldAnnotationProcessor implements IAnnotationProcessor<HelloWorldAnnotation, String> {

		@Override
		public String processAnnotation(HelloWorldAnnotation locatorAnnotation, RuntimeContextInformation info, String lastResult)
				throws ReEvalutateException {
			return "Hello, world";
		}
	}
}
