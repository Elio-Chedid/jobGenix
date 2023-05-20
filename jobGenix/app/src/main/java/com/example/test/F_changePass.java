package com.example.test;

import static com.example.test.ForgetPassword.F_USER;
import static com.example.test.MainActivity.FLAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class F_changePass extends AppCompatActivity {
    EditText etconfpass,etpass;
    Button btnchange;
    RequestQueue requestQueue;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fchange_pass);
        etconfpass=findViewById(R.id.etconfpass);
        etpass=findViewById(R.id.etpass);
        btnchange=findViewById(R.id.btnchange);
        requestQueue= Volley.newRequestQueue(getApplicationContext());


        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etpass.getText().toString().equals(etconfpass.getText().toString())){
                    sendData();
                }else{
                    Toast.makeText(getApplicationContext(),"password doesn\'t match confirm password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void sendData(){

        URL=MyIP.getIP()+"/forget";
        Intent intent;
        intent = new Intent(getApplicationContext(),LogIn.class);
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        startActivity(intent);
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
                //F_USER imported from forgetPassword class
                params.put("username",F_USER);
                params.put("password",etpass.getText().toString());
                params.put("type",FLAG);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(request);
    }

}