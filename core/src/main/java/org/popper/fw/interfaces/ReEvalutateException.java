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
