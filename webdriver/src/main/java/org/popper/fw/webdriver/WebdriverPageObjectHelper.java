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

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.webdriver.annotations.Frame.FramePoExtension;

public class WebdriverPageObjectHelper {
	public static PageObjectImplementation createPageObjectImplementation(Class<?> basicClass, By searchContextBy, String name,
			PageObjectImplementation parent, WebdriverContext context, SearchContext searchContext) {
		PageObjectImplementation poi = new PageObjectImplementation(context, basicClass, name, parent);
		poi.addExtension(new WebdriverPoExtension(context, searchContext, searchContextBy));
		
		return poi;
	}
	
	public static SearchContext getSearchContext(PageObjectImplementation poi) {
		openFrame(poi);
		
		WebdriverPoExtension ext = poi.getExtension(WebdriverPoExtension.class);
		if (ext.searchContext != null) {
			return ext.searchContext;
		}

		if (poi.getParent() == null || ext.searchContextBy == null) {
			return ext.context.getDriver();
		}

		return ext.context.getElement(poi.getName(), ext.searchContextBy, poi.getParent());
	}
	
	/**
	 * Iterate throw the parent hierarchie and open the last element with a Frame annotation,
	 * otherwise open defaultContent
	 */
	private static void openFrame(PageObjectImplementation poi) {
		FramePoExtension poExt = poi.getExtension(FramePoExtension.class);
		By frameToCall = poExt == null ? null : poExt.frameToCall;
		WebdriverContext context = poi.getExtension(WebdriverPoExtension.class).context;
		
		if (frameToCall != null) {
			WebElement frame = context.getElement("Frame", frameToCall, poi.getParent());
			context.getDriver().switchTo().frame(frame);
		} else if (poi.getParent() == null) {
			context.getDriver().switchTo().defaultContent();
		} else {
			openFrame(poi.getParent());
		}
	}
	
	public static class WebdriverPoExtension {
		public final WebdriverContext context;

		public final SearchContext searchContext;
		
		public final By searchContextBy;

		protected WebdriverPoExtension(WebdriverContext context, SearchContext searchContext, By searchContextBy) {
			super();
			this.context = context;
			this.searchContext = searchContext;
			this.searchContextBy = searchContextBy;
		}
	}
}
