package com.stamacoding.rsaApp.logger.console;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.logger.LogEntry;
import com.stamacoding.rsaApp.logger.LoggerException;

/**
 * The logger's console output. Use {@link #print(LogEntry)} to print a log entry to the console.
 */
public class ConsoleOutput {
	
	/**
	 * Prints a log entry to the console.
	 * @param entry the entry to print
	 */
	public static void print(LogEntry entry) {
		if(entry == null) {
			throw new LoggerException("Cannot log a null entry.");
		}
		
		switch(entry.getLevel()) {
		case TRACE:
		case DEBUG:
		case INFO:
			System.out.print(entry.toString(L.Config.Console.TAGGING_ENABLED));
			break;
		case ERROR:
		case FATAL:
		case WARNING:
			System.err.print(entry.toString(L.Config.Console.TAGGING_ENABLED));
			break;
		}
		
		if(entry.getException() != null) {
			entry.getException().printStackTrace();
		}
	}
}
