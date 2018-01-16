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

import java.util.HashMap;
import java.util.Map;

import org.popper.fw.webdriver.annotations.Page;

@Page(name="Abstract PO")
public abstract class AbstractClassImplementingPO implements DefaultAnnotationsPO {
	public Map<String, String> getLabels() {
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("id", idLocatorLabel().text());
		ret.put("xpath", xpathLocatorLabel().text());
		ret.put("class", classLocatorLabel().text());
		
		return ret;
	}
}
