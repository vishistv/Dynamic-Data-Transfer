package com.example.vishistvarugeese.dynamic_data_transfer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateUserActivity extends AppCompatActivity {

    private static final String TAG = CreateUserActivity.class.getSimpleName();
    private TextView inputCpfNumber;
    private TextView inputName;
    private TextView inputPassword;
    private TextView inputPhoneNumber;
    private TextView inputEmail;
    private Button btnRegister;
    private ProgressDialog pDialog;
    private RadioButton radioButtonAdmin;
    private RadioButton radioButtonNormal;
    String type;
    private boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialization
        inputCpfNumber = (TextView) findViewById(R.id.inputCpfNumber);
        inputName = (TextView) findViewById(R.id.inputName);
        inputPassword = (TextView) findViewById(R.id.inputPassword);
        inputPhoneNumber = (TextView) findViewById(R.id.inputPhoneNumber);
        inputEmail = (TextView) findViewById(R.id.inputEmail);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        radioButtonAdmin = (RadioButton) findViewById(R.id.radioButtonAdmin);
        radioButtonNormal = (RadioButton) findViewById(R.id.radioButtonNormal);

        //Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString().trim();
                String cpfNumber = inputCpfNumber.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String phoneNumber = inputPhoneNumber.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();

                //Input Validation
                if(!name.isEmpty() && !cpfNumber.isEmpty() && !password.isEmpty() && !phoneNumber.isEmpty() && !email.isEmpty() && checked != false){

                    if(!(cpfNumber.length() > 4)){
                        Toast.makeText(getApplicationContext(),"Invalid CPF Number! Try again!",Toast.LENGTH_LONG).show();
                    }
                    else if(!(password.length() > 6)){
                        Toast.makeText(getApplicationContext(),"Invalid Password! Should be greater than 6 characters!",Toast.LENGTH_LONG).show();
                    }
                    else if(!(phoneNumber.length() == 10 || phoneNumber.length() == 11)){
                        Toast.makeText(getApplicationContext(),"Invalid Phone Number! Try again!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        registerUser(name, cpfNumber, password, phoneNumber, email, type);
                        Log.d("type: ", type);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

        /**
         * Function to store user in MySQL database. Will post
         * the params to register url
         */
        private void registerUser(final String name, final String cpfNumber, final String password, final String phoneNumber, final String email, final String type) {
            Log.d("r: ", "entered");
            //Tag used to cancel request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering...");
        showDialog();

            StringRequest strReq = new StringRequest(Method.POST, com.example.vishistvarugeese.dynamic_data_transfer.AppConfig.URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if(!error){
                            //User successfully stored in MySQL
                            Toast.makeText(getApplicationContext(), "User successfully registered! ", Toast.LENGTH_LONG).show();
                            inputCpfNumber.setText("");
                            inputName.setText("");
                            inputPassword.setText("");
                            inputPhoneNumber.setText("");
                            inputEmail.setText("");
                            radioButtonUncheck();
                        } else {
                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }

            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("cpfNumber", cpfNumber);
                    params.put("password", password);
                    params.put("phoneNumber",phoneNumber);
                    params.put("email",email);
                    params.put("type",type);

                    return params;
                }
            };
            // Adding request to request queue
            com.example.vishistvarugeese.dynamic_data_transfer.AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    //Radio Button click event
    public void onRadioButtonClicked(View view){
        // Is the button now checked?
        checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonAdmin:
                if (checked)
                    type = "admin";
                break;
            case R.id.radioButtonNormal:
                if (checked)
                    type = "normal";
                break;
        }
    }

    private void radioButtonUncheck(){
        if(checked){
            radioButtonNormal.setChecked(false);
            radioButtonAdmin.setChecked(false);
        }
    }
}
