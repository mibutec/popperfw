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
package org.popper.fw.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.popper.fw.annotations.DefaultConstructor;
import org.popper.fw.annotations.ElementFactory;
import org.popper.fw.annotations.Type;
import org.popper.fw.interfaces.IClassResolver;
import org.popper.fw.interfaces.IElementFactory;
import org.popper.fw.interfaces.IPoFactory;

/**
 * Class instantiating the framework and handling application start
 * 
 * @author michael_bulla
 *
 */
public abstract class AbstractPopperContext implements IClassResolver {
	private IElementFactory defaultElementFactory;
	
	private IClassResolver classResolver;

	public abstract IPoFactory getFactory();

	public IElementFactory getDefaultElementFactory() {
		return defaultElementFactory;
	}
	
	/**
	 * Set the {@link IElementFactory} to be used by default to instantiate default elements
	 * @param defaultElementFactory
	 */
	public void setDefaultElementFactory(IElementFactory defaultElementFactory) {
		this.defaultElementFactory = defaultElementFactory;
	}
	
	public IClassResolver getClassResolver() {
		return classResolver;
	}

	/**
	 * Add a {@link IClassResolver} to the framework to tell popper which classes can be
	 * used to instantiate page objects and elements
	 */
	public void setClassResolver(IClassResolver classResolver) {
		this.classResolver = classResolver;
	}
	
	public Class<?> getImplementingClassFor(Method method, Class<?> clazz) {
		Type typeAnnoation = method.getAnnotation(Type.class);
		if (typeAnnoation != null) {
			return typeAnnoation.value();
		}

		ElementFactory elementFactory = method.getDeclaringClass().getAnnotation(ElementFactory.class);
		if (elementFactory != null) {
			try {
				return elementFactory.value().newInstance().getImplClassForElement(clazz);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (defaultElementFactory != null) {
			Class<?> ret = defaultElementFactory.getImplClassForElement(clazz);
			if (ret != null) {
				return ret;
			}
		}
		
		if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
			return clazz;
		}

		return null;
	}
	
	@Override
	public final <T> T resolveClass(Class<T> clazz) {
		T ret = resolveStaticClass(clazz);
		if (ret != null) {
			return ret;
		} else if (classResolver != null) {
			return classResolver.resolveClass(clazz);
		}
		
		return null;
	}
	
	protected abstract <T> T resolveStaticClass(Class<T> clazz);
	
	public final<T> T instantiateObject(Class<T> type, Map<Class<?>, Object> tempResolver) {
		return new ObjectInstantiator().instantiateObject(type, this, tempResolver);
	}
	
	public final<T> T instantiateObject(Class<T> type) {
		return instantiateObject(type, null);
	}
	
	public static String replaceVariables(String input, Object[] parameters) {
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				String replace = "";
				if (parameters[i] != null) {
					replace = parameters[i].toString();
				}
				input = input.replace("{" + i + "}", replace);
			}
		}
		return input;
	}
	
	public static class ObjectInstantiator {
		private static final Logger log = LogManager.getLogger(ObjectInstantiator.class);
		
		public final<T> T instantiateObject(Class<T> type, IClassResolver classResolver, Map<Class<?>, Object> tempResolver) {
			log.debug("Instantiating Object of class " + type.getName());
			Constructor<T> constructor = getConstructorToUse(type);
			constructor.setAccessible(true);
			Object[] parameters = getParameters(type, classResolver, tempResolver);
			
			try {
				T ret = constructor.newInstance(parameters);
				classResolver.postProcessInstantiatedOIbject(ret);
				return ret;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		public<T> Object[] getParameters(Class<T> type, IClassResolver classResolver, Map<Class<?>, Object> tempResolver) {
			if (tempResolver == null) {
				tempResolver = new HashMap<Class<?>, Object>();
			}
			Class<?>[] parameterTypes = getConstructorDefinition(type);
			Object[] parameters = new Object[parameterTypes.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				Object o = tempResolver.get(parameterTypes[i]);
				
				if (o == null) {
					o = classResolver.resolveClass(parameterTypes[i]);
				}
				
				if (o == null) {
					throw new RuntimeException("There is no Resolver defined to resolve class " + parameterTypes[i].getName() + " when instantiating object of type " + type.getName());
				}
				parameters[i] = o;
			}
			
			return parameters;
			
		}
		
		public Class<?>[] getConstructorDefinition(Class<?> clazz) {
			return getConstructorToUse(clazz).getParameterTypes();
		}
		
		@SuppressWarnings("unchecked")
		private static<T> Constructor<T> getConstructorToUse(Class<T> type) {
			Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();
			if (constructors.length == 1) {
				constructors[0].setAccessible(true);
				return constructors[0];
			}
			
			Constructor<T> ret = null;
			for (Constructor<T> constructor : constructors) {
				DefaultConstructor ec = constructor.getAnnotation(DefaultConstructor.class);
				if (ec != null) {
					if (ret != null) {
						throw new RuntimeException("There are at least 2 Constructors annotated with @ElementConstructor for class " + type.getName() + ": " + ret + ", " + constructor + ". There is only one such Annotation allowed.");
					}
					ret = constructor;
				}
			}
			
			if (ret == null) {
				throw new RuntimeException("There could be no Constructor determindened to instantiate " + type.getName() + ". Provide only one constructor or annotate the none to be used with @ElementConstructor.");
			}
			ret.setAccessible(true);
			return ret;
		}
	}
}


