package com.stamacoding.rsaApp.logger;

import com.stamacoding.rsaApp.logger.console.ConsoleOutput;
import com.stamacoding.rsaApp.logger.file.FileMode;
import com.stamacoding.rsaApp.logger.file.FileOutput;

public class L{
	public static final LogConfig CONFIG = new LogConfig();
	
	public static void f(Class<?> c, String m, Exception e) {
		if(e == null) throw new IllegalArgumentException("f(Class c, String m, Exception e): Exception is not allowed to be null!"); 
		
		log(System.currentTimeMillis(), Level.FATAL, c, m, e);
		
		System.exit(-1);
	}
	
	public static void f(Class<?> c, String m) {
		log(System.currentTimeMillis(), Level.FATAL, c, m, null);
		
		System.exit(-1);
	}
	
	public static void e(Class<?> c, String m, Exception e) {
		if(e == null) throw new IllegalArgumentException("e(Class c, String m, Exception e): Exception is not allowed to be null!"); 
		
		log(System.currentTimeMillis(), Level.ERROR, c, m, e);
	}
	
	public static void e(Class<?> c, String m) {
		log(System.currentTimeMillis(), Level.ERROR, c, m, null);
	}
	
	public static void w(Class<?> c, String m) {
		log(System.currentTimeMillis(), Level.WARNING, c, m, null);
	}
	
	public static void i(Class<?> c, String m) {
		log(System.currentTimeMillis(), Level.INFO, c, m, null);
	}
	
	public static void dL(Class<?> c, String m) {
		log(System.currentTimeMillis(), Level.DEBUG_LOW, c, m, null);
	}
	
	public static void dM(Class<?> c, String m) {
		log(System.currentTimeMillis(), Level.DEBUG_MEDIUM, c, m, null);
	}
	
	public static void dH(Class<?> c, String m) {
		log(System.currentTimeMillis(), Level.DEBUG_HIGH, c, m, null);
	}
	
	private static void log(long t, Level l, Class<?> c, String m, Exception e) {
		if(t < 0) throw new IllegalArgumentException("Illegal log time!");
		if(l == null) throw new IllegalArgumentException("Illegal log level!"); 
		if(c == null) throw new IllegalArgumentException("Illegal class of origin!"); 
		if(m == null) throw new IllegalArgumentException("Illegal log message!"); 
		
		LogEntry entry = new LogEntry(t, l, c, m, e);
		if(Level.asInt(l) >= Level.asInt(L.CONFIG.getFileLogLevel())) FileOutput.write(entry);
		if(Level.asInt(l) >= Level.asInt(L.CONFIG.getConsoleLogLevel())) ConsoleOutput.print(entry);
	}
}
