package com.stamacoding.rsaApp.logger;

public enum Level {
	FATAL, ERROR, WARNING, INFO, DEBUG_LOW, DEBUG_MEDIUM, DEBUG_HIGH;
	
	public static int asInt(Level l) {
		switch(l) {
			case DEBUG_HIGH: return 0;
			case DEBUG_MEDIUM: return 1;
			case DEBUG_LOW: return 2;
			case INFO: return 3;
			case WARNING: return 4;
			case ERROR: return 5;
			case FATAL: return 6;
		}
		return -1;
	}
	public static Level parseInt(int i) {
		switch(i) {
			case 0: return DEBUG_HIGH;
			case 1: return DEBUG_MEDIUM;
			case 2: return DEBUG_LOW;
			case 3: return INFO;
			case 4: return WARNING;
			case 5: return ERROR;
			case 6: return FATAL;
		}
		return null;
	}
}
