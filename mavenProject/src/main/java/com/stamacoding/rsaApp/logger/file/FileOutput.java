package com.stamacoding.rsaApp.logger.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.logger.LogEntry;

public class FileOutput {
	private static String path = "";
	private static boolean initialized = false;
	
	public static void write(LogEntry entry) {
		if(!initialized) initialize();
		File f = new File(path);
		
		if(L.CONFIG.getFileMode() == FileMode.ONE_PER_THREAD) {
			path = "log/" + Thread.currentThread().getName() + ".log";
			f = new File(path);
			
			if(!f.exists())
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
		}
		
		if(f.length() > L.CONFIG.getMaxFileSize()) {
			FileUtils.deleteLines(path, 0, 10000);
		}
		
		String output = entry.toString();
		if(entry.getException() != null) output += entry.getStackTrace();
		
		FileUtils.append(path, output);
	}

	private static void initialize() {
		FileUtils.deleteDirectory("log/");
		new File("log/").mkdirs();
		
		
		if (L.CONFIG.getFileMode() == FileMode.SINGLE) {
			try {
				path = "log/Logs.log";
				new File(path).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		initialized = true;
	}
	
	
}
