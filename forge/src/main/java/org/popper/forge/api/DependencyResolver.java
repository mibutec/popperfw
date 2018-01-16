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
package org.popper.forge.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * At some places like {@link IAnnotationProcessor} you may need access to some services or
 * objects from outside popper framework. In that cases you may use a {@link DependencyResolver}
 * in a spring-like way to resolve a class to an instantiated object.
 * 
 * @author michael_bulla
 *
 */
public interface DependencyResolver {
	public<T> T resolveDependency(Class<T> clazz);
	
	public static class SimpleDependencyResolver implements DependencyResolver {
		private Set<Object> knownDependencies = Collections.synchronizedSet(new HashSet<>());


		@Override
		@SuppressWarnings("unchecked")
		public <T> T resolveDependency(Class<T> clazz) {
			for (Object knownDependency : knownDependencies) {
				if (clazz.isAssignableFrom(knownDependency.getClass())) {
					return (T) knownDependency;
				}
			}
			
			return null;
		}
		
		public SimpleDependencyResolver addDependency(Object o) {
			knownDependencies.add(o);
			return this;
		}
	}
}
