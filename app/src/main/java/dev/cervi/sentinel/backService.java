package dev.cervi.sentinel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;



public class backService extends Service {

    private boolean headsetConnected = false;
    private boolean appRunning = false;
    private boolean appActivated = true;
    private boolean secureLaunch = false;
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    public class HeadsetConnectionReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")){
                if (headsetConnected && intent.getIntExtra("state", 0) == 0){
                    headsetConnected = false;
                    Log.w("CERV1","HEADSET DISCONNECTED LOCOOO");
                } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1){
                    headsetConnected = true;
                    Log.w("CERV1","HEADSET CONNECTED LOCOOO");
                    Intent myIntent = new Intent(getApplicationContext(), PopUpActivity.class);
                    startActivity(myIntent);
                    secureLaunch = true;
                }

                if(appActivated && headsetConnected){
                    Log.w("CERV1","EMERGENCY SERVICE ON BACKGROUND");
                }
                else if(appActivated && !headsetConnected && secureLaunch){
                    Log.w("CERV1","EMERGENCY SERVICE ALARM!!!!!!!!!!");
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.w("CERV1","SERVICE CREATED");
        registerReceiver(new HeadsetConnectionReceiver(), new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if(appActivated)
                    handler.postDelayed(runnable, 500);
            }
        };
        if(appActivated)
            handler.postDelayed(runnable, 500);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Log.w("CERV1","SERVICE STOPPED");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        if(intent.getBooleanExtra("activated", true)){
            Log.w("CERV1","ACTIVATED");
            appActivated = true;
        }
        else{
            Log.w("CERV1","NOT ACTIVATED");
            appActivated = false;
        }
        return START_STICKY;
    }
}

