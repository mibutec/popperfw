/*
 * Copyright [2013] [Michael Bulla, michaelbulla@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.popper.fw.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be annotated on annotations being processed by popper. Tells popper
 * in which order annotations should be processed, when there are several annotations
 * on one method.
 * 
 * In the following example SomeAnnotation2 will be always executed after SomeAnnotation1
 * when evaluating <code>SomePageObject.someMethod</code>
 * 
 * {@literal @}ImplementedBy(...)
 * public @interface SomeAnnotation1 {
 * }
 *
 * {@literal @}ImplementedBy(...)
 * {@literal @}RunAfter(SomeAnnotation1.class)
 * public {@literal @}interface SomeAnnotation2 {
 * }
 *
 * public interface SomePageObject {
 *   {@literal @}SomeAnnotation1
 *   {@literal @}SomeAnnotation2
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
