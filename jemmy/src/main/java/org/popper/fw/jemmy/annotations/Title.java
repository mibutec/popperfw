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
package org.popper.fw.jemmy.annotations;

import java.awt.Container;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.interfaces.ReEvalutateException;
import org.popper.fw.jemmy.JemmyPageObjectHelper.SearchContextProvider;

/**
 * Returns the title of a Window
 * @author michael_bulla
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ImplementedBy(TitleAnnotationProcessor.class)
public @interface Title {

}

class TitleAnnotationProcessor implements IAnnotationProcessor<Title, String> {

    /** (non-Javadoc)
     * @see org.popper.fw.interfaces.IAnnotationProcessor#processAnnotation(java.lang.annotation.Annotation, org.popper.fw.interfaces.LocatorContextInformation, java.lang.Object)
     */
    @Override
    public String processAnnotation(Title locatorAnnotation, LocatorContextInformation info, String lastResult)
            throws ReEvalutateException {
        Container container = (Container) info.getParent().getExtension(SearchContextProvider.class).getSearchContext()
                .getSource();

        if (container instanceof JFrame) {
            return ((JFrame) container).getTitle();
        } else if (container instanceof JDialog) {
            return ((JDialog) container).getTitle();
        } else {
            throw new IllegalStateException("@" + Title.class.getSimpleName()
                    + " may only be annotated on POs representing a JDialog or JFrame");
        }
    }
}