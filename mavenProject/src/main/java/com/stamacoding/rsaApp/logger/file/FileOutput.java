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
	private static String path = "";
	
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
		File f = new File(path);
		
		if(L.Config.File.MODE == FileMode.ONE_PER_THREAD) {
			path = "log/" + Thread.currentThread().getName() + ".log";
			f = new File(path);
			
			if(!f.exists())
				try {
					f.createNewFile();
				} catch (IOException e) {
					throw new LoggerException("Failed to create file: " + path);
				}
		}
		
		if(f.length() > L.Config.File.MAX_SIZE) {
			deleteLines(path, 0, 10000);
		}
		
		String output = entry.toString(L.Config.File.TAGGING_ENABLED);
		if(entry.getException() != null) output += entry.getStackTrace();
		
		append(path, output);
	}

	/**
	 * Initializes the file output. E.g. the log directory is cleared.
	 */
	private static void initialize() {
		deleteDirectory("log/");
		new File("log/").mkdirs();
		
		
		if (L.Config.File.MODE == FileMode.SINGLE) {
			try {
				path = "log/Logs.log";
				new File(path).createNewFile();
			} catch (IOException e) {
				throw new LoggerException("Failed to create file: " + path);
			}
		}
		initialized = true;
	}
	
	/**
	 * Deletes a directory.
	 * @param directory the directory to delete
	 */
	private static void deleteDirectory(String directory){
		
	    try {
			Files.walk(Paths.get(directory))
			  .sorted(Comparator.reverseOrder())
			  .map(Path::toFile)
			  .forEach(File::delete);
		} catch (IOException e) {
			throw new LoggerException("Failed to delete directory: " + directory);
		}
	}
	
	/**
	 * Append a string to a file.
	 * @param path the file's path
	 * @param txt the string to append
	 */
	private static void append(String path, String txt) {
		try {
			OutputStream o = Files.newOutputStream(Paths.get(path), StandardOpenOption.APPEND);
			o.write(txt.getBytes());
			o.close();
		} catch (IOException e) {
			throw new LoggerException("Failed to append string to file: " + path);
		}
	}

	/**
	 * Deletes a certain amount of lines from a file.
	 * @param path the file's path
	 * @param startline the line to start from
	 * @param numlines the amount of lines to delete
	 */
	private static void deleteLines(String path, int startline, int numlines){
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
 
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
 
			FileWriter fw=new FileWriter(new File(path));
			
			//Write entire string buffer into the file
			fw.write(sb.toString());
			fw.close();
		}
		catch (Exception e){
			throw new LoggerException("Failed to delete lines from file: " + path);
		}
	}
	
	
}
