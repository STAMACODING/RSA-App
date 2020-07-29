package com.stamacoding.rsaApp.log.logger;

import java.io.File;

import com.stamacoding.rsaApp.log.debug.Debug;
import com.stamacoding.rsaApp.log.filesystem.Filesystem;

/**
 * <p> This class handles everything that is related to sending messages in the console or saving those information in log files. </p>
 * <p> There are 4 types of log messages: </p>
 * <ol>
 * 		<li>Test: {@link Logger#test(String, String)}</li>
 * 		<li>Debug: {@link Logger#debug(String, String)}</li>
 * 		<li>Warning: {@link Logger#warning(String, String)}</li>
 * 		<li>Error: {@link Logger#error(String, String)}</li>
 * </ol>
 */
public class Logger {
	
	static{
		// Create "logs" directory automatically
		File f = new File("logs");
		f.mkdirs();
	}

	/**
	 * Sets the Output Type.
	 */
	public static outputType OutputType = outputType.ALL;

	/**
	 * [WIP]
	 * Sets the Type of files that get written.
	 */
	public static fileType FileType = fileType.ALL;

	/**
	 * First lines that get written to every log file.
	 * Can be as long as you want to but you should look for the overflow settings to avoid conflicts.
	 */
	static String[] startingMessage = new String[] {"This is the Log file for the Thread:", "___", "Send it to Henri.", "Goodbye!"};

	/**
	 * Path to the folder that contains all log files.
	 * TODO this should be relative to avoid errors
	 */
	static String logPath = "logs";

	/**
	 * Name of the main log file.
	 */
	static String fileName = "Main";

	/**
	 * The Maximum of Lines that one log file can hold.
	 * TODO this should be independent on the startingMessage
	 */
	static int maxLinesCount = 50;

	/** 
	 * only for test runs and unimportant information
	 * @param className input for class, that called the method
	 * @param message input for message, typed by the calling class
	 **/
	public static void test(String className, String message) {
		log(className, message, logType.TEST);
	}
	
	/** only for unimportant information while program is running 
	 * 
	 * @param className input for class, that called the method
	 * @param message input for message, typed by the calling class
	 **/
	public static void debug(String className, String message) {
		log(className, message, logType.DEBUG);
	}
	
	/** only for problems, that do not make the program stop 
	 * 
	 * @param className input for class, that called the method
	 * @param message input for message, typed by the calling class
	**/
	public static void warning(String className, String message) {
		log(className, message, logType.WARNING);
	}

	/**
	 * only for issues that make the program stop
	 * 
	 * @param className input for class that called the method
	 * @param exception thrown exception that gets catched
	 */
	public static <T extends Exception> void error(String className, T exception) throws T{
		log(className, exception.getMessage(), logType.ERROR);
		if(!(exception instanceof RuntimeException)) {
			exception.printStackTrace();
		}
		throw exception;
	}
	
	public static void error(String className, String message) {
		log(className, message, logType.ERROR);
	}
	
	/**
	 * Handles log messages.
	 * @param className Cannot be longer than 20 Characters
	 * @param message Message from the function caller
	 * @param Type Type of message
	 */
	static void log(String className, String message, logType Type)
	{
		if (OutputType != outputType.NONE)
		{
			String logMessage = String.format("[%s][%-20s][%-7s]: %s", Debug.GetTime(), className, Type.toString(), message);
			
			if (OutputType == outputType.onlyConsole || OutputType == outputType.ALL) {
				printToConsole(logMessage);
			}
	
			if (OutputType == outputType.onlyFiles || OutputType == outputType.ALL) {
				printToFile(logMessage);
			}
		}
	}

	/**
	 * does all the console entries, gets parameters from the four methods
	 * @param logMessage final message that gets printed in the console
	 */
	static void printToConsole(String logMessage) {
		System.out.println(logMessage);
	}

	/**
	 * Helps to optimise the function.
	 */
	static int startCounter = 0;

	/**
	 * does all the file entries, gets parameters from the four methods
	 * @param logMessage final message that gets written to file(s)
	 */
	static void printToFile(String logMessage)
	{
		String threadName = Thread.currentThread().getName();
		
		startingMessage[1] = threadName;

		//Only checks for an existing file if it is the first run of the logger.
		if (startCounter == 0) {
			if (!Filesystem.checkFile(Filesystem.getFullFileName(logPath, threadName, Filesystem.FileEnding.log)))
				Filesystem.createFile(logPath, threadName, Filesystem.FileEnding.log, startingMessage);
			startCounter += 1;
		}

		Filesystem.appendToFile(logPath, threadName, Filesystem.FileEnding.log, logMessage, true, startingMessage.length, maxLinesCount);
	}
}

/**
 * Type of the log message.
 */
enum logType
{
	/**
	 * For test runs and unimportant information.
	 */
	TEST,
	/**
	 * For unimportant information while program is running.
	 */
	DEBUG,
	/**
	 * For problems, that do not make the program stop.
	 */
	WARNING,
	/**
	 * For issues, that make the program stop.
	 */
	ERROR
}

/**
 * Type of Visibility of log messages.
 */
enum outputType
{
	/**
	 * Log messages be gone!
	 */
	NONE,
	/**
	 * Only the console is strong enough to carry the messages out of the application!
	 */
	onlyConsole,
	/**
	 * Console is old - let us use files but only files!
	 */
	onlyFiles,
	/**
	 * Perfectly balanced (Console & Files), as all things should be.
	 */
	ALL
}

/**
 * [WIP]
 * Type of files written by Logger.
 */
enum fileType
{
	ONE,
	ALL
}