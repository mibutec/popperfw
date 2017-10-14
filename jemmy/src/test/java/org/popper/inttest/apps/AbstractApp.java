package org.popper.inttest.apps;

import java.awt.Container;
import java.awt.EventQueue;



import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class AbstractApp extends JFrame {
	public AbstractApp() {
		initUI();
		setTitle("Example App");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	protected abstract void initUI();
	
	protected void createLayout(JComponent... arg) {
		Container pane = getContentPane();
		BoxLayout bl = new BoxLayout(pane, 1);
		pane.setLayout(bl);

		for (JComponent component : arg) {
			pane.add(component);
		}
	}
	
	protected static JPanel createPanel(JComponent... arg) {
		JPanel panel = new JPanel();
		for (JComponent component : arg) {
			panel.add(component);
		}
		return panel;
	}
	
	public static void start(Class<? extends AbstractApp> appClass) {
		EventQueue.invokeLater(() -> {
			try {
				appClass.newInstance().setVisible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
}