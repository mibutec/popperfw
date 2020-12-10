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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.popper.forge.api.IFieldProvidingAnnotationProcessor.ProvidedField;
import org.popper.forge.api.IMethodAnalyzer.AnnotationProcessorTupel;
import org.popper.forge.helper.ReflectionHelper;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * Creates an implementation class out of the blank using Javassist
 * 
 * @author michael_bulla
 *
 */
public class ClassCreator {
	private final BlankAnalyzer blankAnalyzer;
	
	private final ClassLoaderProvider classLoaderProvider;
	
	public ClassCreator(BlankAnalyzer blankAnalyzer, ClassLoaderProvider classLoaderProvider) {
		this.blankAnalyzer = blankAnalyzer;
		this.classLoaderProvider = classLoaderProvider;
	}

	/**
	 * Creates the implementation class for the given blank classes
	 * 
	 * @param blankClass The first and mandatory blank class to forge an implementation for
	 * @param furtherInterfaces further optional interfaces to be forged into the forged class
	 * @param implName name of the implementation class
	 * @return forged class
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> createImplClass(Class<T> blankClass, Class<?>[] furtherInterfaces, String implName) {
		
        try {
        	return (Class<T>) classLoaderProvider.provideClassLoader(blankClass, furtherInterfaces).loadClass(implName);
        } catch (ClassNotFoundException cnfe) {
            // class doesn't exist, create it
        }

		CtClass evalClass = null;
		
		try {
			ClassPool pool = ClassPool.getDefault();
			evalClass = pool.makeClass(implName);

			if (blankClass.isInterface()) {
				evalClass.addInterface(pool.get(blankClass.getName()));
			} else {
				evalClass.setSuperclass(pool.get(blankClass.getName()));
			}
			
			for (Class<?> furtherInterface : furtherInterfaces) {
				evalClass.addInterface(pool.get(furtherInterface.getName()));
			}

			addField(evalClass, new ProvidedField("h", InvokationHandler.class));

			Set<ProvidedField> requiredFields = new HashSet<>();
			for (Method builderMethod : blankAnalyzer.findMethodsToProxy(blankClass)) {
				addMethod(evalClass, builderMethod, blankClass);
				requiredFields.addAll(blankAnalyzer.determineRequiredFields(builderMethod, blankClass));
			}
			
			for (Class<?> furtherInterface : furtherInterfaces) {
				for (Method builderMethod : blankAnalyzer.findMethodsToProxy(furtherInterface)) {
					addMethod(evalClass, builderMethod, blankClass);
					requiredFields.addAll(blankAnalyzer.determineRequiredFields(builderMethod, blankClass));
				}
			}
			
			for (ProvidedField requiredField :requiredFields) {
				addField(evalClass, requiredField);
			}

			return (Class<T>) evalClass.toClass(classLoaderProvider.provideClassLoader(blankClass, furtherInterfaces), this.getClass().getProtectionDomain());
		} catch (CannotCompileException | NotFoundException e) {
			throw new CantCreateClassException("error creating class " + implName + " of blank type " + blankClass.getName(), e);
		} finally {
			if (evalClass != null) {
				evalClass.detach();
			}
		}
	}

	private void addField(CtClass evalClass, ProvidedField newField) throws NotFoundException, CannotCompileException {
		CtClass ctFieldClass = ClassPool.getDefault().get(newField.type.getName());
		CtField f = new CtField(ctFieldClass, newField.name, evalClass);
		f.setModifiers(Modifier.PUBLIC);

		evalClass.addField(f);
	}

	private void addMethod(CtClass evalClass, Method m, Class<?> blankClass)
			throws CannotCompileException, NotFoundException {
		String methodString = getMethodString(m, blankClass);
		
		try {
			CtField staticMethodField = new CtField(ClassPool.getDefault().get(Method.class.getName()),
					getStaticMethodFieldName(m), evalClass);
			staticMethodField.setModifiers(Modifier.PRIVATE | Modifier.STATIC);
			evalClass.addField(staticMethodField, getStaticMethodFieldInitializer(m));
			evalClass.addMethod(CtNewMethod.make(methodString, evalClass));
		} catch (CannotCompileException cce) {
			throw new CannotCompileException("error compiling " + methodString, cce);
		}
	}

	private String getStaticMethodFieldName(Method m) {
		return "__$MethodName_" + m.getName() + "_" + Arrays.stream(m.getParameterTypes()).map(c -> c.getSimpleName()).collect(Collectors.joining("_")).replace("[]", "Array");
	}

	private String getStaticMethodFieldInitializer(Method m) {
		String classArrayString = "new Class[0]";
		if (m.getParameterTypes().length > 0) {
			classArrayString = "new Class[] {" + Arrays.stream(m.getParameterTypes()).map(c -> typeToString(c) + ".class")
					.collect(Collectors.joining(", ")) + "}";
		}
		return m.getDeclaringClass().getName() + ".class.getMethod(\"" + m.getName() + "\"," + classArrayString + ");";

	}

	private String getMethodString(Method m, Class<?> blankClass) {
		Class<?> returnType = void.class;
		if (!void.class.equals(m.getReturnType())) {
			returnType = ReflectionHelper.getRealTypeFromGenericOne(m.getReturnType(), m.getGenericReturnType());
		}

		StringBuilder parameterDef = new StringBuilder();
		StringBuilder parameterString = new StringBuilder();
		for (int i = 0; i < m.getParameters().length; i++) {
			if (parameterDef.length() != 0) {
				parameterDef.append(", ");
				parameterString.append(", ");
			}
			parameterDef.append(typeToString(m.getParameterTypes()[i]) + " arg" + i);
			parameterString.append(getBoxingCode(m.getParameterTypes()[i], "arg" + i));
		}

		String methodSignature = "public " + typeToString(returnType) + " " + m.getName() + "(" + parameterDef + ") {";
		String returnStatement = "";

		if (!void.class.equals(m.getReturnType())) {
			returnStatement = "return (" + typeToString(m.getReturnType()) + ") ";
		}

		String argsParameterString = "new Object[0]";
		if (parameterString.length() > 0) {
			argsParameterString = "new Object[] {" + parameterString + "}";
		}
		return methodSignature + returnStatement + getUnboxingCode(m.getReturnType(),
				"h.handleMethodCall(this, " + blankClass.getName() + ".class, " + getStaticMethodFieldName(m) + ", " + argsParameterString + ")") + ";}";
	}
	
	private String typeToString(Class<?> clazz) {
		if (clazz.isArray()) {
			return clazz.getComponentType().getName() + "[]";
		}
		return clazz.getName();
	}

	/**
	 * Javassist doesn't support boxing / unboxing of primitive type, this must be done by ourselfes. Here's the
	 * method to handle unboxing primitive types out of object representation 
	 * 
	 * @param type type to be handled
	 * @param codeToUnbox the java code to compile representing the given type
	 * @return unboxed java code, if necessary, otherwise the given code with no changes
	 */
	private String getUnboxingCode(Class<?> type, String codeToUnbox) {
		if (type == boolean.class) {
			return "((Boolean) " + codeToUnbox + ").booleanValue()";
		} else if (type == byte.class) {
			return "((Byte) " + codeToUnbox + ").byteValue()";
		} else if (type == char.class) {
			return "((Character) " + codeToUnbox + ").charValue()";
		} else if (type == short.class) {
			return "((Short) " + codeToUnbox + ").shortValue()";
		} else if (type == int.class) {
			return "((Integer) " + codeToUnbox + ").intValue()";
		} else if (type == long.class) {
			return "((Long) " + codeToUnbox + ").longValue()";
		} else if (type == float.class) {
			return "((Float) " + codeToUnbox + ").floatValue()";
		} else if (type == double.class) {
			return "((Double) " + codeToUnbox + ").doubleValue()";
		} else {
			return codeToUnbox;
		}
	}

