package com.stamacoding.rsaApp.log.filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.stamacoding.rsaApp.log.debug.Debug;
import com.stamacoding.rsaApp.log.logger.Logger;

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
        appendToFile(filePath, fileName, fileEnding, Message, false, 0, 0);
    }

    public static void appendToFile(String filePath, String fileName, FileEnding fileEnding, String Message, boolean checkOverflow, int maxLines, int offset)
    {
        String fileFull = getFullFileName(filePath, fileName, fileEnding);

        //defines the FILEWRITER
        FileWriter fileWriter = null;

        if (checkFile(fileFull) == false)
        {
            createFile(filePath, fileName, fileEnding, "firstMessage");
        }

        if (checkOverflow) 
        {
            if (getFileLength(fileFull) > maxLines * 100) {
                deleteLines(filePath, fileName, fileEnding, maxLines, offset);
            }
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
        
    public static void deleteFile(String filePath, String fileName, FileEnding fileEnding)
    {
        String fileFull = getFullFileName(filePath, fileName, fileEnding);

        if (checkFile(fileFull) == true)
        {
            new File(fileFull).delete();
        }
    }

    public static void deleteLines(String filePath, String fileName, FileEnding fileEnding, int startLine, int endLine)
    {
        String fileFull = getFullFileName(filePath, fileName, fileEnding);

        long Length = new File (fileFull).length();

        //Logger.debug(Debug.class.getSimpleName(), Length+"");
        System.out.println(Length+"");

        List<String> allLines = readLines(filePath, fileName, fileEnding);

        clearFile(filePath, fileName, fileEnding);

        int counter = 0;

        for (String Line : allLines)
        {
            if (counter <= startLine && counter >= endLine) {
                appendToFile(filePath, fileName, fileEnding, Line);
            }

            counter += 1;
        }
    }

    public static void clearFile(String filePath, String fileName, FileEnding fileEnding)
    {
        deleteFile(filePath, fileName, fileEnding);

        createFile(filePath, fileName, fileEnding);
    }

    public static List<String> readLines(String filePath, String fileName, FileEnding fileEnding)
    {
        String fileFull = getFullFileName(filePath, fileName, fileEnding);

        List<String> allLines = new ArrayList<String>();

        try (BufferedReader in = Files.newBufferedReader(Paths.get(fileFull), StandardCharsets.ISO_8859_1 ) ) {
            for (String line; (line = in.readLine()) != null;)
            {
                allLines.add(line);

                //System.out.println("Read:" + line);
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return allLines;
    }

    public static String getFullFileName(String filePath, String fileName, FileEnding fileEnding)
    {
        return filePath + "/" + fileName + "." + fileEnding.toString();
    }

    static boolean checkFile(String fullFileName)
    {
        return new File(fullFileName).exists();
    }

    static long getFileLength(String fullFileName)
    {
        return new File(fullFileName).length();
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