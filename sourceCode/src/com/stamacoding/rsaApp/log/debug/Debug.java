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
            Thread.sleep(1000);
        }

        //Filesystem.createFile();
    }

    /**
     * Returns current Time [yyyy'-'MM'-'dd'T'HH':'mm':'ss','SSZ]
     * @return current Time
     */
    public static String GetTime()
    {
        DateFormat dfmt = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss','SSZ");

        return dfmt.format(new Date());
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