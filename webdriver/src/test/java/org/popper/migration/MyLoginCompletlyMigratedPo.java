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

import org.openqa.selenium.WebElement;
import org.popper.fw.element.IButton;
import org.popper.fw.element.ITextBox;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.locator.Locator;

@Page(name="My Login")
public interface MyLoginCompletlyMigratedPo {
	@Locator(name="Username", id="username")
	ITextBox getUsernameTextbox();

	@Locator(name="Password", id="password")
	ITextBox getPasswordTextbox();

	@Locator(name="Submit", id="submit")
	IButton getLoginButton();

	@Locator(name="Header", id="header")
	Header getHeader();

	public static interface Header {
		@Locator(name="Index Link", xpath="./a[1]")
		WebElement getIndexLink();
	}
}
