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