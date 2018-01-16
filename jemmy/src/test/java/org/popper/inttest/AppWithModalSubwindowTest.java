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
import org.popper.inttest.apps.AppWithModalSubwindow;
import org.popper.inttest.pos.AppWithModalSubwindowPo;
import org.popper.inttest.pos.AppWithModalSubwindowPo.Sub;
import org.popper.inttest.pos.AppWithModalSubwindowPo.SubSub;


public class AppWithModalSubwindowTest extends AbstractJemmyTest {
	
	public AppWithModalSubwindowTest() {
		super(AppWithModalSubwindow.class);
	}

	@Test
	public void testInstantiateDialogs() throws Exception {
		AppWithModalSubwindowPo main = factory.createPage(AppWithModalSubwindowPo.class);
		main.openSubButton().click();
		
		Sub sub = factory.createPage(Sub.class);
		sub.openSubSubButton().click();
		
		SubSub subsub = factory.createPage(SubSub.class);
		assertEquals("Subsub", subsub.label().text());
	}
	
	@Test
	public void testGetDialogsAsMember() throws Exception {
		AppWithModalSubwindowPo main = factory.createPage(AppWithModalSubwindowPo.class);
		main.openSubButton().click();
		
		Sub sub = main.subWindow();
		sub.openSubSubButton().click();
		
		SubSub subsub = sub.subSubwindow();
		assertEquals("Subsub", subsub.label().text());
	}
}
