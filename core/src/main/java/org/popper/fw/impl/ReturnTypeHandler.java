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
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.popper.fw.NoGenericTypeAllowedException;
import org.popper.fw.interfaces.LocatorContextInformation;

/**
 * Class responsible to instantiate the result of a evalutated method by its return type
 * @author michael_bulla
 */
public class ReturnTypeHandler<A extends Annotation, INTERNAL_OBJECTTYPE> {
	private final ReturnTypeFactory<A, INTERNAL_OBJECTTYPE> returnTypeFactory;
	
	public ReturnTypeHandler(ReturnTypeFactory<A, INTERNAL_OBJECTTYPE> returnTypeFactory) {
		super();
		this.returnTypeFactory = returnTypeFactory;
	}

	public Object createReturnObject(LocatorContextInformation info, A annotation) {
		Method method = info.getMethod();
		if (method.getGenericReturnType() instanceof TypeVariable) {
			throw new NoGenericTypeAllowedException(method);
		}
		
		if (List.class.isAssignableFrom(method.getReturnType())) {
			return handleCollection(annotation, info, new LinkedList<Object>());
		} else if (Set.class.isAssignableFrom(method.getReturnType())) {
			return handleCollection(annotation, info, new HashSet<Object>());
		} else if (Map.class.isAssignableFrom(method.getReturnType())) {
			throw new RuntimeException("Map is not supported by PageObjects (yet)");
		} else if (method.getReturnType().isArray()) {
			return handleArray(annotation, info);
		} else if (isCountType(method.getReturnType())) {
			return returnTypeFactory.handleCountType(info, annotation);
		} else {
			return returnTypeFactory.createObject(annotation, info, method.getReturnType(), null);
		}
	}
	
	private boolean isCountType(Class<?> clazz) {
		return clazz == Integer.class || clazz == Long.class || clazz == Short.class || clazz == int.class
				|| clazz == long.class || clazz == short.class;
	}
	
	protected <T extends Collection<Object>> T handleCollection(A annotation, LocatorContextInformation info, T col) {
		Method method = info.getMethod();
		if (method.getGenericReturnType() instanceof ParameterizedType) {
			ParameterizedType stringListType = (ParameterizedType) method.getGenericReturnType();
			Class<?> genericType = (Class<?>) stringListType.getActualTypeArguments()[0];
			List<INTERNAL_OBJECTTYPE> elements = returnTypeFactory.handleCollectionType(annotation, info);
			for (INTERNAL_OBJECTTYPE element : elements) {
				col.add(returnTypeFactory.createObject(annotation, info, genericType, element));
			}
			return col;
		} else {
			throw new RuntimeException("cannot create List on not parametrized type (" + method.getReturnType() + ")");
		}
	}
	
	protected Object handleArray(A annotation, LocatorContextInformation info) {
		Class<?> genericType = info.getMethod().getReturnType().getComponentType();
		List<INTERNAL_OBJECTTYPE> elements = returnTypeFactory.handleCollectionType(annotation, info);
		Object[] arr = (Object[]) Array.newInstance(genericType, elements.size());

		for (int i = 0; i < elements.size(); i++) {
			INTERNAL_OBJECTTYPE element = elements.get(i);
			arr[i] = returnTypeFactory.createObject(annotation, info, genericType, element);
		}
		return arr;
	}
	
	public static interface ReturnTypeFactory<A extends Annotation, INTERNAL_OBJECTTYPE> {
		Object handleCountType(LocatorContextInformation info, A annotation);
		
		List<INTERNAL_OBJECTTYPE> handleCollectionType(A annotation, LocatorContextInformation info);
		
		Object createObject(A annotation, LocatorContextInformation info, Class<?> type, INTERNAL_OBJECTTYPE internalObject);
	}
}
