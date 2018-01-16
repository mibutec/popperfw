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

import javax.swing.JButton;
import javax.swing.JDialog;

import org.popper.fw.jemmy.identify.IdentifiableButton;
import org.popper.fw.jemmy.identify.IdentifiableLabel;

public class AppWithModalSubwindow extends AbstractApp {
	protected void initUI() {

		JButton subButton = IdentifiableButton.createLabel("openSub", "open");
		subButton.addActionListener(event -> {
			final JDialog frame = new JDialog(this, "Sub", true);
			frame.getContentPane().add(IdentifiableLabel.createLabel("dialogLabel", "Sub"));
			
			JButton subsubButton = IdentifiableButton.createLabel("openSubSub", "open");
			frame.getContentPane().add(subsubButton);

			subsubButton.addActionListener(event2 -> {
				final JDialog frame2 = new JDialog(this, "SubSub", true);
				frame2.getContentPane().add(IdentifiableLabel.createLabel("dialogLabel", "Subsub"));
				frame2.pack();
				frame2.setVisible(true);
			});
			
			frame.pack();
			frame.setVisible(true);

		});
		
		createLayout(subButton);
	}

	public static void main(String[] args) {
		start(AppWithModalSubwindow.class);
	}
}