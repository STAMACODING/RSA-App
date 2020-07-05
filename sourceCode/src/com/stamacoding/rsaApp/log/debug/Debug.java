package com.stamacoding.rsaApp.log.debug;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.stamacoding.rsaApp.log.logger.Logger;

public class Debug
{
    public static String ClientID;

    public static State State;

    /**
     * Only for testing purposes
     * @param args
     */
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            //Logger.debug(Debug.class.getSimpleName(), "Wer das liest hat Probleme");
            Logger.debug(Debug.class.getSimpleName(), GetTime());
            Thread.sleep(42);
        }

        //Filesystem.createFile();
    }

    /**
     * Returns current Time [yyyy'-'MM'-'dd'T'HH':'mm':'ss','SSZ]
     * @return current Time
     */
    public static String GetTime()
    {
        DateFormat dfmt_1 = new SimpleDateFormat("yyyy'-'MM'-'dd'|'HH':'mm':'ss','SS");

        DateFormat dfmt_2 = new SimpleDateFormat("Z");

        String Time = String.format("%-23s|%.3s", dfmt_1.format(new Date()), dfmt_2.format(new Date()));

        return Time;
    }
}

/**
 * Current state of the application.
 */
enum State
{
    Initialize,
    Running
}