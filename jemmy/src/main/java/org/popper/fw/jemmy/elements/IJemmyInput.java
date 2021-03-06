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
package org.popper.fw.jemmy.elements;

import org.popper.fw.annotations.naming.Accessor;
import org.popper.fw.element.IInput;

/**
 * Interface describing alle the methods common to controls, that can handle userinput
 * @author Michael
 *
 */
public interface IJemmyInput extends IInput, IJemmyElement {
	/**
	 * Checks if the element can be interacted now or if it's disabled/readonly/...
	 * @return true if editable, otherwise false
	 */
	@Accessor(name="is editable")
	public boolean isEditable();
}
