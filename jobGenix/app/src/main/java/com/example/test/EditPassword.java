package com.example.test;

import static com.example.test.MainActivity.FLAG;

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

public class EditPassword extends AppCompatActivity {
    EditText oldPass,newPass,conf_new_pass;
    Button btnSavePass;
    String URL;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        oldPass =findViewById(R.id.oldPass);
        newPass =findViewById(R.id.newPass);
        conf_new_pass =findViewById(R.id.conf_new_pass);
        btnSavePass =findViewById(R.id.btnSavePass);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPass.getText().toString().equals(conf_new_pass.getText().toString())){
                    sendData();
                }else{
                    Toast.makeText(getApplicationContext(),"password and confirm pass don\'t match",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i;
        if (FLAG.equals("Employer")){
            i = new Intent(getApplicationContext(),R_editProfile.class);
            startActivity(i);
            finish();
        }else{
            i = new Intent(getApplicationContext(),PreHome.class);
            startActivity(i);
            finish();
        }
    }



    private void sendData() {
        URL = MyIP.getIP()+"/PasswordUpdate";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        Intent i;
                        i =new Intent(getApplicationContext(),Welcome.class);

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
                params.put("type",FLAG);
                params.put("username",UserInfo.Uinfo.get(0));
                params.put("password",newPass.getText().toString());
                params.put("oldPass",oldPass.getText().toString());
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