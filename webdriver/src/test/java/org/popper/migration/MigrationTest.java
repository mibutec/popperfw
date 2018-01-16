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

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.popper.fw.webdriver.WebdriverContext;
import org.popper.fw.webdriver.WebdriverFactory;

public class MigrationTest {
	public static WebdriverFactory popperFactory;
	
	@Test
	public void testLegacy() {
		WebDriver driver = WebDriverHolder.getDriver();
		driver.get(WebDriverHolder.getBaseUrl() + "/pages/myLogin.html");
		MyLoginPo login = new MyLoginPo(driver, null);
	    login.getUsernameTextbox().sendKeys("user");
	    login.getPasswordTextbox().sendKeys("secret");
	    login.getLoginButton().click();
	    Assert.assertEquals("user", login.getUsernameTextbox().getAttribute("value"));
	    Assert.assertEquals("secret", login.getPasswordTextbox().getAttribute("value"));
	    Assert.assertEquals("Index", login.getHeader().getIndexLink().getText());
	    WebDriverHolder.closeWebDriver();
	}

	@Test
	public void testLegacyWithFactory() {
		WebdriverContext context = new WebdriverContext();
        context.setConfig(new WebdriverProxyConfig());
        popperFactory = context.getFactory();
        WebDriverHolder.getDriver().get(WebDriverHolder.getBaseUrl() + "/pages/myLogin.html");
		
		MyLoginPo login = popperFactory.createLegacyPage(MyLoginPo.class, "MyLogin");
	    login.getUsernameTextbox().sendKeys("user");
	    login.getPasswordTextbox().sendKeys("secret");
	    login.getLoginButton().click();
	    Assert.assertEquals("user", login.getUsernameTextbox().getAttribute("value"));
	    Assert.assertEquals("secret", login.getPasswordTextbox().getAttribute("value"));
	    Assert.assertEquals("Index", login.getHeader().getIndexLink().getText());
	    WebDriverHolder.closeWebDriver();
	}
	
	@Test
	public void testLegacyWithDeclarations() {
		WebdriverContext context = new WebdriverContext();
        context.setConfig(new WebdriverProxyConfig());
        popperFactory = context.getFactory();
        WebDriverHolder.getDriver().get(WebDriverHolder.getBaseUrl() + "/pages/myLogin.html");
		
		MyLoginPoWithDec login = popperFactory.createPage(MyLoginPoWithDec.class);
	    login.getUsernameTextbox().sendKeys("user");
	    login.getPasswordTextbox().sendKeys("secret");
	    login.getLoginButton().click();
	    Assert.assertEquals("user", login.getUsernameTextbox().getAttribute("value"));
	    Assert.assertEquals("secret", login.getPasswordTextbox().getAttribute("value"));
	    Assert.assertEquals("Index", login.getHeader().getIndexLink().getText());
	    WebDriverHolder.closeWebDriver();
	}

	@Test
	public void testLegacyWithAsInterfaceDeclarations() {
		WebdriverContext context = new WebdriverContext();
        context.setConfig(new WebdriverProxyConfig());
        popperFactory = context.getFactory();
        WebDriverHolder.getDriver().get(WebDriverHolder.getBaseUrl() + "/pages/myLogin.html");
		
		MyLoginAsInterfacePo login = popperFactory.createPage(MyLoginAsInterfacePo.class);
	    login.getUsernameTextbox().type("user");
	    login.getPasswordTextbox().type("secret");
	    login.getLoginButton().click();
	    Assert.assertEquals("user", login.getUsernameTextbox().getText());
	    Assert.assertEquals("secret", login.getPasswordTextbox().getText());
	    Assert.assertEquals("Index", login.getHeader().getIndexLink().getText());
	    WebDriverHolder.closeWebDriver();
	}

	@Test
	public void testLegacyWithCompletyMigrated() {
		WebdriverContext context = new WebdriverContext();
        context.setConfig(new WebdriverProxyConfig());
        popperFactory = context.getFactory();
        WebDriverHolder.getDriver().get(WebDriverHolder.getBaseUrl() + "/pages/myLogin.html");
		
		MyLoginCompletlyMigratedPo login = popperFactory.createPage(MyLoginCompletlyMigratedPo.class);
	    login.getUsernameTextbox().type("user");
	    login.getPasswordTextbox().type("secret");
	    login.getLoginButton().click();
	    Assert.assertEquals("user", login.getUsernameTextbox().getText());
	    Assert.assertEquals("secret", login.getPasswordTextbox().getText());
	    Assert.assertEquals("Index", login.getHeader().getIndexLink().getText());
	    WebDriverHolder.closeWebDriver();
	}

	@Test
	public void testLegacyWitInnerTypeMigrated() {
		WebdriverContext context = new WebdriverContext();
        context.setConfig(new WebdriverProxyConfig());
        popperFactory = context.getFactory();
        WebDriverHolder.getDriver().get(WebDriverHolder.getBaseUrl() + "/pages/myLogin.html");
		
		MyLoginHeaderMigratedPo login = popperFactory.createLegacyPage(MyLoginHeaderMigratedPo.class, "My Login");
	    login.getUsernameTextbox().sendKeys("user");
	    login.getPasswordTextbox().sendKeys("secret");
	    login.getLoginButton().click();
	    Assert.assertEquals("user", login.getUsernameTextbox().getAttribute("value"));
	    Assert.assertEquals("secret", login.getPasswordTextbox().getAttribute("value"));
	    Assert.assertEquals("Index", login.getHeader().getIndexLink().getText());
	    WebDriverHolder.closeWebDriver();
	}
}
