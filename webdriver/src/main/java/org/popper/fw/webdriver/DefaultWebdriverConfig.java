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
package org.popper.fw.webdriver;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.LocaleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * This class holds the global WebDriver instance for reuse between different
 * adapters and different tables on one FitNesse page. The default values point
 * to the GVS system test stage
 * 
 * @author Michael Bulla
 */
public class DefaultWebdriverConfig implements IWebdriverConfig {
	/*
	 * a logger instance for this class
	 */
	private static final transient Logger logger = LogManager
			.getLogger(DefaultWebdriverConfig.class);

	/**
	 * The used browser, default is firefox
	 */
	private Browser browser = Browser.FIREFOX;

	/**
	 * The machine where the selenium RC server is running, default is localhost
	 */
	private String seleniumHost = "localhost:4444";

	/**
	 * indicates the implicit wait time which is used by webdriver for the
	 * findElement method
	 */
	private int implicitTimeout = 1500;

	/**
	 * Timeout to be used to verify an element does not exist
	 */
	private int checkNotPresentTimeout = 1500;

	/**
	 * Local to configure browsers preferred language
	 */
	private Locale locale = Locale.GERMAN;
	
	private WebDriver createdDriver;

	/**
	 * Base-Url that will be used to generate urls when jumping directly to
	 * pages
	 */
	private String baseUrl;

	public void configureByPropertyfile(String propertyFile) throws IOException {
		Properties props = new Properties();
		props.load(DefaultWebdriverConfig.class
				.getResourceAsStream(propertyFile));
		if (props.getProperty("browserLanguage").contains("$")) {
			props.load(DefaultWebdriverConfig.class
					.getResourceAsStream("/tui.properties"));
		}

		browser = Browser.valueOf(props.getProperty("browserName"));
		seleniumHost = props.getProperty("seleniumHost");
		implicitTimeout = new Integer(props.getProperty("implicitTimeout"));
		locale = LocaleUtils.toLocale(props.getProperty("browserLanguage"));
		baseUrl = props.getProperty("baseUrl");
	}

	@Override
    public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public String getSeleniumHost() {
		return seleniumHost;
	}

	public void setSeleniumHost(String seleniumHost) {
		this.seleniumHost = seleniumHost;
	}
	
	public void setImplicitTimeout(int implicitTimeout) {
		this.implicitTimeout = implicitTimeout;
	}

	public void setCheckNotPresentTimeout(int checkNotPresentTimeout) {
		this.checkNotPresentTimeout = checkNotPresentTimeout;
	}

	@Override
	public int implicitTimeout() {
		return implicitTimeout;
	}
	
	@Override
	public int checkNotPresentTimeout() {
		return checkNotPresentTimeout;
	}

	@Override
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public WebDriver createDriver() {
		try {
			if (browser == Browser.HTML_UNIT
					|| browser == Browser.HTML_UNIT_NOJS) {
				BrowserVersion v = BrowserVersion.FIREFOX_3_6;
				v.setBrowserLanguage(formatLocale(locale));
				HtmlUnitDriver theDriver = new HtmlUnitDriver(v);

				if (browser != Browser.HTML_UNIT_NOJS) {
					theDriver.setJavascriptEnabled(true);
				}
				createdDriver =  theDriver;
			} else if(browser == Browser.FIREFOX) {
				createdDriver = new FirefoxDriver();
			} else {
				logger.debug("[setupWebdriver]for:" + browser);
				DesiredCapabilities caps = getCaps(browser);

				logger.debug("Next step, now we initialize the driver with the caps with SeleniumServer: "
						+ getSeleniumHost());

				logger.debug("Gettin' RemoteWebDriver!! ");
				String driverUrl = "http://" + getSeleniumHost() + "/wd/hub";
				RemoteWebDriver remoteDriver = new RemoteWebDriver(new URL(
						driverUrl), caps);
				remoteDriver.setFileDetector(new LocalFileDetector());

				createdDriver = remoteDriver;
			}
			createdDriver
				.manage()
				.timeouts()
				.implicitlyWait(implicitTimeout(),
					TimeUnit.MILLISECONDS);
			
			return createdDriver;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static DesiredCapabilities getCaps(Browser browser) {
		DesiredCapabilities caps = null;

		if (browser == Browser.REMOTE_INTERNET_EXPLORER) {
			logger.debug("INTERNET EXPLORER CAPS: " + caps);
			caps = DesiredCapabilities.internetExplorer();
		} else if (browser == Browser.CHROME) {
			logger.debug("CHROME CAPS: " + caps);
			caps = DesiredCapabilities.chrome();
		} else {
			caps = DesiredCapabilities.firefox();
			logger.debug("FIREFOX CAPS: " + caps);
		}

		return caps;
	}

	private static String formatLocale(Locale localeToFormat) {
		return localeToFormat.getCountry().length() == 0 ? localeToFormat
				.getLanguage() : localeToFormat.getLanguage() + "-"
				+ localeToFormat.getCountry().toLowerCase();
	}
}
