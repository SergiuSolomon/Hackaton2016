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

        eStartUp,
        eStopUp,

        eStartDown,
        eStopDown,

        eStartForward,
        eStopForward,

        eStartBackward,
        eStopBackward,

        eStartYawLeft,
        eStopYawLeft,

        eStartYawRight,
        eStopYawRight,

        eStartRollLeft,
        eStopRollLeft,

        eStartRollRight,
        eStopRollRight,
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
        int defaultPower = 10;

        _path.add(new Action(EMoves.eTakeOff, 10, 2000) );

        _path.add(new Action(EMoves.eStartUp, defaultPower, 1500) );
        _path.add(new Action(EMoves.eStopUp, 0, 1000) );

        _path.add(new Action(EMoves.eStartDown, defaultPower, 1500) );
        _path.add(new Action(EMoves.eStopDown, 0, 1000) );

        _path.add(new Action(EMoves.eStartForward, defaultPower, 3000) );
        _path.add(new Action(EMoves.eStopForward, 0, 1000) );

        _path.add(new Action(EMoves.eStartBackward, defaultPower, 3000) );
        _path.add(new Action(EMoves.eStopBackward, 0, 1000) );

        _path.add(new Action(EMoves.eStartYawLeft, defaultPower, 3000) );
        _path.add(new Action(EMoves.eStopYawLeft, 0, 1000) );

        _path.add(new Action(EMoves.eStartYawRight, defaultPower, 3000) );
        _path.add(new Action(EMoves.eStopYawRight, 0, 1000) );

        _path.add(new Action(EMoves.eStartRollLeft, defaultPower, 3000) );
        _path.add(new Action(EMoves.eStopRollLeft, 0, 1000) );

        _path.add(new Action(EMoves.eStartRollRight, defaultPower, 3000) );
        _path.add(new Action(EMoves.eStopRollRight, 0, 1000) );

        _path.add(new Action(EMoves.eLand, defaultPower, 1000) );
    }

    public Action getNextAction() {
        Action action = _path.get(_nextActionIndex);

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
