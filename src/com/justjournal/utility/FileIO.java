
package com.justjournal.utility;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jul 11, 2003
 * Time: 10:05:02 PM
 * To change this template use Options | File Templates.
 */
public final class FileIO
{

    public static String ReadTextFile( String FilePath )
            throws IOException
    {
        int myC;
        StringWriter myInput = new StringWriter();
        FileReader myFR = new FileReader( FilePath );
        myC = myFR.read();

        while ( myC != -1 ) {
            myInput.write( myC );
            // System.out.print( myC );  // debug
            myC = myFR.read();
        }

        myFR.close();

        return myInput.toString();
    }

    public static void WriteTextFile( String FilePath, String DataToWrite )
            throws IOException
    {
        FileWriter myFW = new FileWriter( FilePath, false );
        myFW.write( DataToWrite );
        myFW.close();

        return;
    }
}
