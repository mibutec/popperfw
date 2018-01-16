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
package org.popper.fw.element;

import org.popper.fw.annotations.naming.Accessor;

/**
 * Interface definig the methdos an image must provide
 * @author Michael
 *
 */
public interface IImage extends IElement {
	/**
	 * Checks if the image could be loaded by the browser and is shown to the user
	 * @return true if the image is loaded and rendered, otherwise false
	 */
	@Accessor(name="is rendered")
	public boolean isRendered();
	
	/**
	 * Returns the url (src-attribute of an image)
	 * @return url
	 */
	@Accessor(name="url")
	public String getUrl();
}
