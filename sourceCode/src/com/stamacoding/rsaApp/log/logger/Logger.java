package com.stamacoding.rsaApp.log.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.stamacoding.rsaApp.log.debug.Debug;

public class Logger {

	public static outputType OutputType = outputType.ALL;

	public static fileType FileType = fileType.ALL;

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
	
	static void log(String className, String message, logType Type)
	{
		if (OutputType != outputType.NONE)
		{
			//Struktur für zukünftiges Arbeiten: "[" + Debug.Time() + "]" + /*"[" + status + "]" +*/ /*"[" + client/server + "]" +*/ "[" + className + "]" + "[" + Type.toString() + "]" + ": "+ "[" + message + "]";
			String logMessage = String.format("[%s][%-25s][%-7s]:[%s]", Debug.TimeFormat(), className, Type.toString(), message);
			
			if (OutputType == outputType.onlyConsole || OutputType == outputType.ALL) {
				printToConsole(logMessage);
			}
	
			if (OutputType == outputType.onlyFiles || OutputType == outputType.ALL) {
				printToFile(logMessage);
			}
		}
	}

	/**
	 * does all the console- (and maybe log-) entries, gets parameters from the four methods
	 * @param logMessage final message that gets printed in the console
	 */
	static void printToConsole(String logMessage) { // <-- sorry for that, lol
		System.out.println(logMessage);
	}

	/**
	 * does all the file entries, gets parameters from the four methods
	 * @param logMessage final message that gets written to file(s)
	 */
	static void printToFile(String logMessage) { // <-- again, looool
		//defines the FILE
		String fileName = "logFile.log"; //at the moment, the file is here: \StamaCoding\RSA-App\sourceCode\file.log
		File logFile = new File(fileName);
		if (!logFile.exists()) {
			// try-catch just to make sure, all is in place
			try {
				logFile.createNewFile(); //creates a log-file in case it is not already there
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//defines the FILEWRITER
		/* object with the log-file in it, can do thing like .exists(), .write() or .close()*/
		FileWriter logFileWriter = null;
		// try-catch just to make sure, all is in place
		try {
			logFileWriter = new FileWriter(fileName, true); //true sets property of appending text to file and not overwriting it
			logFileWriter.write("\n" + logMessage); //writes the log-message
			logFileWriter.close(); //saves the file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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