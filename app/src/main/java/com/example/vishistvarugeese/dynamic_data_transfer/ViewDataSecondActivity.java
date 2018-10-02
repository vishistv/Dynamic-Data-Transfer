package com.example.vishistvarugeese.dynamic_data_transfer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.vishistvarugeese.dynamic_data_transfer.helper.SQLiteHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewDataSecondActivity extends AppCompatActivity {

    private static final String TAG = ViewDataSecondActivity.class.getSimpleName();

    String date;

    private SQLiteHandler db;

    private String cpfNumber;
    private String password;

    private RecyclerView recyclerView;
    private RecyclerViewDataAdapter recyclerViewDataAdapter;
    private List<ListItem_RecyclerView_Data> listItems;

    private String value;
    private String title;

    private ProgressDialog Dialog;

    int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.usersDataataRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewDataSecondActivity.this));
        listItems = new ArrayList<>();

        //Getting CPF Number and password from SQLite database
        HashMap<String, String> user = db.getUserDetails();
        cpfNumber = user.get("cpfNumber");
        password = user.get("password");

        Dialog = new ProgressDialog(ViewDataSecondActivity.this);
        Dialog.setMessage("Fetching data...");
        Dialog.show();

        Intent intent = getIntent();
        date = intent.getStringExtra("osp");

        storeLog(cpfNumber, password);

        final FirebaseDatabase firebaseDatebase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatebase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(date)){
                    //for loop that gets all values of that date
                    for(int i = 0; i < 6; i++) {

                        firebaseDatebase.getReference(date).child("value" + (i + 1)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    value = dataSnapshot.getValue().toString();
                                } catch(Exception e){
                                    Log.d("data_simple", e + "");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        firebaseDatebase.getReference("titles").child("title" + (i + 1)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try{
                                    title = dataSnapshot.getValue().toString() + ":";
                                } catch(Exception e){

                                }

                                Log.d("data_simple", value + " " + title);
                                ListItem_RecyclerView_Data item = new ListItem_RecyclerView_Data(title, value);
                                listItems.add(item);
                                recyclerViewDataAdapter = new RecyclerViewDataAdapter(listItems, ViewDataSecondActivity.this);
                                recyclerView.setAdapter(recyclerViewDataAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    Dialog.hide();
                }
                else{
                    Toast.makeText(ViewDataSecondActivity.this,"Data Not Available",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(ViewDataSecondActivity.this, ViewDataActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void storeLog(final String cpfNumber, final String password){
        //Tag used to cancel the request
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_STORE_LOG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Log Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("log_error");
                    Log.d("blah: ", error + "");

                    //Check for error node in json
                    if(!error) {


                    } else {

                        //Error in storing log. Get error message
                        String errorMsg = jObj.getString("log_error_msg");
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
                Log.e(TAG,"Store Log Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(ViewDataSecondActivity.this,ViewDataActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent back = new Intent(ViewDataSecondActivity.this,ViewDataActivity.class);
        startActivity(back);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
