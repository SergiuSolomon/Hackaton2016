package com.strajerii.parkingguardian.Drone;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PlateNumberDB {
    private ArrayList<String> _registeredPlateNumbers =  new ArrayList<>();

    public PlateNumberDB() {
        readRegisteredPlateNumbersFormFile();
    }

    private void readRegisteredPlateNumbersFormFile() {
        File downloadDirectory =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "PlateNumbersDB.txt";

        File file = new File( downloadDirectory, fileName );
        try {
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            String line;
            while ( ( line = br.readLine( )) != null ) {
                _registeredPlateNumbers.add( line );
            }
            br.close();
        }
        catch ( IOException e ) {
            Log.d( "PLateNumbers:", "Error reading file", e  );
        }
    }

    public ArrayList<String> checkPlates(ArrayList<String> plates) {
        ArrayList<String> foreignPlates = new ArrayList<>();

        for (String plate : plates) {
            if ( !_registeredPlateNumbers.contains( plate ) ) {
                foreignPlates.add(plate);
            }
        }

        return foreignPlates;
    }

    public boolean isRegistered( String plate ) {
        return _registeredPlateNumbers.contains( plate );
    }
}
