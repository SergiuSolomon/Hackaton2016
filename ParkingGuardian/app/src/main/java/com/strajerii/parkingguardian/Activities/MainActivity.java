package com.strajerii.parkingguardian.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import com.strajerii.parkingguardian.Drone.DroneDiscoverer;
import com.strajerii.parkingguardian.Drone.FlightPath;
import com.strajerii.parkingguardian.Drone.PlateNumberDB;
import com.strajerii.parkingguardian.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_DEVICE_SERVICE = "EXTRA_DEVICE_SERVICE";

    public DroneDiscoverer droneDiscoverer = null;
    public ARDiscoveryDeviceService droneService = null;

    // this block loads the native libraries
    // it is mandatory
    static {
        ARSDK.loadSDKLibs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button droneBtn = (Button) findViewById(R.id.droneBtn);
        droneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (droneService != null) {
                    Intent intent = new Intent(MainActivity.this, BebopActivity.class);
                    intent.putExtra(EXTRA_DEVICE_SERVICE, droneService);
                    startActivity(intent);
                }
            }
        });

        Button checkPlates = (Button) findViewById(R.id.checkPlatesBtn);
        checkPlates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlateNumberDB platesDB = new PlateNumberDB();

                ArrayList<String> currentPlates = new ArrayList<>();
                currentPlates.add("BV-26-MJK");
                currentPlates.add("BV-27-MJK");
                currentPlates.add("BV-28-MJK");

                ArrayList<String> foreignPlates = platesDB.checkPlates(currentPlates);
            }
        });

        // create drone discoverer
        droneDiscoverer = new DroneDiscoverer(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (droneDiscoverer != null) {
            // setup the drone discoverer and register as listener
            droneDiscoverer.setup();
            droneDiscoverer.addListener(mDiscovererListener);

            // start discovering
            droneDiscoverer.startDiscovering();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // clean the drone discoverer object
        if (droneDiscoverer != null) {
            droneDiscoverer.stopDiscovering();
            droneDiscoverer.cleanup();
            droneDiscoverer.removeListener(mDiscovererListener);
        }
    }

    private final DroneDiscoverer.Listener mDiscovererListener = new  DroneDiscoverer.Listener() {

        @Override
        public void onDronesListUpdated(List<ARDiscoveryDeviceService> dronesList) {
            if ( dronesList.size() == 1 ) {
                droneService = dronesList.get(0);
                Button droneBtn = (Button) findViewById(R.id.droneBtn);
                droneBtn.setText(droneService.getName());
            }
        }
    };
}