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
 * Interface defining the methods a radio-button must provide
 * @author Michael
 *
 */
public interface IRadioButton extends IInput {
	/**
	 * Select this radio-button
	 */
	@Action(name="select")
	public void select();
	
	/**
	 * Check if this radiobutton is selected
	 * @return true if is selected, otherwise false
	 */
	@Accessor(name="is selected")
	public boolean isSelected();
}
