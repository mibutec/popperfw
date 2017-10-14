/*
 * Copyright [2013] [Michael Bulla, michaelbulla@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.popper.fw.webdriver.elements.impl;


import org.openqa.selenium.JavascriptExecutor;
import org.popper.fw.webdriver.Browser;
import org.popper.fw.webdriver.elements.IWebImage;

/**
 * Implementation of an Image-Element
 * @author Michael
 *
 */
public class DefaultImage extends AbstractWebElement implements IWebImage {
	public DefaultImage(WebElementReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.IImage#isRendered()
	 */
	@Override
	public boolean isRendered() {
		// There is no natural way to check if an image is loaded, there are just "JS-hacks"
		
		// "JS-hack" for firefox
		String script = "return (typeof arguments[0].naturalWidth!=\"undefined\" && arguments[0].naturalWidth > 0)";

		// "JS-hack" for internet explorer
		if (Browser.REMOTE_INTERNET_EXPLORER == getContext().getBrowser()) {
			script = "return arguments[0].complete";
		}
		
		// TODO: check compatibility for other browsers
		return (Boolean) ((JavascriptExecutor) getDriver()).executeScript(script, getElement());	
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.IImage#getUrl()
	 */
	@Override
	public String getUrl() {
		return getElement().getAttribute("src");
	}

}
