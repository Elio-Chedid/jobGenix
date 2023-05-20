package com.example.test;

import static com.example.test.LogIn.sharedpref;
import static com.example.test.PreHome.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EonDemand extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recFetch;
    AdapterFetchedEoD adapter;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eon_demand);
        refreshLayout=findViewById(R.id.swiperefresh1);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        refreshLayout.setOnRefreshListener(this);
        recFetch=findViewById(R.id.recFetch);
        adapter=new AdapterFetchedEoD(this,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recFetch.setLayoutManager(linearLayoutManager);
        recFetch.setAdapter(adapter);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        },2000);

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),EmployeePanel.class));
        finish();
    }


    @Override
    public void onRefresh() {

    }
}