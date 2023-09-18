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
import org.popper.fw.element.ILink;
import org.popper.testpos.DeepHierarchyPO;
import org.popper.testpos.HierarchyConstraintPO;
import org.popper.testpos.InnerCssSelectorsPO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HierarchyTest extends AbstractIntTest {
	@Test
	public void testDeepHierarchy() {
		DeepHierarchyPO deepHierarchyPo = factory.createPage(DeepHierarchyPO.class);
		deepHierarchyPo.open();
		ILink link = deepHierarchyPo.footer().inner().moreHierarchy().companyLink();
		assertEquals("InnerLink", link.text());
	}
	
	@Test
	public void testDeepHierarchyWithCssSelectors() {
		InnerCssSelectorsPO cssSelectorsPo = factory.createPage(InnerCssSelectorsPO.class);
		cssSelectorsPo.open();
		ILink link = cssSelectorsPo.footer().inner().theLink();
		assertEquals("InnerLink", link.text());
	}
	
	@Test
	public void testHierarchyConstraint() {
		HierarchyConstraintPO hierarchyConstraintPo = factory.createPage(HierarchyConstraintPO.class);
		hierarchyConstraintPo.open();
		
		assertEquals("Michael", hierarchyConstraintPo.userByCss().forename().text());
		assertEquals("Bulla", hierarchyConstraintPo.userByCss().lastname().text());
		try {
			hierarchyConstraintPo.userByCss().birthdate().text();
			fail("Found birthday that should not be found");
		} catch (Exception e) {
			// OK
		}

		assertEquals("01.01.1970", hierarchyConstraintPo.userById().birthdate().text());
		assertEquals("King", hierarchyConstraintPo.userById().lastname().text());
		try {
			hierarchyConstraintPo.userById().forename().text();
			fail("Found birthday that should not be found");
		} catch (Exception e) {
			// OK
		}
	}
	
//	@Test
//	public void testDeepHierarchyWithNotStaticInnerClasses() {
//		DeepHierarchyWithInnerNotStaticClassesPO deepHierarchyPo = factory.createPage(DeepHierarchyWithInnerNotStaticClassesPO.class);
//		deepHierarchyPo.open();
//		ILink link = deepHierarchyPo.footer().inner().moreHierarchy().companyLink();
//		assertEquals("InnerLink", link.text());
//	}

}
