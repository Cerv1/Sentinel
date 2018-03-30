package dev.cervi.sentinel;

import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class PopUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.w("CERV1","POPUP LAUNCHED");
        final AlertDialog LDialog = new AlertDialog.Builder(this)
                .setTitle("Activar Sentinel")
                .setMessage("¿Quieres activar el mecanismo de protección de Sentinel: Ride Safe?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w("CERV1","POSITIVE BUTTON PRESSED");
                        Intent mIntent = new Intent(getApplicationContext(), backService.class);
                        mIntent.putExtra("activated", true);
                        getApplicationContext().startService(mIntent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.w("CERV1","NEGATIVE BUTTON PRESSED");
                        Intent mIntent = new Intent(getApplicationContext(), backService.class);
                        mIntent.putExtra("activated", false );
                        getApplicationContext().startService(mIntent);
                    }
                }).create();
        LDialog.show();
    }
}

