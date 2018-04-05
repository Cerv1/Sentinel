package dev.cervi.sentinel;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;


public class EmergencyActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MAX_SMS_MESSAGE_LENGTH = 160;
    private static final Object SMS_PORT = 50000;
    private GoogleApiClient googleApiClient;
    private String maps_location="";
    private int secs_to_alarm = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        SharedPreferences settings_pref = getApplicationContext().getSharedPreferences("settings_pref", 0);

        Log.w("CERV1", settings_pref.getString("emergency_message", "default"));

        if(settings_pref.getString("emergency_message", "default") != "default"){
            maps_location = settings_pref.getString("emergency_message", "default");
        }

        if(settings_pref.getString("emergency_timer", "default") != "default"){
            secs_to_alarm = Integer.parseInt(settings_pref.getString("emergency_timer", "default"));
            Log.w("CERV1", settings_pref.getString("emergency_timer", "default"));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.v("CERV1", "BAD PERMISSIONS, CRASHING");
            return;
        } else {
            googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
            googleApiClient.connect();
        }


        final TextView _tv = (TextView) findViewById(R.id.countdown_text);
        final CountDownTimer myCountDown = new CountDownTimer(secs_to_alarm*1000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                _tv.setText(String.format("%s", String.format("%d min, %d sec",
                        MILLISECONDS.toMinutes(millisUntilFinished),
                        MILLISECONDS.toSeconds(millisUntilFinished) - MINUTES.toSeconds(MILLISECONDS.toMinutes(millisUntilFinished)))));
            }

            public void onFinish() {
                _tv.setText("SMS SENT");
                Log.w("CERV1", maps_location);
                ArrayList<String> numbers = new ArrayList<String>();

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("contacts_pref", 0);

                if(sharedPref.getString("first_contact_phoneNo", "default") != "default"){
                    numbers.add(sharedPref.getString("first_contact_phoneNo", "default"));
                }
                if(sharedPref.getString("second_contact_phoneNo", "default") != "default"){
                    numbers.add(sharedPref.getString("second_contact_phoneNo", "default"));
                }
                if(sharedPref.getString("third_contact_phoneNo", "default") != "default"){
                    numbers.add(sharedPref.getString("third_contact_phoneNo", "default"));
                }
                if(sharedPref.getString("fourth_contact_phoneNo", "default") != "default"){
                    numbers.add(sharedPref.getString("fourth_contact_phoneNo", "default"));
                }
                if(sharedPref.getString("fifth_contact_phoneNo", "default") != "default"){
                    numbers.add(sharedPref.getString("fifth_contact_phoneNo", "default"));
                }
                Intent sms_intent;
                SmsManager manager = SmsManager.getDefault();
                for(String number : numbers){
                    sendSms(number, maps_location);
                }
            }
        }.start();

        Switch mySwitch = findViewById(R.id.emergency_switch);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myCountDown.cancel();
                    _tv.setText("COUNTDOWN STOPPED. EXITING.");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finishAndRemoveTask();
                        }
                    }, 2200);
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w("CERV1",  "CONNECTEDDDDDDD");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
        //Log.w("CERV1",  "LATITUUUUUUUD "+Double.toString(lat));
        //Log.w("CERV1",  "LONGITUUUUUD "+Double.toString(lon));

        maps_location += " This is my last location -> http://maps.google.com/maps?q=loc:" + lat + "," + lon;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void sendSms(String phonenumber,String message){
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phonenumber, null, message, null, null);
    }

}
