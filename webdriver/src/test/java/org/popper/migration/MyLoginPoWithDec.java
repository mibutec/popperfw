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

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.locator.Locator;

@Page(name="My Login")
public abstract class MyLoginPoWithDec {
	protected WebDriver driver;

	public MyLoginPoWithDec(WebDriver driver, SearchContext searchContext) {
		this.driver = driver;
	}

	@Locator(name="Username", id="username")
	public abstract WebElement getUsernameTextbox();

	@Locator(name="Password", id="password")
	public abstract WebElement getPasswordTextbox();

	@Locator(name="Submit", id="submit")
	public abstract WebElement getLoginButton();

	@Locator(name="Header", id="header")
	public abstract Header getHeader();

	public static class Header {
		protected final WebDriver driver;

		private SearchContext searchContext;

		public Header(WebDriver driver, By by) {
			this.driver = driver;
			this.searchContext = driver.findElement(by);
		}

		public WebElement getIndexLink() {
			return searchContext.findElement(By.xpath("./a[1]"));
		}
	}
}
