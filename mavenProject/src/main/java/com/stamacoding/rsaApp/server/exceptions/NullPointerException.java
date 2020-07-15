package com.stamacoding.rsaApp.server.exceptions;

public class NullPointerException extends java.lang.NullPointerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4314470920795579894L;
	
	public NullPointerException(Class<?> parameterClass, String parameterName) {
		super(parameterName + "(" + parameterClass.getSimpleName() + ") is not allowed to be null!");
	}

}
