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
package org.popper.testpos;

import java.util.HashMap;
import java.util.Map;

import org.popper.fw.element.ILabel;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.PageAccessor;
import org.popper.fw.webdriver.annotations.locator.Locator;

@Page(name="Abstract PO")
public abstract class AbstractClassPO {
	@PageAccessor(uri="/pages/defaultAnnotations.html")
	public abstract void open();
	
	@Locator(name="IdLocator", id="idLocator")
	abstract ILabel idLocatorLabel();
	
	@Locator(name="XpathLocator", xpath="//*[@name='xpathLocator']")
	abstract ILabel xpathLocatorLabel();
	
	@Locator(name="ClassLocator", cssSelector=".classLocator")
	abstract ILabel classLocatorLabel();
	
	public Map<String, String> getLabels() {
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("id", idLocatorLabel().text());
		ret.put("xpath", xpathLocatorLabel().text());
		ret.put("class", classLocatorLabel().text());
		
		return ret;
	}
}
