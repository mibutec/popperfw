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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.popper.forge.api.IAnnotationProcessor;
import org.popper.forge.api.IMethodAnalyzer;
import org.popper.forge.api.annotations.ImplementedBy;
import org.popper.forge.api.annotations.RunAfter;
import org.popper.forge.api.annotations.RunBefore;
import org.popper.forge.helper.AnnotationFinder;

/**
 * Default implementation of {@link IMethodAnalyzer} using annotations to find the {@link IAnnotationProcessor}s to use
 * 
 * @author michael_bulla
 *
 */
public class DefaultMethodAnalyzer implements IMethodAnalyzer {
	private final Map<Class<? extends Annotation>, Class<? extends IAnnotationProcessor<?, ?>>> annotationHandlerMapping;

	
	public DefaultMethodAnalyzer(
			Map<Class<? extends Annotation>, Class<? extends IAnnotationProcessor<?, ?>>> annotationHandlerMapping) {
		super();
		this.annotationHandlerMapping = annotationHandlerMapping;
	}

	@Override
    public List<AnnotationProcessorTupel> findProcessorsForMethod(Method method) {
		List<AnnotationProcessorTupel> ret = new ArrayList<>();
		for (Annotation annotation : getOrderedAnnotations(method)) {
			Class<? extends IAnnotationProcessor<?, ?>> processorClassToUse = getProcessorForAnnotation(annotation.annotationType());
			if (processorClassToUse != null) {
				AnnotationProcessorTupel tupel = new AnnotationProcessorTupel(annotation, method, processorClassToUse);
				ret.add(tupel);
			}
		}

		if (ret.isEmpty()) {
			Method parentMethod = AnnotationFinder.findMethodInSuperClass(method);
			if (parentMethod != null) {
				ret = findProcessorsForMethod(parentMethod);
			}
		}
		
		return ret;
    }
    
	private List<Annotation> getOrderedAnnotations(AnnotatedElement annotationHolder) {
		DefaultDirectedGraph<Annotation, DefaultEdge> graph = new DefaultDirectedGraph<Annotation, DefaultEdge>(
				DefaultEdge.class);
		for (Annotation annotation : annotationHolder.getAnnotations()) {
			graph.addVertex(annotation);
		}

		for (Annotation annotation : annotationHolder.getAnnotations()) {
			Class<? extends Annotation> thisClass = annotation.annotationType();

			RunAfter runAfterAnnotation = thisClass
					.getAnnotation(RunAfter.class);
			if (runAfterAnnotation != null) {
				for (Class<? extends Annotation> otherClass : runAfterAnnotation
						.value()) {
					if (annotationHolder.getAnnotation(otherClass) != null) {
						graph.addEdge(annotationHolder.getAnnotation(otherClass),
								annotation);
					}
				}
			}

			RunBefore runBeforeAnnotation = thisClass
					.getAnnotation(RunBefore.class);
			if (runBeforeAnnotation != null) {
				for (Class<? extends Annotation> otherClass : runBeforeAnnotation
						.value()) {
					if (annotationHolder.getAnnotation(otherClass) != null) {
						graph.addEdge(annotation,
								annotationHolder.getAnnotation(otherClass));
					}
				}
			}
		}

		return getOrderedReferences(graph);
	}
	
	private List<Annotation> getOrderedReferences(
			DefaultDirectedGraph<Annotation, DefaultEdge> g) {
		CycleDetector<Annotation, DefaultEdge> cycleDetector = new CycleDetector<Annotation, DefaultEdge>(
				g);
		if (cycleDetector.detectCycles()) {
			throw new CycleException(cycleDetector);
		}

		List<Annotation> ret = new ArrayList<Annotation>();
		TopologicalOrderIterator<Annotation, DefaultEdge> orderIterator = new TopologicalOrderIterator<Annotation, DefaultEdge>(
				g);
		while (orderIterator.hasNext()) {
			ret.add(orderIterator.next());
		}

		return ret;
	}
	
	private Class<? extends IAnnotationProcessor<?, ?>> getProcessorForAnnotation(Class<? extends Annotation> annotationClass) {
		Class<? extends IAnnotationProcessor<?, ?>> processorClassToUse = annotationHandlerMapping.get(annotationClass);
		
		if (processorClassToUse != null) {
			return processorClassToUse;
		}
		
		ImplementedBy annotationImpl = annotationClass.getAnnotation(ImplementedBy.class);
		if (annotationImpl != null) {
			return annotationImpl.value();
		}
		
		return null;

	}
}
