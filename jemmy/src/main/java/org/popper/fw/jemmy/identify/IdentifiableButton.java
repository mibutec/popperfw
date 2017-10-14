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
package org.popper.fw.jemmy.identify;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class IdentifiableButton extends JButton implements Identifiable {
	private String id;

	public IdentifiableButton() {
		super();
	}
	
	public IdentifiableButton(Action a) {
		super(a);
	}


	public IdentifiableButton(Icon icon) {
		super(icon);
	}


	public IdentifiableButton(String text, Icon icon) {
		super(text, icon);
	}


	public IdentifiableButton(String text) {
		super(text);
	}


	public static IdentifiableButton createLabel(String id) {
		IdentifiableButton label = new IdentifiableButton();
		label.setId(id);
		
		return label;
	}


	public static IdentifiableButton createLabel(String id, Action a) {
		IdentifiableButton label = new IdentifiableButton(a);
		label.setId(id);
		
		return label;
	}


	public static IdentifiableButton createLabel(String id, String text, Icon icon) {
		IdentifiableButton label = new IdentifiableButton(text, icon);
		label.setId(id);
		
		return label;
	}


	public static IdentifiableButton createLabel(String id, String text) {
		IdentifiableButton label = new IdentifiableButton(text);
		label.setId(id);
		
		return label;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
