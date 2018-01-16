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
package org.popper.fw.webdriver.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.impl.ReflectionsUtil;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.webdriver.WebdriverContext;
import org.popper.fw.webdriver.annotations.PageAccessor.PageAccessorImpl;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(PageAccessorImpl.class)
public @interface PageAccessor {
	public String uri() default "";

	public String absoluteUri() default "";
	
	public static class PageAccessorImpl implements IAnnotationProcessor<PageAccessor, Void> {
		protected final Logger log = Logger.getLogger(getClass());

		private WebdriverContext context;

		public PageAccessorImpl(WebdriverContext context) {
			this.context = context;
		}

		@Override
		public Void processAnnotation(PageAccessor pageAccessorAnnotation, LocatorContextInformation info, Void lastResult) {
			Object[] args = info.getParameters();
			if (!handlePageAccessor(pageAccessorAnnotation, args)) {
				List<PageAccessor> classAnnotations = ReflectionsUtil.getAnnotations(info.getBasicClass(), PageAccessor.class);
				for (Annotation annotation : classAnnotations) {
					if (annotation instanceof PageAccessor) {
						if (handlePageAccessor((PageAccessor) annotation, args)) {
							return null;
						}
					}
				}
				throw new RuntimeException(
						"There must be set uri or absoluteUri at @PageAccessor-annotation on method or class-level");
			} else {
				return null;
			}
		}
		
		private boolean handlePageAccessor(PageAccessor pageAccessor, Object[] args) {
			if (pageAccessor == null) {
				return false;
			}
			
			String relUri = pageAccessor.uri();
			if (!StringUtils.isEmpty(relUri)) {
				context.openRelativeUri(handleUriParameters(relUri, args));
				return true;
			} else if (!StringUtils.isEmpty(pageAccessor.absoluteUri())) {
				log.debug("opening url: " + pageAccessor.absoluteUri());
				context.getDriver().get(handleUriParameters(pageAccessor.absoluteUri(), args));
				return true;
			}
			
			return false;
		}
		
		private String handleUriParameters(String uri, Object[] parameters) {
			if (parameters != null && parameters.length == 1 && parameters[0] != null && Map.class.isAssignableFrom(parameters[0].getClass())) {
				Map<?, ?> parameterMap = (Map<?, ?>) parameters[0];
				String paraString = "";
				for (Entry<?,?> entry : parameterMap.entrySet()) {
					String thisPara = "";
					if (paraString.length() > 0) {
						thisPara = "&";
					}
					
					thisPara += entry.getKey() + "=" + entry.getValue();
					paraString += thisPara;
				}
				
				if (paraString.length() > 0) {
					if (!uri.contains("?")) {
						uri += "?";
					} else {
						uri += "&";
					}
				}
				
				uri += paraString;
				return uri;
			}
			return WebdriverContext.replaceVariables(uri, parameters);
		}
	}
}

