package com.example.vishistvarugeese.dynamic_data_transfer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.vishistvarugeese.dynamic_data_transfer.AppConfig.URL_REQ_RESET_PASS;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = ResetPasswordActivity.class.getSimpleName();

    private EditText resetPasswordEmail;
    private Button btnResetPassword;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordEmail = (EditText) findViewById(R.id.resetPasswordEmail);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);

        //Progree dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetPasswordEmail.getText().toString();

                if(email != ""){
                    resetPasswordRequest(email);
                }
            }
        });
    }


    /**
     * Function to send the entered email id
     * to reset the password
     */
    private void resetPasswordRequest(final String email){
        //Tag used to cancel the request
        String tag_string_req = "req_reset_pass";

        pDialog.setMessage("Sending email ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_REQ_RESET_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    Log.d("reset_password", response);
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //Check for error node in json
                    if(!error) {



                    } else {

                        //Error in reset password. Get error message
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
                    Log.e(TAG,"Reset Password Error: " + error.getMessage());
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
                params.put("email", email);

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

}
