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

        assertEquals("0", firstJemmyPo.cntLabel().text());
        firstJemmyPo.plusButton().click();
        firstJemmyPo.plusButtonAsIButton().click();
        firstJemmyPo.plusButton().click();
        assertEquals("3", firstJemmyPo.cntLabel().text());
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
