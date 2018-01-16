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
package org.popper.fw.impl;

import java.lang.reflect.InvocationHandler;

/**
 * Interface being added to page objects. Allows to reference from a created page object
 * to its {@link PageObjectImplementation} (thats the {@link InvocationHandler}) for the created
 * proxies
 * 
 * @author michael_bulla
 *
 */
public interface ImplHolder {
	/**
	 * get the {@link PageObjectImplementation} for a given page oject
	 */
	public PageObjectImplementation getImpl();
}
