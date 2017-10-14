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
