package org.popper.fw.element;


/**
 * Main-Interface of all usable Elements
 * @author Michael
 *
 */
public interface IElement {
	/**
	 * Says if this element is visible on GUI
	 */
	boolean isDisplayed();

	/**
	 * says if the element is selected
	 */
	boolean isSelected();

}
