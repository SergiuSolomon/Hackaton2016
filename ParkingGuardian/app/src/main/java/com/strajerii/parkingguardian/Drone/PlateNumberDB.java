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
        final File folder = new File(Environment.getExternalStorageDirectory() + "/ParkingGuardian/");
        String fileName = "PlateNumbersDB.txt";

        File file = new File( folder, fileName );
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
                boolean areEqual = false;

                for (String registeredPlates : _registeredPlateNumbers) {
                    if (arePlatesPartiallyEqual(plate, registeredPlates)) {
                        areEqual = true;
                        break;
                    }
                }

                if (!areEqual) {
                    foreignPlates.add(plate);
                }
            }
        }

        return foreignPlates;
    }

    public boolean isRegistered( String plate ) {
        return _registeredPlateNumbers.contains( plate );
    }

    private boolean arePlatesPartiallyEqual(String plate1, String plate2) {
        boolean arePartiallyEqual = true;

        ArrayList<String> details1 = getPlateDetail(plate1);
        ArrayList<String> details2 = getPlateDetail(plate2);

        for (int i = 0; i < 3; i++ ) {
            if (!details1.get(i).contains(details2.get(i)) &&
                !details2.get(i).contains(details1.get(i))) {
                arePartiallyEqual = false;
                break;
            }
        }

        return arePartiallyEqual;
    }

    private ArrayList<String> getPlateDetail(String plate) {
        ArrayList<String> details = new ArrayList<>();

        details.add("");
        details.add("");
        details.add("");

        int i = 0;
        for ( ; i < plate.length(); i++) {
            char c = plate.charAt(i);
            boolean isDigit = (c >= '0' && c <= '9');
            if (!isDigit) {
                details.set(0, details.get(0) + c);
            } else {
                break;
            }
        }

        for ( ; i < plate.length(); i++) {
            char c = plate.charAt(i);
            boolean isDigit = (c >= '0' && c <= '9');
            if (isDigit) {
                details.set(1, details.get(1) + c);
            } else {
                break;
            }
        }

        for ( ; i < plate.length(); i++) {
            char c = plate.charAt(i);
            boolean isDigit = (c >= '0' && c <= '9');
            if (!isDigit) {
                details.set(2, details.get(2) + c);
            } else {
                break;
            }
        }

        return details;
    }
}
