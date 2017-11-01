package org.popper.inttest;

import org.junit.After;
import org.junit.Before;
import org.omg.CORBA.SystemException;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.jemmy.JemmyContext;

public abstract class AbstractJemmyTest {
    private final Class<?> appClass;

    protected IPoFactory factory;

    protected JemmyContext context;

    protected AbstractJemmyTest(Class<?> appClass) {
        this.appClass = appClass;
    }

    @Before
    public void setup() {
        context = new JemmyContext(appClass);
        context.setRelevantTimeouts(2500);
        factory = context.getFactory();
        context.start();
    }

    @After
    public void stopApplication() throws SystemException {
        context.stop();
    }
}
