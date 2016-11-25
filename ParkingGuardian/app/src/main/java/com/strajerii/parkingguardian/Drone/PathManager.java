package com.strajerii.parkingguardian.Drone;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PathManager {
    public enum EMoves {
        eTakeOff,
        eLand,
        eUp,
        eDown,
        eLeft,
        eRight,
        eStartForward,
        eStopForward,
        eBackward
    }

    public class Action {
        Action( EMoves eMove, int gas, int time ) {
            this.eMove = eMove;
            this.gas = gas;
            this.time = time;
        }

        public EMoves eMove;
        public int gas;
        public int time;
    }

    private ArrayList<Action> _path =  new ArrayList<Action>();
    private int _nextActionIndex = 0;

    public PathManager() {
        FlightPath oFlightPath = new FlightPath();
        oFlightPath.getPath();
        _path.add(new Action(EMoves.eTakeOff, 10, 2000) );
        _path.add(new Action(EMoves.eStartForward, 5, 3000) );
        _path.add(new Action(EMoves.eStopForward, 0, 1000) );
        _path.add(new Action(EMoves.eLand, 5, 1000) );
    }

    public Action getNextAction() {
        Action action = _path.get(_nextActionIndex);

        _nextActionIndex++;

        return action;
    }

    public boolean hasActions() {
        return ( _nextActionIndex <= ( _path.size() - 1 ) );
    }
}
