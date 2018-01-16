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
package org.popper.migration;

import org.openqa.selenium.WebDriver;
import org.popper.fw.webdriver.Browser;
import org.popper.fw.webdriver.IWebdriverConfig;

public class WebdriverProxyConfig implements IWebdriverConfig {

	@Override
	public WebDriver createDriver() {
		return WebDriverHolder.getDriver();
	}

	@Override
	public Browser getBrowser() {
		return WebDriverHolder.getBrowser();
	}

	@Override
	public String getBaseUrl() {
		return WebDriverHolder.getBaseUrl();
	}

	@Override
	public int implicitTimeout() {
		return 1500;
	}

	@Override
	public int checkNotPresentTimeout() {
		return 1500;
	}
}
