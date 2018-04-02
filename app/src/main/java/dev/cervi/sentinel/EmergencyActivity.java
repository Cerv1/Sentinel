package dev.cervi.sentinel;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;


public class EmergencyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        final TextView _tv = (TextView) findViewById( R.id.countdown_text );
        final CountDownTimer myCountDown = new CountDownTimer(60000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                _tv.setText(String.format("%s", String.format("%d min, %d sec",
                        MILLISECONDS.toMinutes(millisUntilFinished),
                        MILLISECONDS.toSeconds(millisUntilFinished) - MINUTES.toSeconds(MILLISECONDS.toMinutes(millisUntilFinished)))));
            }

            public void onFinish() {
                _tv.setText("SENDING SMS");
            }
        }.start();
        Switch mySwitch = findViewById(R.id.emergency_switch);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
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
}
