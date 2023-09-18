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
package org.popper.inttest;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.popper.fw.element.ILabel;
import org.popper.testpos.TablePO;
import org.popper.testpos.TablePO.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableTest extends AbstractIntTest {
	@Test
	public void testTable() {
		TablePO table = factory.createPage(TablePO.class);
		table.open();
		
		User user1 = table.userById("1");
		assertEquals("Bulla", user1.lastname().text());
		assertEquals("Michael", user1.firstname().text());
		assertEquals("01.01.1970", user1.birthdate().text());
		User user2 = table.userById("2");
		assertEquals("Dude", user2.lastname().text());
		assertEquals("The", user2.firstname().text());
		assertEquals("01.01.1971", user2.birthdate().text());
		User user3 = table.userById("3");
		assertEquals("Duderino", user3.lastname().text());
		assertEquals("El", user3.firstname().text());
		assertEquals("01.01.1972", user3.birthdate().text());
		User user4 = table.userById("4");
		assertEquals("Lebowski", user4.lastname().text());
		assertEquals("Jeffrey", user4.firstname().text());
		assertEquals("01.01.1973", user4.birthdate().text());
		
		List<User> users = table.allUsers();
		assertEquals(4, users.size());
		user1 = users.get(0);
		assertEquals("Bulla", user1.lastname().text());
		assertEquals("Michael", user1.firstname().text());
		assertEquals("01.01.1970", user1.birthdate().text());
		user2 = users.get(1);
		assertEquals("Dude", user2.lastname().text());
		assertEquals("The", user2.firstname().text());
		assertEquals("01.01.1971", user2.birthdate().text());
		user3 = users.get(2);
		assertEquals("Duderino", user3.lastname().text());
		assertEquals("El", user3.firstname().text());
		assertEquals("01.01.1972", user3.birthdate().text());
		user4 = users.get(3);
		assertEquals("Lebowski", user4.lastname().text());
		assertEquals("Jeffrey", user4.firstname().text());
		assertEquals("01.01.1973", user4.birthdate().text());
		
		List<ILabel> allForenames = table.allForenames();
		assertEquals(4, allForenames.size());
		assertEquals("Michael", allForenames.get(0).text());
		assertEquals("The", allForenames.get(1).text());
		assertEquals("El", allForenames.get(2).text());
		assertEquals("Jeffrey", allForenames.get(3).text());
		
		Set<User> usersAsSet = table.allUsersAsSet();
		assertEquals(4, usersAsSet.size());

		User[] usersAsArray = table.allUsersAsArray();
		assertEquals(4, usersAsArray.length);
		assertEquals("Bulla", usersAsArray[0].lastname().text());
		assertEquals("Dude", usersAsArray[1].lastname().text());
		assertEquals("Duderino", usersAsArray[2].lastname().text());
		assertEquals("Lebowski", usersAsArray[3].lastname().text());
	}
	
	@Test
	public void testWebElementAsList() {
		TablePO table = factory.createPage(TablePO.class);
		table.open();
		
		List<WebElement> elements = table.allFirstnamesAsWebelement();
		assertEquals(4, elements.size());
		assertEquals("Michael", elements.get(0).getText());
		assertEquals("The", elements.get(1).getText());
		assertEquals("El", elements.get(2).getText());
		assertEquals("Jeffrey", elements.get(3).getText());
	}

}
