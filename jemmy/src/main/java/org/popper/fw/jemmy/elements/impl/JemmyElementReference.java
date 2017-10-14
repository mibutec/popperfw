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
package org.popper.fw.jemmy.elements.impl;

import org.netbeans.jemmy.ComponentChooser;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.jemmy.JemmyContext;

public class JemmyElementReference {
	private final JemmyContext context;
	
	/**
	 * Name of that element for logging and error-handling reasons
	 */
	private final String name;
	
	/**
	 * parent of this element
	 */
	private final PageObjectImplementation parent;
	
	/**
	 * Locator of this element
	 */
	private final ComponentChooser by;
	
	public JemmyElementReference(String name, PageObjectImplementation parent, ComponentChooser by, JemmyContext context) {
		this.name = name;
		this.parent = parent;
		this.by = by;
		this.context = context;
	}

	public JemmyContext getContext() {
		return context;
	}

	public String getName() {
		return name;
	}

	public PageObjectImplementation getParent() {
		return parent;
	}

	public ComponentChooser getBy() {
		return by;
	}
}
