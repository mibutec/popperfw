/*
 * Copyright [2013] [Michael Bulla, michaelbulla@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.popper.fw.element;

import org.popper.fw.annotations.naming.Accessor;
import org.popper.fw.annotations.naming.Action;
import org.popper.fw.annotations.naming.ParamName;

/**
 * Interfaces describing the function a fileupload must provide
 * @author Michael
 *
 */
public interface IFileupload extends IInput {
	/**
	 * Upload a file from Classpath. The Files name will the the name of the resource in classpath (without
	 * package-structure)
	 * @param resourcename name og the file in classpath
	 */
	@Action(name="upload")
	public void uploadFromClasspath(@ParamName("Filename in Classpath") String resourcename);
	
	/**
	 * Returns the name of the file to be uploaded
	 * @return filename of the file to upload, if set, otherwise null
	 */
	@Accessor(name="filename")
	public String filename();
}
