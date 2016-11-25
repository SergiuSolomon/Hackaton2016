package com.strajerii.parkingguardian.Drone;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by marioca on 11/25/2016.
 */

public class FlightPath {

    public boolean getPath()
    {
        File downloadDirectory =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "FlightPath.txt";

        File file = new File( downloadDirectory, fileName );
        try {
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            String line;
            while ( ( line = br.readLine( )) != null ) {
               Log.d( "FlightPath:", line );
            }
            br.close();
        }
        catch ( IOException e ) {
            //You'll need to add proper error handling here
            Log.d( "FlightPath:", "Error reading file", e  );
        }
        return true;
    }
}
