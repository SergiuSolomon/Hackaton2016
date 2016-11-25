package com.strajerii.parkingguardian.Drone;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PathManager {

    private ArrayList<DroneAction> _path =  new ArrayList<DroneAction>();
    private int _nextActionIndex = 0;

    public PathManager() {
        FlightPath oFlightPath = new FlightPath();
        oFlightPath.getPath( _path );
    }

    public DroneAction getNextAction() {
        DroneAction action = _path.get(_nextActionIndex);

        _nextActionIndex++;

        return action;
    }

    public boolean hasActions() {
        return ( _nextActionIndex <= ( _path.size() - 1 ) );
    }

    public void resetToStart() {
        _nextActionIndex = 0;
    }
}
