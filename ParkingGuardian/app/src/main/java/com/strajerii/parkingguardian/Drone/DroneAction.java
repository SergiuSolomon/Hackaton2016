package com.strajerii.parkingguardian.Drone;

public class DroneAction {
    DroneAction( EMoves eMove, MoveData mvData ) {
        moveData = mvData;
        this.gas = 0;
        this.time = 0;
    }
    public boolean shouldWait()
    {
        return eMove == EMoves.eMove;
    }
    public MoveData moveData;
    public EMoves eMove;
    public int gas;
    public int time;
}
