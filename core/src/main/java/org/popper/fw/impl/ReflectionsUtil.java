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

package org.popper.fw.impl;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class
 * 
 * @author michael_bulla
 *
 */
public class ReflectionsUtil {
	private ReflectionsUtil() {
		// dont instantiate
	}
	
	public static<A extends Annotation> List<A> getAnnotations(Class<?> inspectedClass, Class<A> annoType) {
		return addAnnotationsToList(inspectedClass, annoType, new LinkedList<A>());
	}
	
	private static<A extends Annotation> List<A> addAnnotationsToList(Class<?> inspectedClass, Class<A> annoType, List<A> lst) {
		A anno = inspectedClass.getAnnotation(annoType);
		if (anno != null) {
			lst.add(anno);
		}
		
		if (inspectedClass.getSuperclass() != Object.class && inspectedClass.getSuperclass() != null) {
			addAnnotationsToList(inspectedClass.getSuperclass(), annoType, lst);
		}
		
		for (Class<?> clazz : inspectedClass.getInterfaces()) {
			addAnnotationsToList(clazz, annoType, lst);
		}
		
		return lst;
	}
}
