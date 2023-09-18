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

import org.junit.jupiter.api.Test;
import org.popper.fw.element.ILabel;
import org.popper.testpos.FramesetPO;
import org.popper.testpos.PageWithIframePO;
import org.popper.testpos.TablePO.User;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FrameTest extends AbstractIntTest {

	@Test
	public void testSimpleIframe() {
		PageWithIframePO pageWithIframe = factory.createPage(PageWithIframePO.class);
		pageWithIframe.open();
		assertEquals("idLocator", pageWithIframe.iframeById().idLocatorLabel().text());
		assertEquals("idLocator", pageWithIframe.iframeByCss().idLocatorLabel().text());
		assertEquals("idLocator", pageWithIframe.iframeByXpath().idLocatorLabel().text());
	}
	
	@Test
	public void testFrameset() {
		FramesetPO frameset = factory.createPage(FramesetPO.class);
		frameset.open();
		
		// test access to frame 1
		User user1 = frameset.table().userById("1");
		assertEquals("Bulla", user1.lastname().text());
		assertEquals("Michael", user1.firstname().text());
		assertEquals("01.01.1970", user1.birthdate().text());
		
		// test access to frame 2 containing another frame
		assertEquals("idLocator", frameset.iframePage().iframeById().idLocatorLabel().text());
	}
	
	@Test
	public void testAccessElementDirectlyFromFrame() {
		PageWithIframePO pageWithIframe = factory.createPage(PageWithIframePO.class);
		pageWithIframe.open();
		assertEquals("idLocator", pageWithIframe.directFrameAccess().text());
	}
	
	@Test
	public void testUseSavedPoReferences() {
		FramesetPO frameset = factory.createPage(FramesetPO.class);
		frameset.open();
		
		// create references to several frames
		User userFromOneFrame = frameset.table().userById("1");
		ILabel labelFromAnotherFrame = frameset.iframePage().iframeById().idLocatorLabel();
		
		// mix calls to that frame references
		assertEquals("Bulla", userFromOneFrame.lastname().text());
		assertEquals("idLocator", labelFromAnotherFrame.text());
		assertEquals("Michael", userFromOneFrame.firstname().text());
		assertEquals("idLocator", frameset.iframePage().iframeById().idLocatorLabel().text());
		assertEquals("idLocator", labelFromAnotherFrame.text());
		assertEquals("01.01.1970", userFromOneFrame.birthdate().text());
	}
}
