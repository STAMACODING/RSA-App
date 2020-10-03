package com.stamacoding.rsaApp.logger;

/**
 * Exception caused by the logger.
 */
public class LoggerException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6122394926931681074L;

	/**
	 * Creates a new logger exception. A logger exception is an exception caused by the logger.
	 * @param message the exception's message
	 * 
	 * @see LoggerException
	 */
	public LoggerException(String message) {
		super(message);
	}
}
