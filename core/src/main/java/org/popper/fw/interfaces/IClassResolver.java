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
package org.popper.fw.interfaces;

/**
 * ExtensionPoint to allow Popper to insantiate more than just the internal classes. Implementing this class
 * allows you to use more types in constructor of your Popper pages or {@link IAnnotationProcessor}s
 * 
 * @author michael_bulla
 *
 */
public interface IClassResolver {
	/**
	 * Resolves a required type from constructor to a given object
	 * @param clazz The class from constructor to resolve
	 * @return Object of the given class
	 */
	public<T> T resolveClass(Class<T> clazz);
	
	/**
	 * Postprocessing of a created object
	 * @param instantiatedObject A newly be Popper instantiated object
	 */
	default public void postProcessInstantiatedOIbject(Object instantiatedObject) {
		// default implementation does nothing
	}
}
