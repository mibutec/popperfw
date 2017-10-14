package org.popper.inttest.apps;

import javax.swing.JButton;

import org.popper.fw.jemmy.identify.IdentifiableButton;
import org.popper.fw.jemmy.identify.IdentifiableLabel;

public class SimpleApp extends AbstractApp {
    private int cnt;

    private IdentifiableLabel cntLabel;

    @Override
    protected void initUI() {

        JButton quitButton = IdentifiableButton.createLabel("label1", "Quit");
        JButton plusButton = IdentifiableButton.createLabel("plus", "+");

        cntLabel = IdentifiableLabel.createLabel("cntId", Integer.toString(cnt));

        quitButton.addActionListener(event -> {
            System.exit(0);
        });

        plusButton.addActionListener(event -> {
            incCount();
        });

        createLayout(quitButton, plusButton, cntLabel, IdentifiableLabel.createLabel("bla'Id'", "Some Bla"));
    }

    private void incCount() {
        cntLabel.setText(Integer.toString(++cnt));
    }

    public static void main(String[] args) {
        start(SimpleApp.class);
    }
}