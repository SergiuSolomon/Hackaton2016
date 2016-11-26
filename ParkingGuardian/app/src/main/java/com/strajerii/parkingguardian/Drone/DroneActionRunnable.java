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
        while(_pathManager.hasActions()) {
            DroneAction action = _pathManager.getNextAction();
            doAction(action);
            try {
                Thread.sleep(action.time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void takeOffOrLand() {
        switch (mBebopDrone.getFlyingState()) {
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                mBebopDrone.takeOff();
                break;
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
            case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                mBebopDrone.land();
                break;
            default:
        }
    }

    private void doAction( DroneAction action ) {
        Log.d("Bebop activity", "action " );

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
                mBebopDrone.setGaz((byte) 0);
                mBebopDrone.setPitch((byte) 0);
                mBebopDrone.setFlag((byte) 0);
                mBebopDrone.setYaw((byte) 0);
                mBebopDrone.setRoll((byte) 0);

                mBebopDrone.takePicture();
                break;

            //up
            case eStartUp: {
                Log.d("Bebop activity", "action start up" );
                mBebopDrone.setGaz((byte) action.gas);
            }
            break;
            case eStopUp: {
                Log.d("Bebop activity", "action stop up" );
                mBebopDrone.setGaz((byte) 0);
            }
            break;

            //down
            case eStartDown: {
                mBebopDrone.setGaz((byte) -action.gas);
            }
            break;
            case eStopDown: {
                mBebopDrone.setGaz((byte) 0);
            }
            break;

            //forward
            case eStartForward: {
                mBebopDrone.setPitch((byte) action.gas);
                mBebopDrone.setFlag((byte) 1);
            }
            break;
            case eStopForward: {
                mBebopDrone.setPitch((byte) 0);
                mBebopDrone.setFlag((byte) 0);
            }
            break;

            //backward
            case eStartBackward: {
                mBebopDrone.setPitch((byte) -action.gas);
                mBebopDrone.setFlag((byte) 1);
            }
            break;
            case eStopBackward: {
                mBebopDrone.setPitch((byte) 0);
                mBebopDrone.setFlag((byte) 0);
            }
            break;

            // yaw left
            case eStartYawLeft: {
               Log.d("Bebop activity", "action start yaw left" );
                mBebopDrone.setYaw((byte) -action.gas);
            }
            break;
            case eStopYawLeft: {
               Log.d("Bebop activity", "action stop yaw left" );
                mBebopDrone.setYaw((byte) 0);
            }
            break;

            //yaw right
            case eStartYawRight: {
                mBebopDrone.setYaw((byte) action.gas);
            }
            break;
            case eStopYawRight: {
                mBebopDrone.setYaw((byte) 0);
            }
            break;

            // roll left
            case eStartRollLeft: {
                mBebopDrone.setRoll((byte) -action.gas);
                mBebopDrone.setFlag((byte) 1);
            }
            break;
            case eStopRollLeft: {
                mBebopDrone.setRoll((byte) 0);
                mBebopDrone.setFlag((byte) 0);
            }
            break;

            //roll right
            case eStartRollRight: {
                mBebopDrone.setRoll((byte) action.gas);
                mBebopDrone.setFlag((byte) 1);
            }
            break;
            case eStopRollRight: {
                mBebopDrone.setRoll((byte) 0);
                mBebopDrone.setFlag((byte) 0);
            }
            break;
        }
    }
}
