package dev.cervi.sentinel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.EditText;


public class Core extends AppCompatActivity {

    private boolean headsetConnected = false;

    public static class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("CONNECTED")
                    .setPositiveButton("ACTIVAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public class HeadsetConnectionReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            Log.w("CERV1", "ACTION_HEADSET_PLUG Intent received shure");
            if (intent.hasExtra("state")){
                if (headsetConnected && intent.getIntExtra("state", 0) == 0){
                    headsetConnected = false;
                    Log.w("CERV1","HEADSET DISCONNECTED LOCOOO");
                } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1){
                    headsetConnected = true;
                    Log.w("CERV1","HEADSET CONNECTED LOCOOO");
                    FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();
                    FragmentManager fm = getFragmentManager();
                    dialog.show(fm, "Sample");
                    Intent intentaso = new Intent(getApplicationContext(), PopUpActivity.class);
                    startActivity(intentaso);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        registerReceiver(new HeadsetConnectionReceiver(), new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }

}
