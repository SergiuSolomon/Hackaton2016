package com.strajerii.parkingguardian.Drone;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by marioca on 11/25/2016.
 */

public class FlightPath {

    public boolean getPath( ArrayList<DroneAction> actionPath )
    {
        File downloadDirectory =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "FlightPath.txt";

        File file = new File( downloadDirectory, fileName );
        try {
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            String line;
            while ( ( line = br.readLine( )) != null ) {
                String split[]= line.split( "\\s+" );
                EMoves eMoveToAdd = EMoves.eTakeOff;
                for ( EMoves  eMove : EMoves.values()  ) {
                    if( Objects.equals( eMove.toString(), split[0] ) ) {
                        eMoveToAdd = eMove;
                    }
                }
                int nPos = Integer.getInteger( split[1] );
                int nTime = Integer.getInteger( split[2] );
                actionPath.add( new DroneAction( eMoveToAdd, nPos, nTime ) );
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
