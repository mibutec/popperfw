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
package org.popper.inttest;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.popper.inttest.apps.AppWithPanel;
import org.popper.inttest.pos.AppWithPanelPo;


public class AppWithPanelTest extends AbstractJemmyTest {
	
	public AppWithPanelTest() {
		super(AppWithPanel.class);
	}

	@Test
	public void someTest() {
		AppWithPanelPo firstJemmyPo = factory.createPage(AppWithPanelPo.class);
		assertEquals("Label 1", firstJemmyPo.outer1().text());
		assertEquals("Label 2", firstJemmyPo.outer2().text());
		assertEquals("inner Label 1", firstJemmyPo.panel().inner1().text());
		assertEquals("inner Label 2", firstJemmyPo.panel().inner2().text());
	}
}
