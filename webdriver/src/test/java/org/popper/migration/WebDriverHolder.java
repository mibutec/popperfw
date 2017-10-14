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

package org.popper.migration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.popper.fw.webdriver.Browser;

public class WebDriverHolder {
	private static WebDriver driver = null;
	
	public static WebDriver getDriver() {
		if (driver == null) {
			driver = new HtmlUnitDriver(false);
		}

		return driver;
	}
	
	public static Browser getBrowser() {
		return Browser.HTML_UNIT_NOJS;
	}

	public static String getBaseUrl() {
		return "file://" + WebDriverHolder.class.getResource("/").getFile();
	}
	
	public static void closeWebDriver() {
		if (driver != null) {
			driver.close();
			driver = null;
		}
	}
}
