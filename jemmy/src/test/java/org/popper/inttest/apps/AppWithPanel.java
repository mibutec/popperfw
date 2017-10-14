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