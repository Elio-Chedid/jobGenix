package com.example.test;

import static com.example.test.LogIn.sharedpref;
import static com.example.test.Welcome.accepteds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.*;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployerPanel extends AppCompatActivity {
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_employer_panel);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        sendData();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        loadFragment(new R_HomeFrag());
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.r_home:
                    loadFragment(new R_HomeFrag());
                    break;

                case R.id.r_add:
                    loadFragment(new R_GenRegFrag());
                    break;

                case R.id.r_profile:
                    loadFragment(new R_ProfileFrag());
                    break;


            }

            return true;
        });
    }


    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void sendData() {

        String URL = MyIP.getIP()+"/viewAccR";

        accepteds=new ArrayList<>();
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        JSONArray messageArray = jsonObject.getJSONArray("message");
                        for (int i=0;i<messageArray.length();i++){
                            JSONArray array=messageArray.getJSONArray(i);
                            String U_fname=array.getString(0);
                            String U_lname=array.getString(1);
                            String U_phone=array.getString(2);
                            String U_longtitude=array.getString(3);
                            String U_latitude=array.getString(4);
                            AcceptedR accepted=new AcceptedR(U_fname,U_lname,U_phone,U_longtitude,U_latitude);
                            if (accepteds.size()==0){
                                accepteds.add(accepted);
                            }else if(!checkExist(accepted)){
                                accepteds.add(accepted);
                            }
                        }


                    }else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error+"");

            }
        }){
            public Map<String,String> getParams(){
                SharedPreferences sharedPreferences=getSharedPreferences(sharedpref,MODE_PRIVATE);
                String token=sharedPreferences.getString("token",null);
                Map<String,String> params=new HashMap<String,String>();
                params.put("token",token);
                return params;
            }
        };
        try{
            request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
            requestQueue.add(request);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private boolean checkExist(AcceptedR acceptedR){
        for(AcceptedR a:accepteds){
            if(a.getPhone().equals(acceptedR.getPhone())){
                return true;
            }
        }
        return false;
    }


}