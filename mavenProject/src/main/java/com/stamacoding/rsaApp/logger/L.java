package com.stamacoding.rsaApp.logger;

import com.stamacoding.rsaApp.logger.console.ConsoleOutput;
import com.stamacoding.rsaApp.logger.file.FileMode;
import com.stamacoding.rsaApp.logger.file.FileOutput;

/**
 * <p>
 * Interface to log messages and errors in an ordered way. Dependent on the {@link L.Config}
 * these messages are printed to the console and/or stored in one or multiple log files. Every log entry can
 * contain following types of (meta) information:
 * </p>
 * 
 * <ol>
 * 	<li><b>Tag</b>: The tag of a log entry is a string that assigns the log entry to a more
 * 		abstract context (e.g. "Encryption").</li>
 * 	<li><b>Class of origin</b>: The class of origin is the class where the log entry gets created.</li>
 * 	<li><b>Message</b>: The log entry's message as string.</li>
 * 	<li><b>{@link Level}</b>: The log level describes the importance of a log and how it affects the program execution. Every log level has its own function.</li>
 * </ol>
 *
 * <p>
 * Furthermore (fatal) errors can contain the {@link Exception} that caused
 * the unwanted program state or is important in that context. By that the exception's stack trace gets logged too.
 * </p>
 * 
 * @see L.Config
 */
public class L{
	
	/**
	 * The logger's configuration. Its divided into {@link Console} related and {@link File} related settings.
	 */
	public static class Config{
		
		/**
		 * Logger's console settings. Use {@link #set(boolean, Level, boolean)} or change the different variables directly to
		 * change the settings.
		 */
		public static class Console {
			/**
			 * Whether the console output is enabled.
			 */
			public static boolean OUTPUT_ENABLED = true;
			
			/**
			 * The minimum log level that is printed to the console.
			 */
			public static Level LEVEL = Level.DEBUG; 
			
			/**
			 * Whether the tag feature is enabled.
			 * @see L
			 */
			public static boolean TAGGING_ENABLED = false;
			
			/**
			 * Sets all console settings.
			 * @param outputEnabled whether the console output should be enabled
			 * @param level the minimum log level to set
			 * @param taggingEnabled whether the tag feature should be enabled
			 */
			public static void set(boolean outputEnabled, Level level, boolean taggingEnabled) {
				OUTPUT_ENABLED = outputEnabled;
				LEVEL = level;
				TAGGING_ENABLED = taggingEnabled;
			}
		}
		
		/**
		 * Logger's file settings. Use {@link #set(boolean, Level, boolean)} or change the different variables directly to
		 * change the settings.
		 */
		public static class File{
			/**
			 * Whether the file output is enabled.
			 */
			public static boolean OUTPUT_ENABLED = true;
			
			/**
			 * The minimum log level that gets written to the log file(s).
			 */
			public static Level LEVEL = Level.DEBUG; 
			
			/**
			 * Whether the tag feature is enabled.
			 * @see L
			 */
			public static boolean TAGGING_ENABLED = true;
			
			/**
			 * The maximum file size of a log file (in bytes).
			 */
			public static long MAX_SIZE = 2 * 1024 * 1024;
			
			/**
			 * The used file output mode.
			 * @see FileMode
			 */
			public static FileMode MODE = FileMode.ONE_PER_THREAD;
			
			/**
			 * Sets all file settings.
			 * @param outputEnabled whether the file output should be enabled
			 * @param level the minimum log level that should be written to the log file(s)
			 * @param taggingEnabled whether the tag feature should be enabled
			 * @param maxSize the maximum file size of a log file to set
			 * @param mode the used file output mode ({@link FileMode})
			 */
			public static void set(boolean outputEnabled, Level level, boolean taggingEnabled, long maxSize, FileMode mode) {
				OUTPUT_ENABLED = outputEnabled;
				LEVEL = level;
				TAGGING_ENABLED = taggingEnabled;
				MAX_SIZE = maxSize;
				MODE = mode;
			}
		}
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void f(String t, Class<?> c, String m, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, t, c, m, e));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void f(Class<?> c, String m, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, null, c, m, e));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void f(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, null, c, m, null));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void f(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, t, c, m, null));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param c The class where the log entry gets created
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void f(Class<?> c, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, null, c, e.getMessage(), null));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void f(String t, Class<?> c, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null");
		
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, t, c, e.getMessage(), null));
		
		System.exit(-1);
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void e(Class<?> c, String m, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, null, c, m, e));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void e(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, null, c, m, null));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param c The class where the log entry gets created
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void e(Class<?> c, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, null, c, e.getMessage(), e));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void e(String t, Class<?> c, String m, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, t, c, m, e));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void e(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, t, c, m, null));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void e(String t, Class<?> c, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, t, c, e.getMessage(), e));
	}
	
	/**
	 * Logs a warning. Is used when something happened that can potentially cause application oddities but is not an error.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void w(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.WARNING, null, c, m, null));
	}
	
	/**
	 * Logs a warning. Is used when something happened that can potentially cause application oddities but is not an error.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void w(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.WARNING, t, c, m, null));
	}
	
	/**
	 * Logs generally useful information.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void i(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.INFO, null, c, m, null));
	}
	
	/**
	 * Logs generally useful information.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void i(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.INFO, t, c, m, null));
	}
	
	/**
	 * Logs information that is diagnostically helpful.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 */
	public static void d(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.DEBUG, null, c, m, null));
	}
	
	/**
	 * Logs information that is diagnostically helpful.
 	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 */
	public static void d(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.DEBUG, t, c, m, null));
	}
	
	/**
	 * Logs information that is important for tracing the program's execution in detail.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 */
	public static void t(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.TRACE, null, c, m, null));
	}
	
	/**
	 * Logs information that is important for tracing the program's execution in detail.
 	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 */
	public static void t(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.TRACE, t, c, m, null));
	}
	
	/**
	 * Logs a log entry using the log entry object.
	 * @param entry the log entry as object
	 * @see LogEntry
	 */
	private static void log(LogEntry entry) {
		if(entry.getTime() < 0) throw new LoggerException("Invalid log time");
		if(entry.getLevel() == null) throw new LoggerException("Log level cannot be null"); 
		if(entry.getClassOfOrigin() == null) throw new LoggerException("Class of origin cannot be null"); 
		if(entry.getMessage() == null) throw new LoggerException("Log message cannot be null");
		
		if(Config.File.OUTPUT_ENABLED && Level.asInt(entry.getLevel()) >= Level.asInt(Config.File.LEVEL)) FileOutput.write(entry);
		if(Config.Console.OUTPUT_ENABLED && Level.asInt(entry.getLevel()) >= Level.asInt(Config.Console.LEVEL)) ConsoleOutput.print(entry);
	}
	
	public static void main(String[] args) {
		L.d("Tag", L.class, "Hallooo");
		L.t(Level.class, "Hallooo");
	}
}
