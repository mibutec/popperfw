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
package org.popper.forge;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.popper.forge.api.DependencyResolver;

/**
 * Main entry point to class forge framework. This one is called to create forged objects out of blank classes
 * 
 * @author michael_bulla
 *
 */
public class ClassForge implements DependencyResolver {
	private final ClassNameProvider classNameProvider;
	
	private final ClassCreator classCreator;
	
	private final InvokationHandler invokationhandler;
	
	private final BlankAnalyzer blankAnalyzer;
	
	private final DependencyResolver dependencyResolver;
	
	private final Set<Class<?>> alreadyInitializedClasses = new HashSet<>();
	
	private final DefaultMethodAnalyzer defaultMethodAnalyzer;

	public ClassForge() {
		this(new ClassForgeConfig());
	}
	
	public ClassForge(ClassForgeConfig config) {
		this.classNameProvider = config.getClassNameProvider();
		this.invokationhandler = new InvokationHandler(this);
		this.blankAnalyzer = new BlankAnalyzer(this);
		this.classCreator = new ClassCreator(blankAnalyzer, config.getClassLoaderProvider());
		this.dependencyResolver = config.getDependencyResolver();
		this.defaultMethodAnalyzer = new DefaultMethodAnalyzer(config.getAnnotationProcessorMapping());
	}
	
	/**
	 * Creates an instance out of a blank class
	 * 
	 * @param blankClass First and mandatory blank class to be used
	 * @param furtherInterfaces further optional blanks to add
	 * @return created forged instance of the given interfaces
	 */
	public<T> T createInstance(Class<T> blankClass, Class<?>... furtherInterfaces) {
    	if (!alreadyInitializedClasses.contains(blankClass)) {
    		for (Method m : blankAnalyzer.findMethodsToProxy(blankClass)) {
    			invokationhandler.init(m, blankAnalyzer.findProcessorsForMethod(blankClass, m));
    		}
    		for (Class<?> furtheInterface : furtherInterfaces) {
	    		for (Method m : blankAnalyzer.findMethodsToProxy(furtheInterface)) {
	    			invokationhandler.init(m, blankAnalyzer.findProcessorsForMethod(blankClass, m));
	    		}
    		}
    		alreadyInitializedClasses.add(blankClass);
    	}

        try {
            String implName = classNameProvider.provideClassName(blankClass, furtherInterfaces);
            Class<? extends T> implClass = classCreator.createImplClass(blankClass, furtherInterfaces, implName);
            T ret = implClass.newInstance();
            Field invokationHandlerField = ret.getClass().getDeclaredField("h");
            invokationHandlerField.setAccessible(true);
            invokationHandlerField.set(ret, invokationhandler);
            return ret;

        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T resolveDependency(Class<T> clazz) {
		if (DefaultMethodAnalyzer.class.isAssignableFrom(clazz)) {
			return (T) defaultMethodAnalyzer;
		}
		
		T ret = dependencyResolver.resolveDependency(clazz);
		if (ret != null) {
			return ret;
		}
		
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static interface ClassNameProvider {
		String provideClassName(Class<?> blankClass, Class<?>[] furtherInterfaces);
	}
}
