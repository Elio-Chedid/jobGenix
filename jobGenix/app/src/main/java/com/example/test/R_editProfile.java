package com.example.test;

import static com.example.test.LogIn.sharedpref;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class R_editProfile extends AppCompatActivity {
    EditText f_nameChangeR,l_nameChangeR,comp_nameChange;
    TextView txtChangePassR,txtChangeEmailR,txtChangePhoneR;
    Button btnSaveR;
    RequestQueue requestQueue;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redit_profile);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        f_nameChangeR=findViewById(R.id.f_nameChangeR);
        l_nameChangeR=findViewById(R.id.l_nameChangeR);
        comp_nameChange=findViewById(R.id.comp_nameChange);
        txtChangePassR=findViewById(R.id.txtChangePassR);
        txtChangeEmailR=findViewById(R.id.txtChangeEmailR);
        txtChangePhoneR=findViewById(R.id.txtChangePhoneR);
        btnSaveR=findViewById(R.id.btnSaveR);
        f_nameChangeR.setText(UserInfo.Uinfo.get(1));
        l_nameChangeR.setText(UserInfo.Uinfo.get(2));
        comp_nameChange.setText(UserInfo.Uinfo.get(3));
        btnSaveR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        txtChangePassR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),EditPassword.class);
                startActivity(i);
                finish();
            }
        });

        txtChangeEmailR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),EditEmail.class);
                startActivity(i);
                finish();
            }
        });
        txtChangePhoneR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),EditPhone.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),EmployerPanel.class);
        startActivity(i);
        finish();
    }


    public void sendData(){

        URL=MyIP.getIP()+"/UpdateR";
        Intent intent;
        intent = new Intent(getApplicationContext(),Welcome.class);

        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences=getSharedPreferences(sharedpref,MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("token",jsonObject.getString("token"));
                        editor.apply();
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
                params.put("fname",f_nameChangeR.getText().toString());
                params.put("lname",l_nameChangeR.getText().toString());
                params.put("compName",comp_nameChange.getText().toString());
                params.put("username",UserInfo.Uinfo.get(0));

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(request);
    }

}