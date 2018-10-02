package com.example.vishistvarugeese.dynamic_data_transfer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.vishistvarugeese.dynamic_data_transfer.AppConfig.URL_USER_ACCOUNTS;

public class userAccountsActivity extends AppCompatActivity {

    private static final String TAG = userAccountsActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private com.example.vishistvarugeese.dynamic_data_transfer.RecyclerViewUserAdapter recyclerViewUserAdapter;
    private List<com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_User> listItems;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accounts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Swipe to refresh
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.userSwipeRefresh);

        //Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.userRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(userAccountsActivity.this));
        listItems = new ArrayList<>();

        //Function to load the users to the recycler view
        loadUserAccounts();

        /*
        * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
        * performs a swipe-to-refresh gesture.
        */
        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("Swipe_log: ", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        loadUserAccounts();
                    }
                }
        );
    }

    /**
     * Function to load the user account
     * into the recycler view from the log database
     */
   public void loadUserAccounts(){

        String tag_string_req = "req_user_acc";
        clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_USER_ACCOUNTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jArray = new JSONArray(response);

                    for(int i = jArray.length()-1; i>=0; i--){
                        JSONObject log = jArray.getJSONObject(i);

                        String name = log.getString("name");
                        String cpfNumber = log.getString("cpfNumber");
                        String type = log.getString("type");
                        String created_at = log.getString("created_at");
                        String phoneNumber = log.getString("phoneNumber");

                        Log.d("Adapter: ", name + cpfNumber + type + created_at);
                        ListItem_RecyclerView_User item = new ListItem_RecyclerView_User (name, cpfNumber, type, phoneNumber, created_at);
                        listItems.add(item);
                    }
                    recyclerViewUserAdapter = new RecyclerViewUserAdapter(listItems, userAccountsActivity.this);
                    recyclerView.setAdapter(recyclerViewUserAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null && error.getMessage() !=null){
                    Log.e(TAG,"User Accounts Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        });

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        Log.d("refresh", "enter");
       if (swipeRefresh.isRefreshing()) {
           swipeRefresh.setRefreshing(false);
       }
    }

    public void clear() {
        if (this.listItems == null) {
            return;
        } else {
            int size = this.listItems.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    this.listItems.remove(0);
                }
            }
        }
    }

}
