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