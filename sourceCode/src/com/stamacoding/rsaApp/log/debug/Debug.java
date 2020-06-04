package com.stamacoding.rsaApp.log.debug;

import java.time.OffsetDateTime;

import com.stamacoding.rsaApp.log.filesystem.Filesystem;
import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.log.filesystem.Filesystem;

public class Debug
{
    public static String ClientID;

    public static State State;

    /**
     * Returns the current time in the ISO-8601 system.
     * @return current time
     */
    public static OffsetDateTime Time()
    {
        return OffsetDateTime.now();
    };

    /**
     * Only for testing purposes
     * @param args
     */
    public static void main(String[] args) {
        Logger.debug(Debug.class.getSimpleName(), "Wer das liest hat Probleme");

        //Filesystem.createFile();
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