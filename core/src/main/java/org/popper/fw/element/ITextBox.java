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
import org.popper.fw.annotations.naming.ParamName;

public interface ITextBox extends IInput {
	@Action(name="type")
	public void type(@ParamName("Text") String text);

	@Action(name="clear")
	public void clear();

	@Action(name="clear and type")
	public void text(@ParamName("Text") String text);
	
	@Accessor(name="text")
	public String getText();
}
