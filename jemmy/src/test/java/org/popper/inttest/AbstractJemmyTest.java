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

import org.junit.After;
import org.junit.Before;
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
    public void stopApplication() {
        context.stop();
    }
}
