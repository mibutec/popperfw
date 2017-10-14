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

package org.popper.fw.interfaces;

import java.lang.annotation.Annotation;

/**
 * Extension Point to add more Annotations to popper.
 * 
 *  * In the following example the result of <code>SomePageObject.someMethod()</code> will be created by
 * <code>SomeProcessor.processAnnotation</code>
 *
 * {@literal @}ImplementedBy(SomeProcessor.class) 
 * public {@literal @}interface SomeAnnotation {
 * }
 * 
 * public class SomeProcessor implements IAnnotationProcessor<SomeAnnotation, SomeReturnType> {
 *		public SomeReturnType processAnnotation(SomeAnnotation pageAccessorAnnotation, LocatorContextInformation info, SomeReturnType lastResult) {
 *        return new SomeReturnType();
 *		}
 * }
 * 
 * public interface SomePageObject {
 *   {@literal @}SomeAnnotation
 *   public SomeReturnType someMethod();
 * }

 * @author michael_bulla
 */
public interface IAnnotationProcessor<A extends Annotation, R> {
	public R processAnnotation(A locatorAnnotation, LocatorContextInformation info, R lastResult) throws ReEvalutateException;
}
