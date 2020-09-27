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

import com.stamacoding.rsaApp.logger.LoggerException;

public class FileUtils {
	public static void deleteDirectory(String directory){
		
	    try {
			Files.walk(Paths.get(directory))
			  .sorted(Comparator.reverseOrder())
			  .map(Path::toFile)
			  .forEach(File::delete);
		} catch (IOException e) {
			throw new LoggerException("Failed to delete directory: " + directory);
		}
	}
	public static void append(String path, String txt) {
		try {
			OutputStream o = Files.newOutputStream(Paths.get(path), StandardOpenOption.APPEND);
			o.write(txt.getBytes());
			o.close();
		} catch (IOException e) {
			throw new LoggerException("Failed to append string to file: " + path);
		}
	}

	public static void deleteLines(String path, int startline, int numlines){
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
