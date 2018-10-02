package com.example.vishistvarugeese.dynamic_data_transfer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    String strDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Button btnOk = (Button) findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = datePicker.getDayOfMonth();
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                Log.d("date_simple", day + "" + year + " " + month);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
                    Date date = sdf.parse(day + "-" + month + "-" + year);
                    sdf = new SimpleDateFormat("EEE MMM dd yyyy");
                    strDate = sdf.format(date) + " 00:00:00 GMT+0530 (IST)";
                    Log.d("date_simple", strDate);
                } catch (ParseException e) {
                    Log.d("date_simple", e + "");
                    e.printStackTrace();
                }

                if(haveNetworkConnection()){
                    Intent intent = new Intent(MainActivity.this , com.example.vishistvarugeese.dynamic_data_transfer.SecondActivity.class);
                    intent.putExtra("osp", strDate + "");
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean haveNetworkConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


}
