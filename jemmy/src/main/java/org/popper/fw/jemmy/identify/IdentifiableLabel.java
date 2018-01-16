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
package org.popper.fw.jemmy.identify;

import javax.swing.Icon;
import javax.swing.JLabel;

public class IdentifiableLabel extends JLabel implements Identifiable {
	private String id;

	public IdentifiableLabel() {
		super();
	}


	public IdentifiableLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}


	public IdentifiableLabel(Icon image) {
		super(image);
	}


	public IdentifiableLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}


	public IdentifiableLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}


	public IdentifiableLabel(String text) {
		super(text);
	}
	
	public static IdentifiableLabel createLabel(String id) {
		IdentifiableLabel label = new IdentifiableLabel();
		label.setId(id);
		
		return label;
	}


	public static IdentifiableLabel createLabel(String id, Icon image, int horizontalAlignment) {
		IdentifiableLabel label = new IdentifiableLabel(image, horizontalAlignment);
		label.setId(id);
		
		return label;
	}


	public static IdentifiableLabel createLabel(String id, Icon image) {
		IdentifiableLabel label = new IdentifiableLabel(image);
		label.setId(id);
		
		return label;
	}


	public static IdentifiableLabel createLabel(String id, String text, Icon icon, int horizontalAlignment) {
		IdentifiableLabel label = new IdentifiableLabel(text, icon, horizontalAlignment);
		label.setId(id);
		
		return label;
	}


	public static IdentifiableLabel createLabel(String id, String text, int horizontalAlignment) {
		IdentifiableLabel label = new IdentifiableLabel(text, horizontalAlignment);
		label.setId(id);
		
		return label;
	}


	public static IdentifiableLabel createLabel(String id, String text) {
		IdentifiableLabel label = new IdentifiableLabel(text);
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
