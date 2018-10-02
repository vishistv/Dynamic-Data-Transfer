package com.example.vishistvarugeese.dynamic_data_transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.vishistvarugeese.dynamic_data_transfer.helper.SQLiteHandler;

import java.util.HashMap;

public class NormalUserProfileActivity extends AppCompatActivity {

    private SQLiteHandler db;

    private String cpfNumber;
    private String name;
    private String phoneNumber;
    private String type;

    private TextView txtName;
    private TextView txtType;
    private TextView txtCpfNumber;
    private TextView txtPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = (TextView) findViewById(R.id.txtUserProfileName);
        txtCpfNumber = (TextView) findViewById(R.id.txtProfileCpf);
        txtType = (TextView) findViewById(R.id.txtProfileType);
        txtPhoneNumber = (TextView) findViewById(R.id.txtProfilePhoneNumber);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //Getting CPF Number and password from SQLite database
        HashMap<String, String> user = db.getUserDetails();
        cpfNumber = user.get("cpfNumber");
        name = user.get("name");
        phoneNumber = user.get("phoneNumber");
        type = user.get("type");

        txtType.setText(type);
        txtName.setText(name);
        txtPhoneNumber.setText(phoneNumber);
        txtCpfNumber.setText(cpfNumber);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent back = new Intent(NormalUserProfileActivity.this, com.example.vishistvarugeese.dynamic_data_transfer.ViewDataActivity.class);
        startActivity(back);
        finish();
        return super.onOptionsItemSelected(item);
    }

}
