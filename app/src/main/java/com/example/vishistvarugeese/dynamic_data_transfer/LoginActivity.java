package com.example.vishistvarugeese.dynamic_data_transfer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.vishistvarugeese.dynamic_data_transfer.helper.SQLiteHandler;
import com.example.vishistvarugeese.dynamic_data_transfer.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private EditText inputCpfNumber;
    private EditText inputPassword;
    private Button btnLogin;
    private TextView btnForgotPassword;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputCpfNumber = (EditText) findViewById(R.id.inputCpfNumber);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        btnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //Progree dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //Session manager
        session = new SessionManager(getApplicationContext());

        //Check if user is already logged in or not
        if(session.isLoggedIn()){
            HashMap<String, String> user = db.getUserDetails();
            String type = user.get("type");

            if(type.equals("admin")){
                Intent intent = new Intent(LoginActivity.this,
                        com.example.vishistvarugeese.dynamic_data_transfer.AdminActivity.class);
                startActivity(intent);
                finish();
            } else if(type.equals("normal")) {
                Intent intent = new Intent(LoginActivity.this,
                        com.example.vishistvarugeese.dynamic_data_transfer.ViewDataActivity.class);
                startActivity(intent);
                finish();
            }
        }

        //Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cpfNumber = inputCpfNumber.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                //Check for empty data in the form
                if(!cpfNumber.isEmpty() && !password.isEmpty()) {
                    checkLogin(cpfNumber, password);
                } else {
                    //Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),"Missing credentials! Try again!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentResetPassword = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intentResetPassword);
            }
        });

    }

    /**
     * function to verify login details in database
     */
    private void checkLogin(final String cpfNumber, final String password){
        //Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, com.example.vishistvarugeese.dynamic_data_transfer.AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //Check for error node in json
                    if(!error) {
                        //user successfully logged in
                        //Create login session
                        session.setLogin(true);

                        //Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String cpfNumber = user.getString("cpfNumber");
                        String type = user.getString("type");
                        String phoneNumber = user.getString("phoneNumber");
                        String created_at = user.getString("created_at");

                        //Inserting row in user
                        db.addUser(name, cpfNumber, uid, type, phoneNumber, created_at, password);

                        //Launch Activity
                        if(type.equals("admin")){
                            Intent intent = new Intent(LoginActivity.this,
                                    com.example.vishistvarugeese.dynamic_data_transfer.AdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else if(type.equals("normal")) {
                            Intent intent = new Intent(LoginActivity.this,
                                    com.example.vishistvarugeese.dynamic_data_transfer.ViewDataActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {

                        //Error in login. Get error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"JSON error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null && error.getMessage() !=null){
                    Log.e(TAG,"Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Cannot connect to server",Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cpfNumber", cpfNumber);
                params.put("password", password);

                return params;
            }
        };

        //Adding request to request queue
        com.example.vishistvarugeese.dynamic_data_transfer.AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog(){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

   /* @Override
    public void onBackPressed() {

    }*/
}
