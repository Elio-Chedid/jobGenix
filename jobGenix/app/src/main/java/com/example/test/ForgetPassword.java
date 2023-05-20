package com.example.test;

import androidx.annotation.NonNull;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ForgetPassword extends AppCompatActivity {
    EditText etF_User;
    Button btnF_user;
    String URL;
    FirebaseAuth mAuth;
    public static String codeSent,F_USER;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        etF_User=findViewById(R.id.etF_User);
        btnF_user=findViewById(R.id.btnF_User);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        mAuth=FirebaseAuth.getInstance();
        btnF_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),LogIn.class);
        startActivity(intent);
        finish();
    }


    public void sendData(){

            URL=MyIP.getIP()+"/retrievePhone";
            Intent intent = new Intent(getApplicationContext(),PhoneVer_forget.class);
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        String phoneNum=jsonObject.getString("message");
                        getVerification(phoneNum);
                        Toast.makeText(getApplicationContext(),"verification message sent to your phone",Toast.LENGTH_LONG).show();
                        F_USER=etF_User.getText().toString();
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
                params.put("username",etF_User.getText().toString());
                params.put("type",MainActivity.FLAG);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
        requestQueue.add(request);
    }


    private void getVerification(String phoneNumber) {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent=s;
            }
        };
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
}