	/**
	 * Javassist doesn't support boxing / unboxing of primitive type, this must be done by ourselfes. Here's the
	 * method to handle boxing primitive types out of object representation
	 * 
	 * @param type type to be handled
	 * @param codeToBox the java code to compile representing the given type
	 * @return boxed java code, if necessary, otherwise the given code with no changes
	 */
	private String getBoxingCode(Class<?> type, String codeToBox) {
		if (type == boolean.class) {
			return "Boolean.valueOf(" + codeToBox + ")";
		} else if (type == byte.class) {
			return "Byte.valueOf(" + codeToBox + ")";
		} else if (type == char.class) {
			return "Character.valueOf(" + codeToBox + ")";
		} else if (type == short.class) {
			return "Short.valueOf(" + codeToBox + ")";
		} else if (type == int.class) {
			return "Integer.valueOf(" + codeToBox + ")";
		} else if (type == long.class) {
			return "Long.valueOf(" + codeToBox + ")";
		} else if (type == float.class) {
			return "Float.valueOf(" + codeToBox + ")";
		} else if (type == double.class) {
			return "Double.valueOf(" + codeToBox + ")";
		} else {
			return codeToBox;
		}
	}

	public static class MethodRuntimeInformation {
		public final Method method;
		public final List<AnnotationProcessorTupel> callTupels;

		public MethodRuntimeInformation(Method method, List<AnnotationProcessorTupel> callTupels) {
			super();
			this.method = method;
			this.callTupels = callTupels;
		}
	}
	
	public static interface ClassLoaderProvider {
		ClassLoader provideClassLoader(Class<?> blankClass, Class<?>[] furtherInterfaces);
	}
}
