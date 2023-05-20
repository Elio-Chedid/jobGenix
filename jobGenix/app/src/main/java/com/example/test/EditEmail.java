package com.example.test;

import static com.example.test.MainActivity.FLAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditEmail extends AppCompatActivity {
    Intent i;
    RequestQueue requestQueue;
    String URL;
    EditText changeEmail;
    Button EditVerifyEmail,EditSubmitEmail;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        changeEmail=findViewById(R.id.changeEmail);
        EditVerifyEmail=findViewById(R.id.EditVerifyEmail);
        EditSubmitEmail=findViewById(R.id.EditSubmitEmail);
        for (String i:UserInfo.Uinfo){
            if (i.indexOf("@")!=-1){
                changeEmail.setText(i);
            }
        }
        EditVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });

        EditSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (FLAG.equals("Employer")){
            i=new Intent(getApplicationContext(),EmployerPanel.class);
        }else{
            i = new Intent(getApplicationContext(),PreHome.class);
        }
        startActivity(i);
        finish();
    }

    private void sendCode() {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(changeEmail.getText().toString(),"123456").addOnCompleteListener((task) -> {
            if (task.isSuccessful()){
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            email=changeEmail.getText().toString();
                            Toast.makeText(getApplicationContext(),"verification link sent please verify your email",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void Next() {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        try{
            firebaseAuth.signInWithEmailAndPassword(email,"123456").addOnCompleteListener((task)-> {
                if(task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        sendData();
                    }else{
                        Toast.makeText(getApplicationContext(),"please verify your email first",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void sendData() {
        URL = MyIP.getIP()+"/EmailUpdate";
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
                params.put("email",email);
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