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
package org.popper.forge.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.popper.forge.DefaultMethodAnalyzer;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.IMethodAnalyzer;

/**
 * By default a blank class is analyzed by {@link DefaultMethodAnalyzer}. This one looks at the annotations on a method
 * to find the {@link IAnnotationProcessor} to use.
 * 
 * You may want to write an own rules to for example find {@link IAnnotationProcessor} via method names. The {@link IMethodAnalyzer}
 * to use can be linked via {@link AnalyzedBy} on a blank
 * 
 * @author michael_bulla
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AnalyzedBy {
	public Class<? extends IMethodAnalyzer>[] value();
}
