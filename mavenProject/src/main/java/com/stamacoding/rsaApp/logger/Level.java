package com.stamacoding.rsaApp.logger;


/**
 * Enumeration holding all different log levels. The log level describes the importance of a log and how it affects the program execution.
 * 
 * <ol>
 * 	<li>{@link FATAL}: A log message of this type is called after a fatal error forcing a complete shutdown.</li>
 * 	<li>{@link ERROR}: A log message of this type is called after an error occurred that is fatal to the operation but not to the whole application. The program continues to run.</li>
 * 	<li>{@link WARNING}: Is used when something happened that can potentially cause application oddities but is not an error.</li>
 *  <li>{@link INFO}: Used for generally useful information.</li>
 *  <li>{@link DEBUG}: Used for logs containing information that is diagnostically helpful.</li>
 *  <li>{@link TRACE}: Used for logs that are important for tracing the program's execution in detail.</li>
 * </ol>
 *
 */
public enum Level {
	/**
	 * A log message of this type is called after a fatal error occurred forcing a complete <b>shutdown</b>. <br>
	 * Use <code>L.f(...)</code> do create a log entry of this log level.
	 */
	FATAL,
	/**
	 * A log message of this type is called after an error occurred that is fatal to the related operation but not to the whole application. The program continues to run. <br>
	 * Use <code>L.e(...)</code> do create a log entry of this log level.
	 */
	ERROR,
	/**
	 * Is used when something happened that can potentially cause application oddities but is not an error. <br>
	 * Use <code>L.w(...)</code> do create a log entry of this log level.
	 */
	WARNING, 
	/**
	 * Used for generally useful information. <br>
	 * Use <code>L.i(...)</code> do create a log entry of this log level.
	 */
	INFO, 
	/**
	 * Used for logs containing information that is diagnostically helpful. <br>
	 * Use <code>L.d(...)</code> do create a log entry of this log level.
	 */
	DEBUG,
	/**
	 * Used for logs that are important for tracing the program's execution in detail. <br>
	 * Use <code>L.t(...)</code> do create a log entry of this log level.
	 */
	TRACE;
	
	/**
	 * Gets the integer value of a log level.
	 * @param l the log level
	 * @return the log level's integer value
	 */
	public static int asInt(Level l) {
		switch(l) {
			case TRACE: return 0;
			case DEBUG: return 1;
			case INFO: return 2;
			case WARNING: return 3;
			case ERROR: return 4;
			case FATAL: return 5;
		}
		return -1;
	}
	
	/**
	 * Parses an integer to a log level object.
	 * @param i the integer to parse
	 * @return the matching log level
	 */
	public static Level parseInt(int i) {
		switch(i) {
			case 0: return TRACE;
			case 1: return DEBUG;
			case 2: return INFO;
			case 3: return WARNING;
			case 4: return ERROR;
			case 5: return FATAL;
		}
		return null;
	}
}
