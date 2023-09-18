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
import org.popper.testpos.DefaultAnnotationsWithParametersPO;
import org.popper.testpos.DefaultAnnotationsWithParametersShortcutPO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterTest extends AbstractIntTest {
	@Test
	public void testDefaultAnnotationsWithParameters() {
		DefaultAnnotationsWithParametersPO defaultAnnotationPo = factory.createPage(DefaultAnnotationsWithParametersPO.class);
		defaultAnnotationPo.open();
		assertEquals("idLocator", defaultAnnotationPo.idLocatorLabel("id", "cator").text());
		assertEquals("xpathLocator", defaultAnnotationPo.xpathLocatorLabel("name", "xpathLocator").text());
		assertEquals("classLocator", defaultAnnotationPo.classLocatorLabel("cator", "class").text());
	}

	@Test
	public void testDefaultAnnotationsWithParametersShortcut() {
		DefaultAnnotationsWithParametersShortcutPO defaultAnnotationPo = factory.createPage(DefaultAnnotationsWithParametersShortcutPO.class);
		defaultAnnotationPo.open();
		assertEquals("idLocator", defaultAnnotationPo.idLocatorLabel("id", "cator").text());
		assertEquals("xpathLocator", defaultAnnotationPo.xpathLocatorLabel("name", "xpathLocator").text());
		assertEquals("classLocator", defaultAnnotationPo.classLocatorLabel("class", "cator").text());
	}

    @Test
    public void testDefaultMethodWithParameters() {
        DefaultAnnotationsWithParametersShortcutPO defaultAnnotationPo = factory
                .createPage(DefaultAnnotationsWithParametersShortcutPO.class);
        assertEquals("ok", defaultAnnotationPo.returnSomethingWithParameters("param"));
    }

    @Test
    public void testDefaultMethodWithoutParameters() {
        DefaultAnnotationsWithParametersShortcutPO defaultAnnotationPo = factory
                .createPage(DefaultAnnotationsWithParametersShortcutPO.class);
        assertEquals("ok", defaultAnnotationPo.returnSomethingWithoutParameters());
    }

    @Test
    public void testDefaultVoidMethodWithParameters() {
        DefaultAnnotationsWithParametersShortcutPO defaultAnnotationPo = factory
                .createPage(DefaultAnnotationsWithParametersShortcutPO.class);
        // should not throw an exception
        defaultAnnotationPo.doSomethingWithParameters("param");
    }

    @Test
    public void testDefaultVoidMethodWithoutParameters() {
        DefaultAnnotationsWithParametersShortcutPO defaultAnnotationPo = factory
                .createPage(DefaultAnnotationsWithParametersShortcutPO.class);
        // should not throw an exception
        defaultAnnotationPo.doSomethingWithoutParameters();
    }
}
