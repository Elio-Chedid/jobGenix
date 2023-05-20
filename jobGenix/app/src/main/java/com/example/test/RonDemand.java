package com.example.test;

import static com.example.test.LogIn.sharedpref;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class RonDemand extends AppCompatActivity {
    Spinner spinnerJtitle,spinerExpNeeded ;
    Button post;
    EditText editEmpNeeded,etjobDesc;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ron_demand);
        spinnerJtitle = findViewById(R.id.jobtitle);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.Jtitle, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJtitle.setAdapter(adapter);
        spinnerJtitle.setSelection(0);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        spinerExpNeeded=findViewById(R.id.expneeded);
        ArrayAdapter <CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.Eneeded, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerExpNeeded.setAdapter(adapter1);
        spinerExpNeeded.setSelection(0);
        post=findViewById(R.id.post);
        editEmpNeeded=findViewById(R.id.editEmpNeeded);
        etjobDesc=findViewById(R.id.etjobDesc);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),EmployerPanel.class));
        finish();
    }

    private void sendData() {
        String URL = MyIP.getIP()+"/Generate";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        Intent i =new Intent(getApplicationContext(),EmployerPanel.class);
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
                SharedPreferences sharedPreferences=getSharedPreferences(sharedpref,MODE_PRIVATE);
                String token=sharedPreferences.getString("token",null);
                Map<String,String> params=new HashMap<String,String>();
                params.put("token",token);
                params.put("days","1");
                params.put("hours","2");
                params.put("category","onDemand");
                params.put("jTitle",spinnerJtitle.getSelectedItem().toString());
                params.put("jDesc",etjobDesc.getText().toString());
                params.put("expNeeded",spinerExpNeeded.getSelectedItem().toString());
                params.put("empNeeded",editEmpNeeded.getText().toString());
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