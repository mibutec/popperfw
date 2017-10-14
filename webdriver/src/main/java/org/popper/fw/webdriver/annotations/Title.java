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

package org.popper.fw.webdriver.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.WebDriver;
import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.webdriver.annotations.Title.TitleImpl;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(TitleImpl.class)
public @interface Title {
	// marker-annotation
	
	public static class TitleImpl implements IAnnotationProcessor<Title, String> {
		private final WebDriver webDriver;
		
		public TitleImpl(WebDriver webDriver) {
			this.webDriver = webDriver;
		}

		@Override
		public String processAnnotation(Title titleAnnotation, LocatorContextInformation info, String lastResult) {
			return webDriver.getTitle();
		}
	}
}

