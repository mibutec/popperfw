package org.popper.inttest;

import junit.framework.Assert;

import org.junit.Test;
import org.popper.fw.element.ILink;
import org.popper.testpos.DeepHierarchyPO;
import org.popper.testpos.HierarchyConstraintPO;
import org.popper.testpos.InnerCssSelectorsPO;

public class HierarchyTest extends AbstractIntTest {
	@Test
	public void testDeepHierarchy() {
		DeepHierarchyPO deepHierarchyPo = factory.createPage(DeepHierarchyPO.class);
		deepHierarchyPo.open();
		ILink link = deepHierarchyPo.footer().inner().moreHierarchy().companyLink();
		Assert.assertEquals("InnerLink", link.text());
	}
	
	@Test
	public void testDeepHierarchyWithCssSelectors() {
		InnerCssSelectorsPO cssSelectorsPo = factory.createPage(InnerCssSelectorsPO.class);
		cssSelectorsPo.open();
		ILink link = cssSelectorsPo.footer().inner().theLink();
		Assert.assertEquals("InnerLink", link.text());
	}
	
	@Test
	public void testHierarchyConstraint() {
		HierarchyConstraintPO hierarchyConstraintPo = factory.createPage(HierarchyConstraintPO.class);
		hierarchyConstraintPo.open();
		
		Assert.assertEquals("Michael", hierarchyConstraintPo.userByCss().forename().text());
		Assert.assertEquals("Bulla", hierarchyConstraintPo.userByCss().lastname().text());
		try {
			hierarchyConstraintPo.userByCss().birthdate().text();
			Assert.fail("Found birthday that should not be found");
		} catch (Exception e) {
			// OK
		}

		Assert.assertEquals("01.01.1970", hierarchyConstraintPo.userById().birthdate().text());
		Assert.assertEquals("King", hierarchyConstraintPo.userById().lastname().text());
		try {
			hierarchyConstraintPo.userById().forename().text();
			Assert.fail("Found birthday that should not be found");
		} catch (Exception e) {
			// OK
		}
	}
	
//	@Test
//	public void testDeepHierarchyWithNotStaticInnerClasses() {
//		DeepHierarchyWithInnerNotStaticClassesPO deepHierarchyPo = factory.createPage(DeepHierarchyWithInnerNotStaticClassesPO.class);
//		deepHierarchyPo.open();
//		ILink link = deepHierarchyPo.footer().inner().moreHierarchy().companyLink();
//		Assert.assertEquals("InnerLink", link.text());
//	}

}
