package com.strajerii.parkingguardian.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import com.strajerii.parkingguardian.Drone.DroneDiscoverer;
import com.strajerii.parkingguardian.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public DroneDiscoverer mDroneDiscoverer;

    // this block loads the native libraries
    // it is mandatory
    static {
        ARSDK.loadSDKLibs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create drone discoverer
        mDroneDiscoverer = new DroneDiscoverer(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (mDroneDiscoverer != null) {
            // setup the drone discoverer and register as listener
            mDroneDiscoverer.setup();
            mDroneDiscoverer.addListener(mDiscovererListener);

            // start discovering
            mDroneDiscoverer.startDiscovering();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // clean the drone discoverer object
        if (mDroneDiscoverer != null) {
            mDroneDiscoverer.stopDiscovering();
            mDroneDiscoverer.cleanup();
            mDroneDiscoverer.removeListener(mDiscovererListener);
        }
    }

    private final DroneDiscoverer.Listener mDiscovererListener = new  DroneDiscoverer.Listener() {

        @Override
        public void onDronesListUpdated(List<ARDiscoveryDeviceService> dronesList) {
            int it = 0;
        }
    };
}