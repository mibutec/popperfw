package org.popper.inttest.pos;

import org.netbeans.jemmy.operators.JLabelOperator;
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
    JLabelOperator cntLabel();

    @Locator(xpath = "//*[@id=\"bla'Id'\"]")
    @WaitForEvents
    IJemmyLabel someBlaLabel();

    @Locator(xpath = "//*[@id='notExisting']")
    @WaitForEvents
    IJemmyLabel notExistingLabel();
}
