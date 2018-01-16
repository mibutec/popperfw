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
package org.popper.fw.interfaces;

/**
 * Execption to be thrown by {@link IAnnotationProcessor} to indicate a method evaluation should
 * be reexecuted
 * 
 * @author michael_bulla
 *
 */
public class ReEvalutateException extends Exception {
	private static final long serialVersionUID = 1L;

	public ReEvalutateException() {
		
	}
}
