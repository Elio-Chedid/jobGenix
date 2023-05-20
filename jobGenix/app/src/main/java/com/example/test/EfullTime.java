package com.example.test;

import static com.example.test.PreHome.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class EfullTime extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recFetch1;
    AdapterFetchedEoD adapter;
    SwipeRefreshLayout refreshLayout;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_efull_time);
        refreshLayout=findViewById(R.id.swiperefresh3);
        refreshLayout.setOnRefreshListener(this);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        recFetch1=findViewById(R.id.recFetch1);
        adapter=new AdapterFetchedEoD(this,list);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recFetch1.setLayoutManager(linearLayoutManager);

        recFetch1.setAdapter(adapter);
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