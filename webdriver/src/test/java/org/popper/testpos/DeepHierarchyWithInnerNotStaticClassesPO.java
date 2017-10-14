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

package org.popper.testpos;

import org.popper.fw.element.ILink;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.annotations.PageAccessor;
import org.popper.fw.webdriver.annotations.locator.Locator;

@Page(name="Login")
public abstract class DeepHierarchyWithInnerNotStaticClassesPO {
	@PageAccessor(uri="/pages/deepHierarchy.html")
	public abstract void open();
	
	@Locator(name="Footer", id="footer")
	public abstract Footer footer();

	public abstract class Footer {
		@Locator(name="Inner", id="inner")
		public abstract Inner inner();
		
		public abstract class Inner {
			@Locator(name="More Hierarchy", cssSelector=".moreHierarchy")
			public abstract MoreHierarchy moreHierarchy();
			
			public abstract class MoreHierarchy {
				@Locator(name="Company", id="companyLink")
				public abstract ILink companyLink();
			}
		}
	}
}



