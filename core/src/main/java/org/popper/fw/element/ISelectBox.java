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
import org.popper.fw.annotations.naming.ParamName;

/**
 * Definies the methods a selectbox must provide
 * @author Michael
 *
 */
public interface ISelectBox extends IInput {
	/**
	 * Select the option based on the visible text of that option
	 * @param text visible text
	 */
	@Action(name="select by text")
	public void selectByText(@ParamName("Displayed Text") String text);
	
	/**
	 * get the visible text of the selected option
	 * @return text of the selected option
	 */
	@Accessor(name="selected text")
	public String getSelectedText();
	
	@Override
	default String value() {
		return getSelectedText();
	}

	@Override
	default void value(String value) {
		selectByText(value);
	}

}
