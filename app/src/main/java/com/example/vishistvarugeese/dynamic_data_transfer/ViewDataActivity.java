package com.example.vishistvarugeese.dynamic_data_transfer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vishistvarugeese.dynamic_data_transfer.helper.SQLiteHandler;
import com.example.vishistvarugeese.dynamic_data_transfer.helper.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ViewDataActivity extends AppCompatActivity {

    private SQLiteHandler db;
    private SessionManager session;

    private String strDate;
    private String username;

    private TextView toolbarName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarName = (TextView) findViewById(R.id.toolbarName);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //getting username
        HashMap<String, String> user = db.getUserDetails();
        username = user.get("name");

        //Setting name on toolbar
        toolbarName.setText(username);

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
                    java.util.Date date = sdf.parse(day + "-" + month + "-" + year);
                    sdf = new SimpleDateFormat("EEE MMM dd yyyy");
                    strDate = sdf.format(date) + " 00:00:00 GMT+0530 (IST)";
                    Log.d("date_simple", strDate);
                } catch (ParseException e) {
                    Log.d("date_simple", e + "");
                    e.printStackTrace();
                }

                if(haveNetworkConnection()){
                    Intent intent = new Intent(ViewDataActivity.this , com.example.vishistvarugeese.dynamic_data_transfer.ViewDataSecondActivity.class);
                    intent.putExtra("osp", strDate);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(ViewDataActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
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

    private void logoutUser(){
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ViewDataActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.normal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.normalMenuLogout){
            logoutUser();
        }
        else if(id == R.id.profile) {
            Intent intent = new Intent(ViewDataActivity.this, NormalUserProfileActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
