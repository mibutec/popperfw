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
package org.popper.forge.api.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be annotated on annotations being processed by class forge. Tells class forge
 * in which order annotations should be processed, when there are several annotations
 * on one method.
 * 
 * In the following example SomeAnnotation2 will be always executed after SomeAnnotation1
 * when evaluating <code>SomePageObject.someMethod</code>
 * 
 * @ImplementedBy(...)
 * public @interface SomeAnnotation1 {
 * }
 *
 * @ImplementedBy(...)
 * @RunAfter(SomeAnnotation1.class)
 * public @interface SomeAnnotation2 {
 * }
 *
 * public interface SomePageObject {
 *   @SomeAnnotation1
 *   @SomeAnnotation2
 *   public SomeReturnType someMethod();
 * }
 * 
 * @author michael_bulla
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RunAfter {
	public Class<? extends Annotation>[] value();
}
