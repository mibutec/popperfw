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

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.management.ManagementFactory;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.popper.fw.annotations.ImplementedBy;
import org.popper.fw.annotations.RunAfter;
import org.popper.fw.annotations.RunBefore;
import org.popper.fw.helper.AnnotationFinder;
import org.popper.fw.interfaces.IAnnotationProcessor;
import org.popper.fw.interfaces.LocatorContextInformation;
import org.popper.fw.interfaces.ReEvalutateException;

import javassist.util.proxy.MethodHandler;

public class PageObjectImplementation implements InvocationHandler,
		MethodHandler {
	protected final Class<?> basicClass;

	protected final String name;

	protected final PageObjectImplementation parent;

	private Set<Object> extensions;

	private final AbstractPopperContext context;

	public PageObjectImplementation(AbstractPopperContext context,
			Class<?> basicClass, String name, PageObjectImplementation parent) {
		this.context = context;
		this.extensions = new HashSet<>();
		this.basicClass = basicClass;
		this.parent = parent;
		this.name = name;
	}

	// @Override
	public Object invoke(Object self, Method thisMethod, Method proceed,
			Object[] args) throws Throwable {
		return invoke(self, thisMethod, args);
	}
	
	private static boolean isJava8() {
	    String version = ManagementFactory.getRuntimeMXBean().getSpecVersion();
	    return version.startsWith("1.8");
	}

	@SuppressWarnings({ "cast" })
	// @Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (method.getDeclaringClass() == ImplHolder.class) {
			return this;
		}

		if (method.isDefault()) {
			final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
			if (!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}
			
			final Class<?> declaringClass = method.getDeclaringClass();
			if(isJava8()) {
    			return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
    	                .unreflectSpecial(method, declaringClass)
    	                .bindTo(proxy)
    	                .invokeWithArguments(args);
			} else {
			      MethodType mt = MethodType.methodType(method.getReturnType());
			      return MethodHandles.lookup()
			          .findSpecial(declaringClass, method.getName(), mt, declaringClass)
			          .bindTo(proxy)
			          .invokeWithArguments(args);
			}
		}

		String methodName = method.getName();
		if ("equals".equals(methodName)) {
			return equals(args[0]);
		} else if ("hashCode".equals(methodName)) {
			return hashCode();
		} else if ("toString".equals(methodName)) {
			return "[" + basicClass.getName() + "]";
		}

		boolean evaluate = true;
		Object result = null;
		while (evaluate) {
			evaluate = false;
			try {
				result = evaluateMethod(method, args, null);
			} catch (ReEvalutateException ree) {
				evaluate = true;
			}
		}

		return result;
	}

	protected Map<Class<?>, Object> createReolver() {
		Map<Class<?>, Object> resolver = new HashMap<Class<?>, Object>();
		resolver.put(PageObjectImplementation.class, this);
		// fillResolverWithParents(resolver, this);
		return resolver;
	}

	// private static void fillResolverWithParents(Map<Class<?>, Object>
	// resolver, PageObjectImplementation po) {
	// resolver.put(po.basicClass, po.proxy);
	// if (po.parent != null) {
	// fillResolverWithParents(resolver, po.parent);
	// }
	//
	// }

	/**
	 * Main entry point to annotation handling. Evaluates the annotation on a method and decides
	 * which strategy to use to create a result
	 * @param method method to evaluate
	 * @param args parameters called 
	 * @param annotationHolder popper supports annotation inheritance on methods. So the evaluated method means not
	 * necessary mean to be the holder for annotations. In this parameter a {@link AnnotatedElement} holding the
	 * annotations to process is given. In most cases this should be a superclass definition of the method given
	 * @return result for the method call
	 * @throws ReEvalutateException tells the framework to re-evaluate the whole method processing
	 */
	@SuppressWarnings({ "rawtypes", "cast", "unchecked" })
	private Object evaluateMethod(Method method, Object[] args, AnnotatedElement annotationHolder)
			throws ReEvalutateException {
		Object result = null;
		if (annotationHolder == null) {
			annotationHolder = method;
		}
		LocatorContextInformation info = new LocatorContextInformation(this,
				method, annotationHolder, args, basicClass, context);
		boolean foundAction = false;
		for (Annotation annotation : getOrderedAnnotations(annotationHolder)) {
			ImplementedBy annotationImpl = annotation.annotationType()
					.getAnnotation(ImplementedBy.class);
			if (annotationImpl != null) {
				IAnnotationProcessor annotationProcessor = context
						.instantiateObject(annotationImpl.value());
				result = annotationProcessor.processAnnotation(annotation,
						info, result);
				foundAction = true;
			}
		}

		if (!foundAction) {
			Method parentMethod = AnnotationFinder.findMethodInSuperClass(method);
			if (parentMethod != null) {
				result = evaluateMethod(method, args, parentMethod);
			} else {
				throw new RuntimeException(
						"couldnt find any implementation-rule for " + method);
			}
		}
		return result;
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

	public void addExtension(Object extension) {
		extensions.add(extension);
	}

	public void removeExtension(Class<?> type) {
		Set<Object> oldExtensions = new HashSet<>(extensions);
		extensions.clear();
		for (Object ext : oldExtensions) {
			if (!type.isAssignableFrom(ext.getClass())) {
				extensions.add(ext);
			}
		}
	}

	public <T> T getExtension(Class<T> clazz) {
		for (Object ext : extensions) {
			if (clazz.isAssignableFrom(ext.getClass())) {
				return clazz.cast(ext);
			}
		}

		return null;
	}

	public String getName() {
		return name;
	}

	public PageObjectImplementation getParent() {
		return parent;
	}

	public Class<?> getBasicClass() {
		return basicClass;
	}
}
