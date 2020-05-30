package com.stamacoding.rsaApp.log.logger;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

public class Logger {
	/** only for test runs and unimportant information 
	 * 
	 * @param className input for class, that called the method
	 * @param message input for message, typed by the calling class
	 **/
	public void test(String className, String message) {
		console(className, message, logType.TEST);
	}
	
	/** only for unimportant information while program is running 
	 * 
	 * @param className input for class, that called the method
	 * @param message input for message, typed by the calling class
	 **/
	public void debug(String className, String message) {
		console(className, message, logType.DEBUG);
	}
	
	/** only for problems, that do not make the program stop 
	 * 
	 * @param className input for class, that called the method
	 * @param message input for message, typed by the calling class
	**/
	public void warning(String className, String message) {
		console(className, message, logType.WARNING);
	}
	
	/** 
	 * only for issues, that make the program stop 
	 * 
	 * @param className input for class, that called the method
	 * @param message input for message, typed by the calling class
	 **/
	public void error(String className, String message) {
		console(className, message, logType.ERROR);
	}
	
	/**
	 * does all the console- (and maybe log-) entries, gets parameters from the four methods
	 * 
	 * @param className
	 * @param message
	 * @param Type describes, which method called -> test, debug, warning, error
	 */
	public void console(String className, String message, logType Type) {
		String logMessage = /*"[" + time + "]" +*/ /*"[" + status + "]" +*/ /*"[" + client/server + "]" +*/ "[" + className + "]" + "[" + Type.toString() + "]" + ": "+ "[" + message + "]";
		System.out.println(logMessage);
	}
}

enum logType
{
	TEST,
	DEBUG,
	WARNING,
	ERROR
}