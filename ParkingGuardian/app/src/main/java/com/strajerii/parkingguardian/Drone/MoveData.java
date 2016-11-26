package com.strajerii.parkingguardian.Drone;

public class MoveData
{
    MoveData (float dX, float dY, float dZ, float dPsi){
        this._dx = dX;
        this._dy = dY;
        this._dz = dZ;
        this._dPsi = dPsi;
    }
    public float _dx;
    public float _dy;
    public float _dz;
    public float _dPsi;
}