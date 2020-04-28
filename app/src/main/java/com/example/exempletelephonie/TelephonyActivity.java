package com.example.exempletelephonie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class TelephonyActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephony);

        context = this;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

        int phoneType = telephonyManager.getPhoneType();
        String type = "";

        switch(phoneType)
        {
            case TelephonyManager.PHONE_TYPE_NONE :
                type = "Aucun";
                break;

            case TelephonyManager.PHONE_TYPE_GSM :
                type = "Gsm";
                break;
        }

        String imei = "";
        String operateur = telephonyManager.getSimOperator();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                 imei = telephonyManager.getImei();
            }

            String contrat = telephonyManager.getSubscriberId();

        }

        TextView txtTelephony = findViewById(R.id.txtTelephony);
        txtTelephony.setText("Nos Infos : " + type +"" + imei);;

        getAllContacts();

        Button btnCallPhone = findViewById(R.id.btnCallPhone);

        btnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0782893066"));

                if(ActivityCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                {startActivity(callIntent);
                }

            }
        });
    }

    private HashMap<String, String> getAllContacts() {

        HashMap<String,String> nameList = new HashMap<>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNo="";

                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String toto="";
                    }

                    pCur.close();
                }

                if(!name.isEmpty() && !phoneNo.isEmpty())
                {
                    nameList.put(name,phoneNo);
                }
            }
        }
        if (cur != null) {
            cur.close();
        }

        return nameList;
    }
}
