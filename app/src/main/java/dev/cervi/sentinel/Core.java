package dev.cervi.sentinel;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.net.URLEncoder;


public class Core extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        startService(new Intent(this, backService.class));

    }

    public void goContactsActivity(View view){
        Intent contact = new Intent(this, ContactsActivity.class);
        startActivity(contact);
    }

    public void goSettingsActivity(View view){
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }

    public void sendMessageToWhatsAppContact(View view) {
        PackageManager packageManager = this.getPackageManager();
        String number = "+34654323419";
        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode("AIUDA ME KAÍ DE LA MOTO AYY LA RECONCHA DE LA MAAADRE", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
