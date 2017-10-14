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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To be annotated on evaluated methods, tells popper which type of an element to create as result
 * 
 * In the following example the return type of <code>someMethod()</code> is an interface of an element
 * which cannot be instantiate by popper. @Type tells popper to instantitate <code>SomeElementImplementation</code> 
 * 
 * public interface SomePageObject {
 *   {@literal @}Locator(...)
 *   {@literal @}Type(SomeElementImplementation.class)
 *   public SomeElementInterface someMethod();
 * }

 * @author michael_bulla
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Type {
	public Class<?> value();
}
