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
package org.popper.fw.jemmy.annotations.locator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.annotations.RunBefore;
import org.popper.fw.impl.PageObjectImplementation;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.interfaces.ReEvalutateException;
import org.popper.fw.jemmy.JemmyContext;
import org.popper.fw.jemmy.JemmyPageObjectHelper;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImplementedBy(DialogLocatorProcessor.class)
@RunBefore(Locator.class)
public @interface Window {
    String name() default "";
}

/**
 * {@link IAnnotationProcessor} processing {@link Window}-annotation
 *
 * @author Michael
 *
 */
class DialogLocatorProcessor implements IAnnotationProcessor<Window, Object> {

    private final JemmyContext context;

    public DialogLocatorProcessor(JemmyContext context) {
        super();
        this.context = context;
    }

    @Override
    public Object processAnnotation(Window dialogLocator, LocatorContextInformation info, Object lastResult)
            throws ReEvalutateException {
        Class<?> type = info.getMethod().getReturnType();
        PageObjectImplementation poi = JemmyPageObjectHelper.createWindowPageObjectImplementation(type,
                info.getMethod().getName(), context, null);

        return context.getFactory().createProxy(type, poi);
    }
}