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

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {
	private ReflectionHelper() {
		
	}
	
	public final static Map<Class<?>, Class<?>> primitiveWrapperMapper = new HashMap<Class<?>, Class<?>>();
	
	static {
	    primitiveWrapperMapper.put(boolean.class, Boolean.class);
	    primitiveWrapperMapper.put(byte.class, Byte.class);
	    primitiveWrapperMapper.put(short.class, Short.class);
	    primitiveWrapperMapper.put(char.class, Character.class);
	    primitiveWrapperMapper.put(int.class, Integer.class);
	    primitiveWrapperMapper.put(long.class, Long.class);
	    primitiveWrapperMapper.put(float.class, Float.class);
	    primitiveWrapperMapper.put(double.class, Double.class);
	    primitiveWrapperMapper.put(void.class, Void.class);
	}
	
	/**
	 * Get the object wrapper fir a primitive type
	 * @param clazz class to get the wrapper for
	 * @return wrapper class in case the input is a primitive, otherwise the input itself
	 */
	public static Class<?> getWrapper(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return primitiveWrapperMapper.get(clazz);
		}
		
		return clazz;
	}
	
	public static Class<?> getRealTypeFromGenericOne(Class<?> type, Type genericType) {
		if (genericType instanceof TypeVariable) {
			TypeVariable<?> tv = (TypeVariable<?>) genericType;
			if (tv.getBounds().length > 0) {
				return (Class<?>) tv.getBounds()[0];
			} else {
				return Object.class;
			}
		} else {
			return type;
		}
	}
}
