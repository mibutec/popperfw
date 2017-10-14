package org.popper.testpos;

import org.popper.fw.element.IButton;
import org.popper.fw.element.IElement;
import org.popper.fw.element.ILabel;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.locator.Locator;

@Page
public interface GenericPo<T extends IElement> {
	@Locator(id="47")
	T getSomeElement();
	
	@Page
	public static interface WrongGenerichandling extends GenericPo<ILabel> {
		
	}

	@Page
	public static interface CorrectGenerichandling extends GenericPo<IButton> {
		@Locator(id="47")
		IButton getSomeElement();
	}
}
