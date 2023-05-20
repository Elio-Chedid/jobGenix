package com.example.test;

import static com.example.test.LogIn.sharedpref;
import static com.example.test.MainActivity.FLAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Welcome extends AppCompatActivity {
    TextView txtWelcome;
    Button btnnxt;
    String URL;
    RequestQueue requestQueue;
    public static ArrayList<AcceptedR> accepteds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        txtWelcome=findViewById(R.id.txtWelcome);
        btnnxt=findViewById(R.id.btnnxt);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        UserInfo.Uinfo.clear();
        btnnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
                if(FLAG.equals("Employer")){
                    sendData();
                    Intent i =new Intent(getApplicationContext(),EmployerPanel.class);
                    startActivity(i);
                    finish();
                }else{
                    sendData1();
                    Intent i =new Intent(getApplicationContext(),PreHome.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        txtWelcome.setText("Welcome to JobGenix");
    }



    public void sendData(){
        URL=MyIP.getIP()+"/getInfo";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        UserInfo.Uinfo.add(jsonObject.getString("username"));
                        UserInfo.Uinfo.add(jsonObject.getString("fname"));
                        UserInfo.Uinfo.add(jsonObject.getString("lname"));
                        UserInfo.Uinfo.add(jsonObject.getString("cname"));
                        UserInfo.Uinfo.add(jsonObject.getString("email"));
                        UserInfo.Uinfo.add(jsonObject.getString("phone"));
                    }else{
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(request);
    }

    public void sendData1(){
        URL=MyIP.getIP()+"/getInfo";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        UserInfo.Uinfo.add(jsonObject.getString("username"));
                        UserInfo.Uinfo.add(jsonObject.getString("fname"));
                        UserInfo.Uinfo.add(jsonObject.getString("lname"));
                        UserInfo.Uinfo.add(jsonObject.getString("email"));
                        UserInfo.Uinfo.add(jsonObject.getString("phone"));
                    }else{
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(request);
    }


}