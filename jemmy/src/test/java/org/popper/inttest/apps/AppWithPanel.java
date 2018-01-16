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
package org.popper.inttest.apps;

import javax.swing.JPanel;

import org.popper.fw.jemmy.identify.IdentifiableLabel;

public class AppWithPanel extends AbstractApp {
	protected void initUI() {
		createLayout(
			IdentifiableLabel.createLabel("label1", "Label 1"),
			IdentifiableLabel.createLabel("label2", "Label 2"),
			new InnerPane()
		);
	}
	
	public static class InnerPane extends JPanel {
		InnerPane() {
			add(IdentifiableLabel.createLabel("inner1", "inner Label 1"));
			add(IdentifiableLabel.createLabel("inner2", "inner Label 2"));
		}
	}
	
	public static void main(String[] args) {
		start(AppWithPanel.class);
	}
}