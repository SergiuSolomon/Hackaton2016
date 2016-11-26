package com.strajerii.parkingguardian.Drone;

import android.util.Log;

public class DroneActionRunnable implements Runnable
{
    BebopDrone mBebopDrone;
    PathManager _pathManager;

    public DroneActionRunnable(BebopDrone drone, PathManager pathManager)
    {
        mBebopDrone = drone;
        _pathManager = pathManager;
    }

    @Override
    public void run()
    {
        while(_pathManager.hasActions())
        {
            DroneAction action = _pathManager.getNextAction();
            doAction( action );

            try {
                Thread.sleep( action.moveData._nSleep );
            } catch (InterruptedException e) {
                Log.d( "Bebop activity", "Sleep failed", e );
            }

            if( action.shouldWait() ){
                //we need to wait drone to finish the given action
                mBebopDrone.waitDrone();
            }
        }
    }

    private void takeOffOrLand() {
        switch (mBebopDrone.getFlyingState()) {
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                Log.d("Bebop activity", "state landed" );
                mBebopDrone.takeOff();
                break;
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                Log.d("Bebop activity", "state flying" );
                mBebopDrone.land();
                break;
            default:
                Log.d("Bebop activity", "Unknown state" );
        }
    }

    private void doAction( DroneAction action ) {
        switch( action.eMove ) {
            case eTakeOff:
                Log.d("Bebop activity", "action take off" );
                takeOffOrLand();
                break;
            case eLand:
                Log.d("Bebop activity", "action land" );
                takeOffOrLand();
                break;

            //take picture
            case eTakePicture:
                Log.d("Bebop activity", "action take picture" );
                mBebopDrone.takePicture();
                break;

            //up
            case eMove: {
                Log.d("Bebop activity", "action move" );
                mBebopDrone.moveBy( action.moveData );
            }
            break;
        }
    }
}
