package dev.cervi.sentinel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ContactsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 13;
    private int contact_used = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.w("CERV1", "NO TENEMOS PERMISSSS");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        Button saved_prefs;
        String name;
        String phoneNo;

       if(sharedPref.getString("first_contact_name", "default") != "default"){
            saved_prefs = findViewById(R.id.button_first_contact);
            saved_prefs.setText(sharedPref.getString("first_contact_name", "default"));
            saved_prefs.setBackgroundResource(R.color.contactSelectedBackground);
       }
       if(sharedPref.getString("second_contact_name", "default") != "default"){
           saved_prefs = findViewById(R.id.button_second_contact);
           saved_prefs.setText(sharedPref.getString("second_contact_name", "default"));
           saved_prefs.setBackgroundResource(R.color.contactSelectedBackground);
       }
       if(sharedPref.getString("third_contact_name", "default") != "default"){
           saved_prefs = findViewById(R.id.button_third_contact);
           saved_prefs.setText(sharedPref.getString("third_contact_name", "default"));
           saved_prefs.setBackgroundResource(R.color.contactSelectedBackground);
       }
       if(sharedPref.getString("fourth_contact_name", "default") != "default"){
           saved_prefs = findViewById(R.id.button_fourth_contact);
           saved_prefs.setText(sharedPref.getString("fourth_contact_name", "default"));
           saved_prefs.setBackgroundResource(R.color.contactSelectedBackground);
       }
       if(sharedPref.getString("fifth_contact_name", "default") != "default"){
           saved_prefs = findViewById(R.id.button_fifth_contact);
           saved_prefs.setText(sharedPref.getString("fifth_contact_name", "default"));
           saved_prefs.setBackgroundResource(R.color.contactSelectedBackground);
       }

    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            Button contact;
            contact = findViewById(data.getIntExtra("id_button", 0));

            contact.setText(name);
            contact.setBackgroundResource(R.color.contactSelectedBackground);

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            if(data.getIntExtra("id_button", 0) == R.id.button_first_contact) {
                editor.putString("first_contact_name", name);
                editor.putString("first_contact_phoneNo", phoneNo);
            }
            else if(data.getIntExtra("id_button", 0) == R.id.button_second_contact) {
                editor.putString("second_contact_name", name);
                editor.putString("second_contact_phoneNo", phoneNo);
            }
            else if(data.getIntExtra("id_button", 0) == R.id.button_third_contact) {
                editor.putString("third_contact_name", name);
                editor.putString("third_contact_phoneNo", phoneNo);
            }
            else if(data.getIntExtra("id_button", 0) == R.id.button_fourth_contact) {
                editor.putString("fourth_contact_name", name);
                editor.putString("fourth_contact_phoneNo", phoneNo);
            }
            else if(data.getIntExtra("id_button", 0) == R.id.button_fifth_contact) {
                editor.putString("fifth_contact_name", name);
                editor.putString("fifth_contact_phoneNo", phoneNo);
            }

            editor.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFirstContact(View view){
        Intent contact_picker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        contact_picker.putExtra("id_button", R.id.button_first_contact);
        startActivityForResult(contact_picker, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    public void openSecondContact(View view){
        Intent contact_picker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        contact_picker.putExtra("id_button", R.id.button_second_contact);
        startActivityForResult(contact_picker, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    public void openThirdContact(View view){
        Intent contact_picker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        contact_picker.putExtra("id_button", R.id.button_third_contact);
        startActivityForResult(contact_picker, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    public void openFourthContact(View view){
        Intent contact_picker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        contact_picker.putExtra("id_button", R.id.button_fourth_contact);
        startActivityForResult(contact_picker, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    public void openFifthContact(View view){
        Intent contact_picker = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        contact_picker.putExtra("id_button", R.id.button_fifth_contact);
        startActivityForResult(contact_picker, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("CERV1", Integer.toString(data.getIntExtra("id_b", 0)));
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            contactPicked(data);

        }
    }
}
