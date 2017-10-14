package org.popper.inttest;

import junit.framework.Assert;

import org.junit.Test;
import org.popper.testpos.DefaultAnnotationsWithParametersPO;
import org.popper.testpos.DefaultAnnotationsWithParametersShortcutPO;

public class ParameterTest extends AbstractIntTest {
	@Test
	public void testDefaultAnnotationsWithParameters() {
		DefaultAnnotationsWithParametersPO defaultAnnotationPo = factory.createPage(DefaultAnnotationsWithParametersPO.class);
		defaultAnnotationPo.open();
		Assert.assertEquals("idLocator", defaultAnnotationPo.idLocatorLabel("id", "cator").text());
		Assert.assertEquals("xpathLocator", defaultAnnotationPo.xpathLocatorLabel("name", "xpathLocator").text());
		Assert.assertEquals("classLocator", defaultAnnotationPo.classLocatorLabel("cator", "class").text());
	}

	@Test
	public void testDefaultAnnotationsWithParametersShortcut() {
		DefaultAnnotationsWithParametersShortcutPO defaultAnnotationPo = factory.createPage(DefaultAnnotationsWithParametersShortcutPO.class);
		defaultAnnotationPo.open();
		Assert.assertEquals("idLocator", defaultAnnotationPo.idLocatorLabel("id", "cator").text());
		Assert.assertEquals("xpathLocator", defaultAnnotationPo.xpathLocatorLabel("name", "xpathLocator").text());
		Assert.assertEquals("classLocator", defaultAnnotationPo.classLocatorLabel("class", "cator").text());
	}
}
