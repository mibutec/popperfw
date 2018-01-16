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

import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.popper.forge.ClassCreator.ClassLoaderProvider;
import org.popper.forge.ClassForge.ClassNameProvider;
import org.popper.forge.api.DependencyResolver;
import org.popper.forge.api.DependencyResolver.SimpleDependencyResolver;
import org.popper.forge.api.IAnnotationProcessor;

/**
 * Configuration class for {@link ClassForge}
 * 
 * @author michael_bulla
 *
 */
public class ClassForgeConfig {
	private ClassNameProvider classNameProvider = (c, fc) -> c.getName() + "Impl";
	
	private ClassLoaderProvider classLoaderProvider = (c, fc) -> getClass().getClassLoader();
	
	private final Map<Class<? extends Annotation>, Class<? extends IAnnotationProcessor<?, ?>>> annotationProcessorMapping = new HashMap<>();
	
	private DependencyResolver dependencyResolver = new SimpleDependencyResolver();
	
	/**
	 * Set the {@link ClassNameProvider} to use
	 * 
	 * @param classNameProvider {@link ClassNameProvider} to use
	 */
	public ClassForgeConfig setClassNameProvider(ClassNameProvider classNameProvider) {
		this.classNameProvider = classNameProvider;
		return this;
	}
	
	/**
	 * Set the {@link ClassLoaderProvider} to use
	 * @param classLoaderProvider {@link ClassLoaderProvider} to use
	 */
	public ClassForgeConfig setClassLoaderProvider(ClassLoaderProvider classLoaderProvider) {
		this.classLoaderProvider = classLoaderProvider;
		return this;
	}
	
	/**
	 * You may instruct class forge to use an own {@link ClassLoader} per created blank instance. This is more comfortable, since you
	 * can create more specialized instances for your use case creating a new class each time a blank should be forged.
	 * 
	 * But this comes at a price: {@link ClassLoader}s may not access private or package private classes loaded by another {@link ClassLoader}.
	 * This means you cannot for private or package private interfaces.
	 */
	public ClassForgeConfig useOwnClassLoaderPerBlank() {
		classLoaderProvider = (c, cl) -> new URLClassLoader(new URL[0], Thread.currentThread().getContextClassLoader());
		return this;
	}
	
	/**
	 * Set the {@link DependencyResolver} to use. If you are in a dependency injection managed environment this is the place
	 * to give class forge access to your <code>ApplicationContext</code>. Just create your own implementation of {@link DependencyResolver}
	 * accessing <code>ApplicationContext</code> to resolve dependencies.
	 * 
	 * @param dependencyResolver {@link DependencyResolver} to use
	 */
	public ClassForgeConfig setDependencyResolver(DependencyResolver dependencyResolver) {
		this.dependencyResolver = dependencyResolver;
		return this;
	}
	
	/**
	 * Normally the {@link IAnnotationProcessor} to be used for a given annotation is linked on the annotation itself. You may come to a point you
	 * want to override the default implementation for a given annotation. This can be done here.
	 * 
	 * @param annotationClass The annotation class to override behavior for 
	 * @param processorClass the processor to use
	 */
	public ClassForgeConfig addAnnotationProcessorOverride(Class<? extends Annotation> annotationClass, Class<? extends IAnnotationProcessor<?, ?>> processorClass) {
		annotationProcessorMapping.put(annotationClass, processorClass);
		return this;
	}
	
	public ClassNameProvider getClassNameProvider() {
		return classNameProvider;
	}

	public ClassLoaderProvider getClassLoaderProvider() {
		return classLoaderProvider;
	}

	public Map<Class<? extends Annotation>, Class<? extends IAnnotationProcessor<?, ?>>> getAnnotationProcessorMapping() {
		return annotationProcessorMapping;
	}

	public DependencyResolver getDependencyResolver() {
		return dependencyResolver;
	}
}
