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

package org.popper.testpos;

import org.popper.fw.element.ILabel;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.PageAccessor;
import org.popper.fw.webdriver.annotations.locator.Locator;

@Page(name="Simple Po")
public interface DefaultAnnotationsWithParametersShortcutPO {
	@PageAccessor(uri="/pages/defaultAnnotations.html")
	void open();
	
	// calling idLocator("id", "cator") results in "idLocator"
	@Locator(name="IdLocator", id="{0}Lo{1}")
	ILabel idLocatorLabel(String part1, String part2);
	
	@Locator(name="XpathLocator", xpath="//*[@{0}='{1}']")
	ILabel xpathLocatorLabel(String attribute, String name);
	
	// calling classLocator("call", "cator") results in ".callidLocator"
	@Locator(name="ClassLocator", cssSelector=".classLocator")
	ILabel classLocatorLabel(String part1, String part2);
}
