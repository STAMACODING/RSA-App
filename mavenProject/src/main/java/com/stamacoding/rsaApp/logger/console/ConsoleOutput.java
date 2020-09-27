package com.stamacoding.rsaApp.logger.console;

import com.stamacoding.rsaApp.logger.LogEntry;

public class ConsoleOutput {
	public static void print(LogEntry entry) {
		switch(entry.getLevel()) {
		case DEBUG_HIGH:
		case DEBUG_LOW:
		case DEBUG_MEDIUM:
		case INFO:
			System.out.print(entry.toString());
			break;
		case ERROR:
		case FATAL:
		case WARNING:
			System.err.print(entry.toString());
			break;
		}
		
		if(entry.getException() != null) {
			entry.getException().printStackTrace();
		}
	}
}
