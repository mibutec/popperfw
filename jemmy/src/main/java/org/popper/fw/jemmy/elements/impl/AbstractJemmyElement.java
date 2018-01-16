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
package org.popper.fw.jemmy.elements.impl;

import java.awt.Container;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.TimeoutExpiredException;
import org.netbeans.jemmy.operators.ComponentOperator;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.popper.fw.jemmy.JemmyContext;
import org.popper.fw.jemmy.JemmyPageObjectHelper.SearchContextProvider;
import org.popper.fw.jemmy.elements.IJemmyElement;
import org.popper.fw.jemmy.elements.IJemmyLabel;

/**
 * Abstract Superclass for all Elements in an application that can be interacted with. Interaction may mean
 * readonly actions (see {@link IJemmyLabel}) or read-write action (see {@link AbstractJemmyInput}).
 *
 * This class is responsible to handle location of elements and handle errormessages (with help of the
 * getElement-method).
 *
 * When implementing a new Jemmy Element, it should be a subclass of {@link AbstractJemmyElement}.
 *
 * @author Michael
 *
 */
public abstract class AbstractJemmyElement<T extends ComponentOperator> implements IJemmyElement {

    protected final JemmyElementReference reference;

    protected final Class<T> operatorType;
    
    protected AbstractJemmyElement(JemmyElementReference reference, Class<T> operatorType) {
        this.reference = reference;
        this.operatorType = operatorType;
    }

    protected JemmyContext getContext() {
        return reference.getContext();
    }

    @Override
    public T getOperator() {
        try {
            Constructor<T> constructor = operatorType.getConstructor(ContainerOperator.class, ComponentChooser.class);
            ContainerOperator parent = reference.getParent().getExtension(SearchContextProvider.class)
                    .getSearchContext();

            T operator = constructor.newInstance(parent, reference.getBy());
            reference.getContext().flashComponent(operator);
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

    public T getOperatorFast() {
        int oldTimeout = reference.getContext().getRelevantTimeouts();
        try {
            reference.getContext().setRelevantTimeouts(1000);
            return getOperator();
        } finally {
            reference.getContext().setRelevantTimeouts(oldTimeout);
        }
    }
    
    /**
     * Convenience-method
     * @param timeInMillis time to sleep in Milliseconds
     */
    protected void sleep(long timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            // nothing to do
        }
    }

    /*
     * (non-Javadoc)
     * @see org.popper.fw.element.IElement#isDisplayed()
     */
    @Override
    public boolean isDisplayed() {
        try {
            return isDisplayedRecursivly(getOperatorFast());
        } catch (TimeoutExpiredException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isDisplayedRecursivly(ComponentOperator op) {
        boolean isVisible = op.isVisible();
        Container parent = op.getParent();
        if (parent == null) {
            return isVisible;
        } else {
            return isVisible && isDisplayedRecursivly(new ContainerOperator(parent));
        }
    }

    /*
     * (non-Javadoc)
     * @see org.popper.fw.element.IElement#isSelected()
     */
    @Override
    public boolean isSelected() {
        throw new IllegalStateException("method not implemented");
    }
}
