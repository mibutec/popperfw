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


import java.io.File;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.popper.fw.webdriver.elements.IWebFileupload;

/**
 * Implementation of a fileupload that's represented by a default fielupload control
 * @author Michael
 *
 */
public class DefaultFileupload extends AbstractInput implements IWebFileupload {
	
	public DefaultFileupload(WebElementReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.IFileupload#uploadFromClasspath(java.lang.String)
	 */
	@Override
	public void uploadFromClasspath(String resourcename) {
		checkEditability();

		try {
			URL url = DefaultFileupload.class.getResource(resourcename);
			if (url == null) {
				throw new RuntimeException("Ressource " + resourcename + " existiert nicht!");
			}
			File file = new File(url.toURI());
			getElement().sendKeys(file.getAbsolutePath());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.popper.fw.element.IFileupload#filename()
	 */
	@Override
	public String filename() {
		String ret = getElement().getAttribute("value");
		if (StringUtils.isEmpty(ret)) {
			return null;
		}
		
		return ret;
	}
}
