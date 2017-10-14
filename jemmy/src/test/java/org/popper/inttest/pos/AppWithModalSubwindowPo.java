package org.popper.inttest.pos;

import org.popper.fw.jemmy.annotations.Dialog;
import org.popper.fw.jemmy.annotations.Frame;
import org.popper.fw.jemmy.annotations.locator.Locator;
import org.popper.fw.jemmy.annotations.locator.Window;
import org.popper.fw.jemmy.elements.IJemmyButton;
import org.popper.fw.jemmy.elements.IJemmyLabel;

@Frame
@Locator(title = "Example App")
public interface AppWithModalSubwindowPo {
    @Locator(xpath = "//*[@id='openSub']")
    IJemmyButton openSubButton();

    @Window
    @Locator(title = "Sub")
    Sub subWindow();

    @Dialog
    @Locator(title = "Sub")
    public static interface Sub {
        @Locator(xpath = "//*[@id='openSubSub']")
        IJemmyButton openSubSubButton();

        @Locator(xpath = "//*[@id='dialogLabel']")
        IJemmyLabel label();

        @Window
        @Locator(title = "SubSub")
        SubSub subSubwindow();
    }

    @Dialog
    @Locator(title = "SubSub")
    public static interface SubSub {
        @Locator(xpath = "//*[@id='dialogLabel']")
        IJemmyLabel label();
    }
}
