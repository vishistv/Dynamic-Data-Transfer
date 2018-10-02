package com.example.vishistvarugeese.dynamic_data_transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.vishistvarugeese.dynamic_data_transfer.AppController;
import com.example.vishistvarugeese.dynamic_data_transfer.CreateUserActivity;
import com.example.vishistvarugeese.dynamic_data_transfer.ListItem_RecyclerView_Admin;
import com.example.vishistvarugeese.dynamic_data_transfer.LoginActivity;
import com.example.vishistvarugeese.dynamic_data_transfer.MainActivity;
import com.example.vishistvarugeese.dynamic_data_transfer.RecyclerViewAdapter;
import com.example.vishistvarugeese.dynamic_data_transfer.helper.SQLiteHandler;
import com.example.vishistvarugeese.dynamic_data_transfer.helper.SessionManager;
import com.example.vishistvarugeese.dynamic_data_transfer.userAccountsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.vishistvarugeese.dynamic_data_transfer.AppConfig.URL_GET_LOG;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = AdminActivity.class.getSimpleName();

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private List<ListItem_RecyclerView_Admin> listItems;

    private SQLiteHandler db;
    private SessionManager session;

    private TextView navCpfNumber;
    private TextView navUserName;

    private TextView date;
    private SwipeRefreshLayout swipeRefresh;

    private String name;
    private String cpfNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Swipe to refresh
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);


        //Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        //Function to load the logs to the recycler view
        loadLogResults();

        //Navigation Drawer
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvDrawer);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpDrawerContent(nvDrawer);


        //Setting the username in Navigation drawer header
        navUserName = (TextView) findViewById(R.id.navUserName);
        navCpfNumber = (TextView) findViewById(R.id.navCpfNumber);
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        cpfNumber = user.get("cpfNumber");

        navUserName.setText(name);
        navCpfNumber.setText(cpfNumber);


        //Fetching the current date
        date = (TextView) findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, EEEE");
        String currentDate = sdf.format(new Date());

        date.setText(currentDate);

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
                    loadLogResults();
                }
            }
        );

    }

    /**
     * Function to load the users logged in
     * into the recycler view from the log database
     */
    public void loadLogResults(){

        String tag_string_req = "req_log";
        clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_LOG, new Response.Listener<String>() {
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

                        Log.d("Adapter: ", name);
                        ListItem_RecyclerView_Admin item = new ListItem_RecyclerView_Admin(created_at, name, cpfNumber, type);
                        listItems.add(item);
                    }
                    recyclerViewAdapter = new RecyclerViewAdapter(listItems, AdminActivity.this);
                    recyclerView.setAdapter(recyclerViewAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Store Log Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

    }

    public void clear() {
        int size = this.listItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listItems.remove(0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectItemDrawer(MenuItem menuitem){
        switch (menuitem.getItemId()){
            case R.id.addUser:
                Intent intentCreateUser = new Intent(AdminActivity.this , CreateUserActivity.class);
                startActivity(intentCreateUser);
                break;
            case R.id.viewData:
                Intent intentViewData = new Intent(AdminActivity.this , MainActivity.class);
                startActivity(intentViewData);
                break;
            case R.id.userAccounts:
                Intent intentUserAccountsData = new Intent(AdminActivity.this , userAccountsActivity.class);
                startActivity(intentUserAccountsData);
                break;
            case R.id.logOut:
                logoutUser();
            default:
                break;
        }
    }

    private void setUpDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return false;
            }
        });
    }


    @Override
    protected void onRestart() {
        loadLogResults();
        super.onRestart();
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser(){
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
