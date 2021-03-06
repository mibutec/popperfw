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

import org.popper.fw.element.ILink;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.PageAccessor;
import org.popper.fw.webdriver.annotations.locator.Locator;

@Page(name="Login")
public interface DeepHierarchyPO {
	@PageAccessor(uri="/pages/deepHierarchy.html")
	void open();
	
	@Locator(name="Footer", id="footer")
	Footer footer();

	public static interface Footer {
		@Locator(name="Inner", id="inner")
		Inner inner();
		
		public static interface Inner {
			@Locator(name="More Hierarchy", cssSelector=".moreHierarchy")
			MoreHierarchy moreHierarchy();
			
			public static interface MoreHierarchy {
				@Locator(name="Company", id="companyLink")
				ILink companyLink();
			}
		}
	}
}



