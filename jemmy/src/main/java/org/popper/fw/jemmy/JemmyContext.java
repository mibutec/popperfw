/*
 * Copyright [2013] [Michael Bulla, michaelbulla@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.popper.fw.jemmy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.QueueTool;
import org.netbeans.jemmy.TestOut;
import org.netbeans.jemmy.Timeouts;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.Operator;
import org.netbeans.jemmy.operators.WindowOperator;
import org.popper.fw.impl.AbstractPopperContext;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.interfaces.IPoFactory;
import org.popper.fw.jemmy.JemmyPageObjectHelper.SearchContextProvider;

/**
 * Implementation of {@link AbstractPopperContext} for usage with Jemmy
 *
 * @author Michael Bulla
 */
public class JemmyContext extends AbstractPopperContext {
    private static final String[] RELEVANT_TIMEOUTS = new String[] {"DialogWaiter.WaitDialogTimeout",
            "FrameWaiter.WaitFrameTimeout", "Waiter.WaitingTime", "WindowWaiter.WaitWindowTimeout",
            "ComponentOperator.WaitComponentTimeout",};

    private final Class<?> mainDialogClass;

    private WindowOperator containerOperator;

    private int relevantTimeouts = 3000;

    private int flashTimeout = -1;

    public JemmyContext(Class<?> mainDialogClass) {
        this.mainDialogClass = mainDialogClass;
    }

    public void setRelevantTimeouts(int timeout) {
        relevantTimeouts = timeout;
        for (String timeoutName : RELEVANT_TIMEOUTS) {
            Timeouts.setDefault(timeoutName, timeout);
        }
    }

    public int getRelevantTimeouts() {
        return relevantTimeouts;
    }

    public void start() {
        JemmyProperties.setCurrentOutput(TestOut.getNullOutput());
        setDefaultElementFactory(new JemmyDefaultElementFactory());

        try {
            new ClassReference(mainDialogClass.getName()).startApplication();
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        containerOperator = new JFrameOperator();
    }

    public void stop() {
        for (Window window : Window.getWindows()) {
            if (window.getClass() == mainDialogClass) {
                window.dispose();
            }
        }

        containerOperator = null;
    }

    public void flashComponent(Operator operator) {
        if (flashTimeout >= 0) {
            Component comp = operator.getSource();
            Color oldcolor = comp.getBackground();
            comp.setBackground(new Color(255, 0, 0));
            try {
                Thread.sleep(flashTimeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            comp.setBackground(oldcolor);
        }
    }

    public boolean isStarted() {
        return containerOperator != null;
    }

    public void waitForEvents() {
        try {
            while (!QueueTool.checkEmpty()) {
                if (SwingUtilities.isEventDispatchThread()) {
                    //if we are currently running on the event dispatch thread, we cannot wait until the queue is empty
                    break;
                }
                // All queue entries will be executed; the current thread will wait until the one we have passed in has finished:
                SwingUtilities.invokeAndWait(() -> {
                });

                // Now check whether there are still queue entries - this may happen, if more were created by the queue
                // entries that were executed just now:
                Thread.sleep(10);

                // Don't wait for ActionProducer, often you end up waiting until the timeout (60 secs) occurs.
                // Such an ActionProducer thread waits until the according action is completed.
                // If a view is opened via JemmyMenuView, the producer is in place until the view is closed again,
                // e.g. the exceptions view (deviation view) can be open for a long time.
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public<T extends Operator> T createOperator(Class<T> type, PageObjectImplementation poi, ComponentChooser chooser) {
    	try {
            Constructor<T> constructor = type.getConstructor(ContainerOperator.class, ComponentChooser.class);
            ContainerOperator parent = poi.getExtension(SearchContextProvider.class)
                    .getSearchContext();

            T operator = constructor.newInstance(parent, chooser);
            return operator;
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getTargetException();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new RuntimeException(cause);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolveStaticClass(Class<T> clazz) {
        if (JemmyContext.class.isAssignableFrom(clazz)) {
            return (T) this;
        } else if (IPoFactory.class.isAssignableFrom(clazz)) {
            return (T) getFactory();
        } else {
            return null;
        }
    }

    public WindowOperator getRoot() {
        return containerOperator;
    }

    @Override
    public JemmyPoFactory getFactory() {
        return new JemmyPoFactory(this);
    }

    public int getFlashTimeout() {
        return flashTimeout;
    }

    public void setFlashTimeout(int flashTimeout) {
        this.flashTimeout = flashTimeout;
    }
}
