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

import org.netbeans.jemmy.operators.JMenuBarOperator;
import org.netbeans.jemmy.operators.Operator.DefaultStringComparator;
import org.netbeans.jemmy.operators.Operator.StringComparator;
import org.popper.fw.jemmy.elements.IJemmyMenuBar;

/**
 *
 * Implementation of {@link IJemmyMenuBar}
 *
 * @author Michael
 */
public class DefaultMenuBar extends AbstractJemmyElement<JMenuBarOperator> implements IJemmyMenuBar {
    public DefaultMenuBar(JemmyElementReference reference) {
        super(reference, JMenuBarOperator.class);
    }

    /** (non-Javadoc)
     * @see org.popper.fw.jemmy.elements.IJemmyMenuBar#pushMenuEntry(java.lang.String, java.lang.String)
     */
    @Override
    public void pushMenuEntry(String menuPath) {
        getOperator().pushMenuNoBlock(menuPath, "|", getDefaultStringComparatorForFindingPath());
    }

    private StringComparator getDefaultStringComparatorForFindingPath() {
        StringComparator comp = new DefaultStringComparator(true, false) { //NOSONAR by tina_zemlin: is a really similar and small inner class
            @Override
            public boolean equals(final String caption, final String match) { //NOSONAR by pierre_labuhn on 04.10.2016 11:29:40 method used from Jemmy framework
                return super.equals(caption.replace(" ", ""), match.replace(" ", ""));//remove all spaces
            }
        };
        return comp;
    }

}
