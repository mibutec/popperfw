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

import org.popper.fw.element.IButton;
import org.popper.fw.jemmy.annotations.Frame;
import org.popper.fw.jemmy.annotations.WaitForEvents;
import org.popper.fw.jemmy.annotations.locator.Locator;
import org.popper.fw.jemmy.elements.IJemmyButton;
import org.popper.fw.jemmy.elements.IJemmyLabel;

@Frame
@Locator(title = "Example App")
public interface SimpleAppPo {
    @Locator(xpath = "//*[@id='quit']")
    @WaitForEvents
    IJemmyButton quitButton();

    @Locator(xpath = "//*[@id='plus']")
    @WaitForEvents
    IJemmyButton plusButton();

    @Locator(xpath = "//*[@id='plus']")
    @WaitForEvents
    IButton plusButtonAsIButton();

    @Locator(xpath = "//*[@id='cntId']")
    @WaitForEvents
    IJemmyLabel cntLabel();

    @Locator(xpath = "//*[@id=\"bla'Id'\"]")
    @WaitForEvents
    IJemmyLabel someBlaLabel();

    @Locator(xpath = "//*[@id='notExisting']")
    @WaitForEvents
    IJemmyLabel notExistingLabel();
}
