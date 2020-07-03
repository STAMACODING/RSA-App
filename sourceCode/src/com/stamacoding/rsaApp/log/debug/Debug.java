package com.stamacoding.rsaApp.log.debug;

import java.text.DecimalFormat;
import java.time.OffsetDateTime;

//import com.stamacoding.rsaApp.log.filesystem.Filesystem;
import com.stamacoding.rsaApp.log.logger.Logger;

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
        //return String.format("%.2f", OffsetDateTime.now());//Zeit wird auf 3 Nachkommastellen gerundet
    };
    
    /**
     * 
     * @return current time as formatted time (yyyy-mm-ddThh:mm:ss:mmmm+zz:zz) (date-time-time zone)
     */
    public static String TimeFormat()
    {
        //#region Variables

        String time = Time().toString();
        
        double timeDouble = 0;
        
        int timeLen = time.length();
        
        DecimalFormat df = new DecimalFormat("##.###"); //formatiert Zeit auf 4 Nachkommastellen(letzte ist immer 0)
        
        StringBuilder saveTime = new StringBuilder(time);
        
        StringBuilder timeB = new StringBuilder(time); //Zeit wird in StringBuilder gegeben
        
        //#endregion

        //#region  Time

        for(int i = 0; i < 17; i++) { //erste 17 Stellen (Datum, Stunden, Minuten) werden geloescht
    		timeB.deleteCharAt(0);
        }
        
    	for(int i = 0; i <= 5; i++) { //letzte 5 Stellen (Zeitzone) werden geloescht
    		timeB.deleteCharAt(timeB.length()-1);
        }
        
        time = timeB.toString();
        
        timeDouble = Double.parseDouble(time);
        
        String ret = saveTime.replace(17, timeLen - 7, df.format(timeDouble)).toString(); //alte Sekunden werden durch formatierte Sekunden ersetzt
        
        return ret; //DecimalFormat formatiert Double zu String mit 3 Nachkommastellen

        //#endregion
    }

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