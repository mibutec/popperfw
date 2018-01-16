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
package org.popper.fw.jemmy.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.netbeans.jemmy.TimeoutExpiredException;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.interfaces.ReEvalutateException;
import org.popper.fw.jemmy.JemmyContext;
import org.popper.fw.jemmy.JemmyPageObjectHelper.SearchContextProvider;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(DoesExistProcessor.class)
public @interface DoesExist {

}

class DoesExistProcessor implements IAnnotationProcessor<DoesExist, Boolean> {

    private final JemmyContext context;

    public DoesExistProcessor(JemmyContext context) {
        super();
        this.context = context;
    }

    @Override
    public Boolean processAnnotation(DoesExist dialogLocator, LocatorContextInformation info, Boolean lastResult)
            throws ReEvalutateException {
        int oldTimeout = context.getRelevantTimeouts();
        try {
            context.setRelevantTimeouts(0);
            ContainerOperator op = info.getParent().getExtension(SearchContextProvider.class).getSearchContext();
            return op.isShowing() && op.isVisible();
        } catch (TimeoutExpiredException e) {
            return false;
        } finally {
            context.setRelevantTimeouts(oldTimeout);
        }
    }
}