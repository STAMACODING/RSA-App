package com.stamacoding.rsaApp.log.logger;

import java.io.File;

import com.stamacoding.rsaApp.log.debug.Debug;
import com.stamacoding.rsaApp.log.filesystem.Filesystem;

public class Logger {
	
	static{
		// Create "logs" directory automatically
		File f = new File("logs");
		f.mkdirs();
	}

	public static outputType OutputType = outputType.ALL;

	public static fileType FileType = fileType.ALL;

	static String startingMessage = "Hello";

	static String logPath = "logs";

	static String fileName = "Main";

	/** only for test runs and unimportant information 
	 * 
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
	 * only for issues, that make the program stop 
	 * 
	 * @param className input for class, that called the method
	 * @param message input for message, typed by the calling class
	 **/
	public static void error(String className, String message) {
		log(className, message, logType.ERROR);
	}
	
	/**
	 * 
	 * @param className Cannot be longer than 20 Characters
	 * @param message
	 * @param Type
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
				//printToFile(logMessage);
			}
		}
	}

	/**
	 * does all the console- (and maybe log-) entries, gets parameters from the four methods
	 * @param logMessage final message that gets printed in the console
	 */
	static void printToConsole(String logMessage) {
		System.out.println(logMessage);
	}

	/**
	 * does all the file entries, gets parameters from the four methods
	 * @param logMessage final message that gets written to file(s)
	 */
	static void printToFile(String logMessage)
	{
		Filesystem.appendToFile(logPath, fileName, Filesystem.FileEnding.log, logMessage, true, 20, 1);
	}
}

enum logType
{
	TEST,
	DEBUG,
	WARNING,
	ERROR
}

enum outputType
{
	NONE,
	onlyConsole,
	onlyFiles,
	ALL
}

enum fileType
{
	ONE,
	ALL
}