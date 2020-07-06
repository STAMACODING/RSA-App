package com.stamacoding.rsaApp.log.filesystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Filesystem
{
    public static void createFile(String filePath, String fileName, FileEnding fileEnding)
    {
        createFile(filePath, fileName, fileEnding, null);
    }

    public static void createFile(String filePath, String fileName, FileEnding fileEnding, String firstMessage)
    {
        try {
            String fileFull = getFullFileName(filePath, fileName, fileEnding);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileFull));

            if (firstMessage != null)
            {
                writer.write(firstMessage);
            }

            writer.close();

        } catch (Exception e) {

        }
    }

    static void writeToFile()
    {

    }

    public static void appendToFile(String filePath, String fileName, FileEnding fileEnding, String Message)
    {
        String fileFull = getFullFileName(filePath, fileName, fileEnding);

        //defines the FILEWRITER
        FileWriter fileWriter = null;

        if (checkFile(fileFull) == false)
        {
            createFile(filePath, fileName, fileEnding, "firstMessage");
        }
        
		// try-catch just to make sure, all is in place
        try
        {
            fileWriter = new FileWriter(fileFull, true); //true sets property of appending text to file and not overwriting it
            
            fileWriter.write("\n" + Message); //writes the log-message
            
			fileWriter.close(); //saves the file
        } catch (IOException e)
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
        
    static void deleteFile()
    {

    }

    static void deleteLines()
    {
        
    }

    static String getFullFileName(String filePath, String fileName, FileEnding fileEnding)
    {
        return filePath + "/" + fileName + "." + fileEnding.toString();
    }

    static boolean checkFile(String fullFileName)
    {
        return new File(fullFileName).exists();
    }

    public enum FileEnding
    {
        none,
        log,
        txt,
        xml,
        json
    }
}