package com.strajerii.parkingguardian.Drone;

public class DroneAction {
    DroneAction( EMoves eMove, int gas, int time ) {
        this.eMove = eMove;
        this.gas = gas;
        this.time = time;
    }

    public EMoves eMove;
    public int gas;
    public int time;
}
