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

package org.popper.fw.element;

import org.popper.fw.annotations.naming.Accessor;
import org.popper.fw.annotations.naming.Action;

/**
 * Interfaces describing the function a checkbox must provide
 * @author Michael
 *
 */
public interface ICheckbox extends IInput {
	/**
	 * Ensure a checkbox is checked.
	 * If the checkbox before this action is unchecked, it will be checked
	 * If the checkbox before this action is checked, it will be unchecked
	 * 
	 */
	@Action(name="check")
	public void check();

	/**
	 * Ensure a checkbox is unchecked.
	 * (see check)
	 * 
	 */
	@Action(name="uncheck")
	public void uncheck();
	
	/**
	 * Toggles state of a checkbox (unchecked to checked, checked to unchecked)
	 */
	@Action(name="toggle")
	public void toggle();
	
	/**
	 * Returns the state of a checkbox
	 * @return true if checked, otherwise false
	 */
	@Accessor(name="is checked")
	public boolean ischecked();
}
