package com.strajerii.parkingguardian.Drone;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by marioca on 11/25/2016.
 */

public class FlightPath {

    public boolean getPath( ArrayList<DroneAction> droneActions )
    {
        MoveData dummyData = new MoveData( 0, 0, 0, 0, 500 );

        final File folder = new File(Environment.getExternalStorageDirectory() + "/ParkingGuardian/");
        String fileName = "FlightPath.txt";

        File file = new File( folder, fileName );
        try {
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            String line;
            while ( ( line = br.readLine( )) != null ) {
                String split[]= line.split( "\\s+" );
                float nX = Float.parseFloat( split[0] ) / 1000;
                float nY = Float.parseFloat( split[1] ) / 1000;
                float nZ = Float.parseFloat( split[2] ) / 1000;
                float nRadAngle = Float.parseFloat( split[3] ) / 1000;
                int nSleep = Integer.parseInt( split[4] );
                MoveData data = new MoveData( nX, nY, nZ, nRadAngle, nSleep );
                droneActions.add( new DroneAction( EMoves.eMove, data ) );
                droneActions.add( new DroneAction( EMoves.eTakePicture, dummyData ));
            }
            br.close();
        }
        catch ( IOException e ) {
            //You'll need to add proper error handling here
            Log.d( "FlightPath:", "Error reading file", e  );
        }

        droneActions.add( new DroneAction( EMoves.eLand, dummyData ) );

        return true;
    }
}