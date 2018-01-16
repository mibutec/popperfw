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
package org.popper.forge.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationFinder {
	/**
	 * As java default behavior methods can be inherited from super class to inherited class. But
	 * not from implemented interface to implementer. This method finds inherited methods on implemented
	 * interfaces.
	 * 
	 * This is done by searching in any implemented interfaces for methods with the same method signature.
	 * 
	 * @param method method to search for
	 * @return found method or null
	 */
	public static Method findMethodInSuperClass(Method method) {
		String methodName = method.getName();
		Class<?> parentClass = method.getDeclaringClass().getSuperclass();
		Class<?> declaringClass = method.getDeclaringClass();
		Class<?>[] types = method.getParameterTypes();

		Method foundMethod = getMethod(parentClass, methodName, types);
		if (foundMethod != null) {
			return foundMethod;
		}

		Class<?>[] implementingInterfaces = declaringClass.getInterfaces();
		for (Class<?> implementingInterface : implementingInterfaces) {
			foundMethod = getMethod(implementingInterface, methodName, types);
			if (foundMethod != null) {
				return foundMethod;
			}
		}

		return null;
	}

	public static Method getMethod(Class<?> declaringClass, String methodName,
			Class<?>[] types) {
		try {
			return declaringClass.getMethod(methodName, types);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * As java default behavior annotations can be inherited from super class to inherited class. But
	 * not from implemented interface to implementer. This method finds inherited annotations on implemented
	 * interfaces.
	 * 
	 * @param clazz class to search annotation on
	 * @param annotationClass annotation to search for
	 * @return first found annotation of type or null
	 */
	public static<A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationClass) {
		A annotation = clazz.getAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		
		for (Class<?> implementingInterface : clazz.getInterfaces()) {
			A annotationOnInterface = findAnnotation(implementingInterface, annotationClass);
			if (annotationOnInterface != null) {
				return annotationOnInterface;
			}
		}
		
		return null;
	}
	
	public static<A extends Annotation> A findMandatoryAnnotation(Class<?> clazz, Class<A> annotationClass) {
		A ret = findAnnotation(clazz, annotationClass);
		
		if (ret == null) {
            throw new IllegalStateException("Didn't find mandatory @" + annotationClass.getSimpleName()
                    + " annotation on " + clazz.getSimpleName());
		}
		
		return ret;
	}

}
