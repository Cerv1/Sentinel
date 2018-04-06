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
    private boolean appActivated = true;
    private boolean secureLaunch = false;
    public Handler handler = null;
    public static Runnable runnable = null;

    public class HeadsetConnectionReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Log.w("CERV1", "onReceived connected");
            if (intent.hasExtra("state")){
                if (headsetConnected && intent.getIntExtra("state", 0) == 0){
                    headsetConnected = false;
                    Log.w("CERV1","HEADSET DISCONNECTED");
                } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1){
                    headsetConnected = true;
                    Log.w("CERV1","HEADSET CONNECTED");
                    Intent myIntent = new Intent(getApplicationContext(), PopUpActivity.class);
                    startActivity(myIntent);
                    secureLaunch = true;
                }
                if(appActivated && headsetConnected){
                    Log.w("CERV1","EMERGENCY SERVICE ON BACKGROUND");
                }
                else if(appActivated && !headsetConnected && secureLaunch){
                    Intent emergency = new Intent(getApplicationContext(), EmergencyActivity.class);
                    startActivity(emergency);
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
        startService(new Intent(getApplicationContext(), backService.class));
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, 0);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        startService(new Intent(getApplicationContext(), backService.class));
        Log.w("CERV1","SERVICE STOPPED");
    }

    @Override
    public void onTaskRemoved(Intent root_intent){
        stopService(new Intent(getApplicationContext(), backService.class));
        sendBroadcast(new Intent(this, backService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
            if (intent.getBooleanExtra("activated", false)) {
                Log.w("CERV1", "ACTIVATED");
                appActivated = true;
            } else {
                Log.w("CERV1", "NOT ACTIVATED");
                appActivated = false;
            }
        return START_STICKY;
    }
}

