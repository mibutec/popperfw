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
