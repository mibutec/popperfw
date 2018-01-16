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
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.popper.forge.api.annotations.RunBefore;

/**
 * Exception thrown if {@link PageObjectImplementation} detects a cycle in
 * dependency tree of annotations. Can happen when usind {@link RunBefore} and / or
 * {@link RunBefore}
 *
 * @author Michael
 */
public class CycleException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CycleException(CycleDetector<Annotation, DefaultEdge> cycleDetector) {
        super("found cycle in Lifecycle management: " + cycleToString(cycleDetector));
    }

    private static String cycleToString(CycleDetector<Annotation, DefaultEdge> cycleDetector) {
        StringBuilder builder = new StringBuilder();

        // Get all vertices involved in cycles.
        Set<Annotation> cycleVertices = cycleDetector.findCycles();

        // Loop through vertices trying to find disjoint cycles.
        while (!cycleVertices.isEmpty()) {
            // Get a vertex involved in a cycle.
            Iterator<Annotation> iterator = cycleVertices.iterator();

            // Get all vertices involved with this vertex.
            Set<Annotation> subCycle = cycleDetector.findCyclesContainingVertex(iterator.next());
            for (Annotation sub : subCycle) {
                builder.append(" -> " + sub);
                cycleVertices.remove(sub);
            }
        }

        return builder.toString();
    }
}
