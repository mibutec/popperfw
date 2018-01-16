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
package org.popper.inttest.pos;

import org.popper.fw.jemmy.annotations.Frame;
import org.popper.fw.jemmy.annotations.locator.Locator;
import org.popper.fw.jemmy.elements.IJemmyLabel;
import org.popper.inttest.apps.AppWithPanel.InnerPane;

@Frame
@Locator(title = "Example App")
public interface AppWithPanelPo {
    @Locator(xpath = "//*[@id='label1']")
    IJemmyLabel outer1();

    @Locator(xpath = "//*[@id='label2']")
    IJemmyLabel outer2();

    @Locator(componentClass = InnerPane.class)
    ThePanel panel();

    public interface ThePanel {
        @Locator(xpath = "//*[@id='inner1']")
        IJemmyLabel inner1();

        @Locator(xpath = "//*[@id='inner2']")
        IJemmyLabel inner2();
    }
}
