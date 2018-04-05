package dev.cervi.sentinel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences settings_pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings_pref = getApplicationContext().getSharedPreferences("settings_pref", 0);

        EditText mTimer = findViewById(R.id.timer_text);
        TextInputEditText mMessage = findViewById(R.id.message_text);

        if(settings_pref.getString("emergency_message", "default") != "default"){
            mMessage.setText(settings_pref.getString("emergency_message", "default"));
        }

        if(settings_pref.getString("emergency_timer", "default") != "default"){
            mTimer.setText(settings_pref.getString("emergency_timer", "default"));
        }

    }

    public void saveSettings(View view){

        SharedPreferences.Editor editor = settings_pref.edit();
        EditText mTimer = findViewById(R.id.timer_text);
        TextInputEditText mMessage = findViewById(R.id.message_text);

        Log.w("CERV1", mTimer.getText().toString());
        Log.w("CERV1", mMessage.getText().toString());

        editor.putString("emergency_timer", mTimer.getText().toString());
        editor.putString("emergency_message", mMessage.getText().toString());
        editor.commit();
        finish();
    }
}
