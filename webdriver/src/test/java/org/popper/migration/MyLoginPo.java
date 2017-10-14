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

public class MyLoginPo {
	private WebDriver driver;

	public MyLoginPo(WebDriver driver, SearchContext searchContext) {
		this.driver = driver;
	}

	public WebElement getUsernameTextbox() {
		return driver.findElement(By.id("username"));
	}

	public WebElement getPasswordTextbox() {
		return driver.findElement(By.id("password"));
	}

	public WebElement getLoginButton() {
		return driver.findElement(By.id("submit"));
	}

	public Header getHeader() {
		WebElement searchContextForHeader = driver.findElement(By.id("header"));
		return new Header(driver, searchContextForHeader);
	}

	public static class Header {
		protected final WebDriver driver;

		private SearchContext searchContext;

		public Header(WebDriver driver, SearchContext searchContext) {
			this.driver = driver;
			this.searchContext = searchContext;
		}

		public WebElement getIndexLink() {
			return searchContext.findElement(By.xpath("./a[1]"));
		}
	}
}
