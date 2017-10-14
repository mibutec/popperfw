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
