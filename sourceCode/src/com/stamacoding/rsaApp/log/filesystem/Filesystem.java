package com.stamacoding.rsaApp.log.filesystem;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Filesystem
{
    public static void createFile(){
        try {
            String testFile = "TestFile.txt";

            String testString = "Hello World";

            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));

            writer.write(testString);

            writer.close();
        } catch (Exception e) {

        }
    }   

    static void writeToFile()
    {

    }

    static void appendToFile()
    {

    }
        
    static void deleteFile()
    {

    }

    static void deleteLines()
    {
        
    }
}