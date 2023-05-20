package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class EmployeeSignUp extends AppCompatActivity {
    RequestQueue requestQueue;
    String URL;
    EditText f_name_employee,l_name_employee,etUser,pass_employee,conf_pass_employee;
    Button reg_btn_employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_sign_up);
        requestQueue= Volley.newRequestQueue(getApplicationContext());

        f_name_employee=findViewById(R.id.f_name_employee);
        l_name_employee=findViewById(R.id.l_name_employee);
        etUser=findViewById(R.id.etUser);
        pass_employee=findViewById(R.id.pass_employee);
        conf_pass_employee=findViewById(R.id.conf_pass_employee);
        reg_btn_employee=findViewById(R.id.reg_btn_employee);


        reg_btn_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pass_employee.getText().toString().equals(conf_pass_employee.getText().toString())){
                    sendData();
                }else{
                    Toast.makeText(getApplicationContext(),"password does not match confirm password",Toast.LENGTH_LONG).show();
                }
            }
        });


    }



    private void sendData() {
        URL = MyIP.getIP()+"/UsignUp";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        Intent i =new Intent(getApplicationContext(),LogIn.class);
                        startActivity(i);
                        finish();
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
                Map<String,String> params=new HashMap<String,String>();
                params.put("fname",f_name_employee.getText().toString());
                params.put("lname",l_name_employee.getText().toString());
                params.put("email",EmailVer.email);
                params.put("phone",PhoneVer.phone);
                params.put("longtitude",MainActivity.LONGT);
                params.put("latitude",MainActivity.LAT);
                params.put("username",etUser.getText().toString());
                params.put("password",conf_pass_employee.getText().toString());
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
}