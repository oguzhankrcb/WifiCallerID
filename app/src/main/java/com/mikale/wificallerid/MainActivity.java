package com.mikale.wificallerid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button mStartService;
    private Button mStopService;
    private TextView txtStatus;
    private EditText edtIP, edtPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        mStartService = findViewById(R.id.btnStartService);
        mStopService = findViewById(R.id.btnStopService);
        edtIP = findViewById(R.id.edtIP);
        edtPort = findViewById(R.id.edtPort);
        txtStatus = findViewById(R.id.txtStatus);

        String getSavedIP = LocalDataManager.getSharedPreference(MainActivity.this, "ip", "");
        String getSavedPort = LocalDataManager.getSharedPreference(MainActivity.this, "port", "");
        String getSavedStatus = LocalDataManager.getSharedPreference(MainActivity.this, "status", "0");

        if (!getSavedIP.equals(""))
        {
            edtIP.setText(getSavedIP);
        }

        if(!getSavedPort.equals(""))
        {
            edtPort.setText(getSavedPort);
        }

        if (!getSavedStatus.equals("0")){
            txtStatus.setText("Status : Aktif");
        }
        else{
            txtStatus.setText("Status : Pasif");
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG}, 1);

        mStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDataManager.setSharedPreference(MainActivity.this, "status", "1");
                LocalDataManager.setSharedPreference(MainActivity.this, "ip", edtIP.getText().toString());
                LocalDataManager.setSharedPreference(MainActivity.this, "port", edtPort.getText().toString());
                txtStatus.setText("Status : Aktif");
                Toast.makeText(MainActivity.this, "Servis Başarıyla Başlatıldı!", Toast.LENGTH_LONG).show();
            }
        });

        mStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDataManager.setSharedPreference(MainActivity.this, "status", "0");
                txtStatus.setText("Status : Pasif");
                Toast.makeText(MainActivity.this, "Servis Başarıyla Durduruldu!", Toast.LENGTH_LONG).show();
            }
        });

    }


}