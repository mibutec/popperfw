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

import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.popper.fw.element.ILabel;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.PageAccessor;
import org.popper.fw.webdriver.annotations.locator.Locator;

@Page(name="Self Implemented Locator")
public interface TablePO {
	@PageAccessor(uri="/pages/table.html")
	void open();

	@Locator(name="all users as list", xpath="//table[@id='users']//tbody/tr[.//td]")
	List<User> allUsers();
	
	@Locator(name="all forenames", xpath="//table[@id='users']//tr/td[@class='firstname']")
	List<ILabel> allForenames();
	
	@Locator(name="all users as set", xpath="//table[@id='users']//tbody/tr[.//td]")
	Set<User> allUsersAsSet();
	
	@Locator(name="all users as array", xpath="//table[@id='users']//tbody/tr[.//td]")
	User[] allUsersAsArray();
	
	@Locator(name="all users", xpath="//table[@id='users']//tr[@id={0}]")
	User userById(String id);
	
	@Locator(name="all users", xpath="//table[@id='users']//*[@class='firstname']")
	List<WebElement> allFirstnamesAsWebelement();
	
	public static interface User {
		@Locator(name="Firstname", cssSelector=".firstname")
		ILabel firstname();

		@Locator(name="Lastname", cssSelector=".lastname")
		ILabel lastname();

		@Locator(name="Birthdate", cssSelector=".birthdate")
		ILabel birthdate();
	}
}
