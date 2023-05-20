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

public class E_editProfile extends AppCompatActivity {

    EditText f_nameChangeE,l_nameChangeE;
    TextView txtChangePassE,txtChangeEmailE,txtChangePhoneE;
    Button btnSaveE;
    RequestQueue requestQueue;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eedit_profile);

        requestQueue= Volley.newRequestQueue(getApplicationContext());
        f_nameChangeE=findViewById(R.id.f_nameChangeE);
        l_nameChangeE=findViewById(R.id.l_nameChangeE);
        txtChangePassE=findViewById(R.id.txtChangePassE);
        txtChangeEmailE=findViewById(R.id.txtChangeEmailE);
        txtChangePhoneE=findViewById(R.id.txtChangePhoneE);
        btnSaveE=findViewById(R.id.btnSaveE);
        f_nameChangeE.setText(UserInfo.Uinfo.get(1));
        l_nameChangeE.setText(UserInfo.Uinfo.get(2));

        btnSaveE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });


        txtChangePassE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),EditPassword.class);
                startActivity(i);
                finish();
            }
        });

        txtChangeEmailE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),EditEmail.class);
                startActivity(i);
                finish();
            }
        });
        txtChangePhoneE.setOnClickListener(new View.OnClickListener() {
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
        Intent i=new Intent(getApplicationContext(),EmployeePanel.class);
        startActivity(i);
        finish();
    }


    public void sendData(){

        URL=MyIP.getIP()+"/UpdateE";
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
                params.put("fname",f_nameChangeE.getText().toString());
                params.put("lname",l_nameChangeE.getText().toString());
                params.put("username",UserInfo.Uinfo.get(0));

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(request);
    }

}