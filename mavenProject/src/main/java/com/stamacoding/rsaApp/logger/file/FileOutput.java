package com.stamacoding.rsaApp.logger.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.logger.LogEntry;
import com.stamacoding.rsaApp.logger.LoggerException;

/**
 * The logger's file output. Use {@link #print(LogEntry)} to write a log entry to the current log file.
 */
public class FileOutput {
	/**
	 * The path of the current log file
	 */
	private static File logFile = null;
	
	/**
	 * Stores whether the file output got initialized
	 * @see #initialize()
	 */
	private static boolean initialized = false;
	
	/**
	 * Writes a log entry to the current log file.
	 * @param entry the entry to log
	 */
	public static void write(LogEntry entry) {
		if(!initialized) initialize();

		if(L.Config.File.MODE == FileMode.ONE_PER_THREAD) 
			logFile = new File("log/" + Thread.currentThread().getName() + ".log");
		else
			logFile = new File("log/Logs.log");

		if(!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new LoggerException("Failed to create file: " + logFile.getPath());
			}
		}
		
		if(logFile.length() > L.Config.File.MAX_SIZE) {
			deleteLines(0, 10000);
		}
		
		String output = entry.toString(L.Config.File.TAGGING_ENABLED);
		if(entry.getException() != null) output += entry.getStackTrace();
		
		append(output);
	}

	/**
	 * Initializes the file output. E.g. the log directory is cleared.
	 */
	private static void initialize() {
		deleteLogDirectory();
		new File("log/").mkdirs();
		initialized = true;
	}
	
	/**
	 * Deletes a directory.
	 * @param directory the directory to delete
	 */
	private static void deleteLogDirectory(){
		if(new File("log/").exists()) {
			 try {
					Files.walk(Paths.get("log/"))
					  .sorted(Comparator.reverseOrder())
					  .map(Path::toFile)
					  .forEach(File::delete);
				} catch (IOException e) {
					e.printStackTrace();
					throw new LoggerException("Failed to delete log directory (log/)");
				}
		}
	}
	
	/**
	 * Append a string to a file.
	 * @param path the file's path
	 * @param txt the string to append
	 */
	private static void append(String txt) {
		try {
			OutputStream o = Files.newOutputStream(logFile.toPath(), StandardOpenOption.APPEND);
			o.write(txt.getBytes());
			o.close();
		} catch (IOException e) {
			throw new LoggerException("Failed to append string to log file (" + logFile.getPath() + ")! Did you close the file?");
		}
	}

	/**
	 * Deletes a certain amount of lines from a file.
	 * @param path the file's path
	 * @param startline the line to start from
	 * @param numlines the amount of lines to delete
	 */
	private static void deleteLines(int startline, int numlines){
		try{
			BufferedReader br = new BufferedReader(new FileReader(logFile));
 
			//String buffer to store contents of the file
			StringBuffer sb=new StringBuffer("");
 
			//Keep track of the line number
			int linenumber = 0;
			String line;
 
			while((line=br.readLine())!=null){
				if(linenumber<startline||linenumber>startline+numlines)
					sb.append(line+"\n");
				linenumber++;
			}
			br.close();
 
			FileWriter fw = new FileWriter(logFile);
			
			//Write entire string buffer into the file
			fw.write(sb.toString());
			fw.close();
		}
		catch (Exception e){
			throw new LoggerException("Failed to delete lines from log file: " + logFile.getPath());
		}
	}
	
	
}
