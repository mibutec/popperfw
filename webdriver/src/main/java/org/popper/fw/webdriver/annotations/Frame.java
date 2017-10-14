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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.webdriver.WebdriverContext;
import org.popper.fw.webdriver.WebdriverPageObjectHelper;
import org.popper.fw.webdriver.annotations.Frame.FrameImpl;
import org.popper.fw.webdriver.annotations.locator.AbstractLocatorAnnotationProcessor;
import org.popper.fw.webdriver.annotations.locator.LocatorAnnotationProcessor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(FrameImpl.class)
public @interface Frame {
	public String xpath() default "";
	public String cssSelector() default "";
	public String id() default "";
	
	public static class FrameImpl extends AbstractLocatorAnnotationProcessor<Frame> {

		public FrameImpl(WebDriver webdriver, WebdriverContext context, IPoFactory factory) {
			super(webdriver, context, factory);
		}

		@Override
		public Object processAnnotation(Frame frameAnnotation, LocatorContextInformation info, Object lastResult) {
			By frameBy = LocatorAnnotationProcessor.createBy(frameAnnotation.cssSelector(), frameAnnotation.xpath(),
					frameAnnotation.id(), info.getParameters());			
			PageObjectImplementation framePO = WebdriverPageObjectHelper.createPageObjectImplementation(PageObjectImplementation.class, null, "Frame", info.getParent(), context,
					null);
			framePO.addExtension(new FramePoExtension(frameBy));
			
			return context.getFactory().createProxy(info.getMethod().getReturnType(), framePO);
//			Object ret = framePO;
//			if (info.getMethod().getAnnotation(Locator.class) == null) {
//				ret = context.getFactory().createProxy(info.getMethod().getReturnType(), framePO);
//			}
//			return ret;
		}

		@Override
		protected By getBy(Frame annotation, LocatorContextInformation info) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public static class FramePoExtension {
		public final By frameToCall;

		FramePoExtension(By frameToCall) {
			super();
			this.frameToCall = frameToCall;
		}
	}
}

