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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.popper.fw.webdriver.annotations.Page;
import org.popper.fw.webdriver.elements.impl.DefaultLabel;
import org.popper.fw.webdriver.elements.impl.WebElementReference;
import org.popper.testpos.AbstractClassImplementingPO;
import org.popper.testpos.AbstractClassPO;
import org.popper.testpos.DeepHierarchywithFailurePO;
import org.popper.testpos.DefaultAnnotationsPO;
import org.popper.testpos.OpenedSitePO;
import org.popper.testpos.VerifyFailedPagePO;
import org.popper.testpos.VerifyPagePO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class AnnotationTest extends AbstractIntTest {

	@Test
	public void testDefaultAnnotations() {
		DefaultAnnotationsPO defaultAnnotationPo = factory.createPage(DefaultAnnotationsPO.class);
		defaultAnnotationPo.open();
		assertEquals("idLocator", defaultAnnotationPo.idLocatorLabel().text());
		assertEquals("classLocator", defaultAnnotationPo.classLocatorLabel().text());
		assertEquals("xpathLocator", defaultAnnotationPo.xpathLocatorLabel().text());
		assertContains(defaultAnnotationPo.getHtml(), "Lorem Ipsum");
		assertEquals("any Title", defaultAnnotationPo.getTitle());
	}

	@Test
	public void testOpen() {
		OpenedSitePO opener = factory.createPage(OpenedSitePO.class);
		opener.open();
		assertEndsWith(opener.hrefLabel().text(), "open.html");
		opener.open("user1", "password1");
		assertEndsWith(opener.hrefLabel().text(), "open.html?name=user1&password=password1");
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("p1", "v1");
		opener.openMapSimple(parameters);
		assertEndsWith(opener.hrefLabel().text(), "open.html?p1=v1");
		
		opener.openMapWithExistingParameters(parameters);
		assertEndsWith(opener.hrefLabel().text(), "open.html?para1=val1&p1=v1");

		parameters.put("p2", "v2");
		opener.openMapWithExistingParameters(parameters);
		assertEquals(2, opener.hrefLabel().text().split("\\?").length);
		assertContains(opener.hrefLabel().text(), "open.html?para1=val1");
		assertContains(opener.hrefLabel().text(), "p1=v1");
		assertContains(opener.hrefLabel().text(), "p2=v2");
		
		opener.openMapWithUriFromParent(parameters);
		assertEquals(2, opener.hrefLabel().text().split("\\?").length);
		assertContains(opener.hrefLabel().text(), "open.html?");
		assertContains(opener.hrefLabel().text(), "p1=v1");
		assertContains(opener.hrefLabel().text(), "p2=v2");
	}
	
	@Test
	public void testErrormessage() {
		DeepHierarchywithFailurePO poWithFailure = factory.createPage(DeepHierarchywithFailurePO.class);
		poWithFailure.open();
		try {
			poWithFailure.footer().inner().moreHierarchy().companyLink().click();
			fail("Failure in Locator should lead to Exception");
		} catch (RuntimeException re) {
			assertEquals("Object not found: Login -> Footer (By.id: footer) -> Inner (By.id: inner) -> More Hierarchy (By.cssSelector: .moreHierarchy) -> Company (By.id: wrongId)", re.getMessage());
		}
	}
	
	@Test
	public void testAbstractClass() {
		AbstractClassPO abstractClassPo = factory.createPage(AbstractClassPO.class);
		abstractClassPo.open();
		Map<String, String> labels = abstractClassPo.getLabels();
		assertEquals("idLocator", labels.get("id"));
		assertEquals("classLocator", labels.get("class"));
		assertEquals("xpathLocator", labels.get("xpath"));
		
		AbstractClassImplementingPO abstractImplClassPo = factory.createPage(AbstractClassImplementingPO.class);
		labels = abstractImplClassPo.getLabels();
		assertEquals("idLocator", labels.get("id"));
		assertEquals("classLocator", labels.get("class"));
		assertEquals("xpathLocator", labels.get("xpath"));
	}
	
	@Test
	public void testVerifyTitle() {
		VerifyPagePO verifyPage = factory.createPage(VerifyPagePO.class);
		verifyPage.open();
		assertEquals("alles OK", verifyPage.message().text());
		
		VerifyFailedPagePO verifyFailedPage = factory.createPage(VerifyFailedPagePO.class);
		
		try {
			verifyFailedPage.open();
			verifyFailedPage.message();
			fail("verify Page should return an error");
		} catch (Exception e) {
			// alles OK
		}
	}
	
	@Test
	public void verifyAnnotationsAreInheritedOnMethods() {
		InheritedPo inheritedPo = factory.createPage(InheritedPo.class);
		inheritedPo.open();
		assertEquals(inheritedPo.idLocatorLabel().text(), "idLocator");
		assertEquals(inheritedPo.xpathLocatorLabel().text(), "xpathLocator");
	}
	
	@Test
	public void verifyTypeAnnotationsAreInheritedFromParentInterface() {
		InheritedPoInheritingAnnotationFromParentInterface inheritedPo = factory.createPage(InheritedPoInheritingAnnotationFromParentInterface.class);
		inheritedPo.open();
	}
	
	@Page
	public static interface InheritedPo extends DefaultAnnotationsPO {
		ExtendedDefaultLabel idLocatorLabel();
		
		public static class ExtendedDefaultLabel extends DefaultLabel {
			public ExtendedDefaultLabel(WebElementReference reference) {
				super(reference);
			}
		}
	}
	
	public static interface InheritedPoInheritingAnnotationFromParentInterface extends DefaultAnnotationsPO {
	}

}
