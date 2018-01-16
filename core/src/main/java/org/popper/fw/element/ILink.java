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
import org.popper.fw.annotations.naming.Action;

/**
 * Interface defining methdos a link must provide
 * @author Michael
 *
 */
public interface ILink extends IElement {
	/**
	 * Click on the link
	 */
	@Action(name="click")
	public void click();
	
	/**
	 * text inside the link if available. If no text inside, than empty string
	 * @return textvalue of the link
	 */
	@Accessor(name="text")
	public String text();
	
	/**
	 * Returns the content of the href-attribute
	 * @return href of the link
	 */
	@Accessor(name="target")
	public String href();
}
