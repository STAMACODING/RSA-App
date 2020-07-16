package com.stamacoding.rsaApp.server.exceptions;

public class InvalidValueException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3747429302304767752L;
/**
	public <T extends Object> InvalidValueException(Class<?> parameterClass, String parameterName, T value) {
		super(parameterName + "(" + parameterClass.getSimpleName() + ") has an invalid value (" + value.toString() + ")!");
	}
**/	
	public <T extends Object> InvalidValueException(Class<?> parameterClass, String parameterName, T value, String desired) {
		super(parameterName + "(" + parameterClass.getSimpleName() + ") has an invalid value (" + value.toString() + ")! Value should be " + desired + ".");
	}
	
	public InvalidValueException(String message) {
		super(message);
	}
}
