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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.popper.inttest.apps.SimpleApp;
import org.popper.inttest.pos.SimpleAppPo;

public class SimpleAppTest extends AbstractJemmyTest {

    public SimpleAppTest() {
        super(SimpleApp.class);
    }

    @Test
    public void someTest() {
        SimpleAppPo firstJemmyPo = factory.createPage(SimpleAppPo.class);

        assertEquals("0", firstJemmyPo.cntLabel().getText());
        firstJemmyPo.plusButton().click();
        firstJemmyPo.plusButtonAsIButton().click();
        firstJemmyPo.plusButton().click();
        assertEquals("3", firstJemmyPo.cntLabel().getText());
        assertEquals("Some Bla", firstJemmyPo.someBlaLabel().text());
    }

    @Test
    @Ignore
    public void notExistingIsFast() {
        SimpleAppPo firstJemmyPo = factory.createPage(SimpleAppPo.class);
        long start = System.currentTimeMillis();
        assertFalse(firstJemmyPo.notExistingLabel().isDisplayed());
        long diff = System.currentTimeMillis() - start;
        assertTrue("Expected not finding an element to be faster than 300 ms, but was " + diff + " ms", diff < 300);
    }
}
