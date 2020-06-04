package com.stamacoding.rsaApp.log.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;

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
			String logMessage = "[" + Debug.Time() + "]" + /*"[" + status + "]" +*/ /*"[" + client/server + "]" +*/ "[" + className + "]" + "[" + Type.toString() + "]" + ": "+ "[" + message + "]";

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
		String fileName = "logFile.txt"; //at the moment, the file is here: \StamaCoding\RSA-App\sourceCode\file.log
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
			logFileWriter = new FileWriter(fileName);
			String data = read(fileName, logFile); //ISSUE: method cannot read the things from log-file
			logFileWriter.write(data + logMessage); //writes the things, that are already there, and the log-message
			logFileWriter.close(); //saves the file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * returns all the things that are already in the log-file
	 * @param logFile name of the log-file
	 */
	static String read(String fileName, File logFile) {
		String data = "";
		try {
			Scanner scanFile = new Scanner(new File("C:\\Users\\Admin\\Documents\\Bene privat\\Programmieren\\Git\\StamaCoding\\RSA-App\\sourceCode\\logFile.txt"));
			while (scanFile.hasNextLine()){
				data += scanFile.nextLine(); //reads the data from the log-file with a scanner
				System.out.println("Daten: " + data);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
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