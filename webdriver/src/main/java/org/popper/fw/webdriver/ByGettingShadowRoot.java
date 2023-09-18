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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * By variant, which will delegate to another By for searching, but return the found elements shadow root
 * instead of the element.
 */
public class ByGettingShadowRoot extends By {

    private final By realBy;

    public ByGettingShadowRoot(By realBy) {
        super();
        this.realBy = realBy;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        List<WebElement> all = realBy.findElements(context);
        List<WebElement> shadowRoots = new ArrayList<>();
        for (WebElement original : all) {
            shadowRoots.add(getShadowRoot(original, context));
        }
        return shadowRoots;
    }

    public static WebElement getShadowRoot(WebElement parent, SearchContext context) {
        WebDriver driver = getDriver(context);
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", parent);
    }

    private static WebDriver getDriver(SearchContext context) {
        if (context instanceof WebDriver) {
            return (WebDriver) context;
        }
        throw new IllegalArgumentException("Cannot get WebDriver from context " + context);
    }

}
