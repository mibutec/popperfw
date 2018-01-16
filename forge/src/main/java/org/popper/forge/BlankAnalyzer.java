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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.popper.forge.api.DependencyResolver;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.IFieldProvidingAnnotationProcessor;
import org.popper.forge.api.IMethodAnalyzer;
import org.popper.forge.api.RuntimeContextInformation;
import org.popper.forge.api.IFieldProvidingAnnotationProcessor.ProvidedField;
import org.popper.forge.api.IMethodAnalyzer.AnnotationProcessorTupel;
import org.popper.forge.api.annotations.AnalyzedBy;
import org.popper.forge.helper.AnnotationFinder;
import org.popper.forge.helper.ReflectionHelper;
import org.popper.forge.helper.TypeResolver;

/**
 * Analyzes a blank class to find the fields to be added to forged class and the method implementations
 * 
 * @author michael_bulla
 *
 */
public class BlankAnalyzer {
	private Predicate<Method> filterPredicate = m -> Modifier.isAbstract(m.getModifiers());

	private final DependencyResolver dependencyResolver;
	
	public BlankAnalyzer(DependencyResolver dependencyResolver) {
		this.dependencyResolver = dependencyResolver;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Set<ProvidedField> determineRequiredFields(Method method, Class<?> blankClass) {
		Set<ProvidedField> ret = new HashSet<>();
		
		for (AnnotationProcessorTupel tupel : findProcessorsForMethod(blankClass, method)) {
			if (IFieldProvidingAnnotationProcessor.class.isAssignableFrom(tupel.processorToUse)) {
				IFieldProvidingAnnotationProcessor processor = (IFieldProvidingAnnotationProcessor<?,?>) dependencyResolver.resolveDependency(tupel.processorToUse);
				ret.addAll(processor.providedFields(tupel.annotation, method, tupel.annotatedElement, blankClass));
			}
		}
		
		return ret;
	}
	
	public Collection<Method> findMethodsToProxy(Class<?> builderClass) {
		Collection<Method> ret = new HashSet<>();

		// Find all public methods (and so all methods from interfaces)
		for (Method m : builderClass.getMethods()) {
			if (filterPredicate.test(m)) {
				ret.add(m);
			}
		}

		// Find all not public methods
		Class<?> classToAnalyze = builderClass;
		while (classToAnalyze != Object.class && classToAnalyze != null) {
			for (Method m : classToAnalyze.getDeclaredMethods()) {
				if (filterPredicate.test(m)) {
					ret.add(m);
				}
			}
			classToAnalyze = classToAnalyze.getSuperclass();
		}

		return ret;
	}

    public List<AnnotationProcessorTupel> findProcessorsForMethod(Class<?> blankClass, Method method) {
    	Collection<Class<? extends IMethodAnalyzer>> methodAnalyzers;
    	AnalyzedBy analyzedby = AnnotationFinder.findAnnotation(blankClass, AnalyzedBy.class);
    	if (analyzedby != null) {
    		methodAnalyzers = Arrays.asList(analyzedby.value());
    	} else {
    		methodAnalyzers = Arrays.asList(DefaultMethodAnalyzer.class);
    	}
    	
    	List<AnnotationProcessorTupel> ret = null;
    	for (Class<? extends IMethodAnalyzer> analyzerClass : methodAnalyzers) {
    		ret = dependencyResolver.resolveDependency(analyzerClass).findProcessorsForMethod(method);
    		
    		if (ret != null && !ret.isEmpty()) {
    			break;
    		}
    	}
    	
		if (ret == null || ret.isEmpty()) {
			throw new RuntimeException("couldnt find any implementation-rule for " + method);
		}

		for (AnnotationProcessorTupel tupel : ret) {
			checkMethodConsistancy(method, tupel);
		}
		
		return ret;
    }
    
    private void checkMethodConsistancy(Method m, AnnotationProcessorTupel tupel) {
    	Class<? extends IAnnotationProcessor<?, ?>> processorClass = tupel.processorToUse;
    	Class<? extends Annotation> annotationClass = tupel.annotation.annotationType();
    	Class<?> returnType = m.getReturnType();
    	
		boolean doesAMethodMatch = false;
		Class<?> annotationTypeOfImplementation = TypeResolver.resolveRawArguments(IAnnotationProcessor.class, processorClass)[0];
		Class<?> returnTypeOfImplementation = TypeResolver.resolveRawArguments(IAnnotationProcessor.class, processorClass)[1];

		for (Method processorMethod : processorClass.getMethods()) {
			if (processorMethod.getName().equals("processAnnotation") && processorMethod.getParameterTypes().length == 3 && !Modifier.isAbstract(processorMethod.getModifiers())) {
				boolean doesFirstParameterFit = annotationTypeOfImplementation.isAssignableFrom(annotationClass);
				Class<?> boxedReturnType = ReflectionHelper.getWrapper(returnType);
				boolean doesThirdParameterFit = returnTypeOfImplementation.isAssignableFrom(boxedReturnType) || boxedReturnType.isAssignableFrom(returnTypeOfImplementation);				
				if (doesFirstParameterFit && doesThirdParameterFit) {
					doesAMethodMatch = true;
					break;
				}
			}
		}
			
		if (!doesAMethodMatch) {
			throw new IllegalStateException("Sanity check failed! Expected " + processorClass.getName() + " to contain method processAnnotation(" + annotationClass.getName() + ", " + RuntimeContextInformation.class.getName() + ", (assignable to " + returnType.getName() + ")), but found processAnnotation(" + annotationTypeOfImplementation.getName() + ", " + RuntimeContextInformation.class.getName() + ", " + returnTypeOfImplementation.getName() + ")");
		}
    }
}

