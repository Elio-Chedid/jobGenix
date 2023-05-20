package com.example.test;

import static com.example.test.MainActivity.FLAG;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EditPhone extends AppCompatActivity {
    EditText phoneChange,OTPchange;
    Button btnVerifyChange,btnSubmitChange;
    RequestQueue requestQueue;
    FirebaseAuth mAuth;
    String codeSent;
    String phoneNumber;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        phoneChange=findViewById(R.id.phoneChange);
        OTPchange=findViewById(R.id.OTPchange);
        btnVerifyChange=findViewById(R.id.btnVerifyChange);
        btnSubmitChange=findViewById(R.id.btnSubmitChange);
        mAuth=FirebaseAuth.getInstance();
        for (String i:UserInfo.Uinfo){
            if (i.indexOf("+961")!=-1){
                phoneChange.setText(i);
            }
        }

        btnVerifyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerification();
            }
        });

        btnSubmitChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });
    }



    private void getVerification() {
        phoneNumber=phoneChange.getText().toString();
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
    private void verifyCode(){
        String cd=OTPchange.getText().toString();
        try{
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, cd);
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendData();
                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendData() {
        URL = MyIP.getIP()+"/PhoneUpdate";
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
                params.put("phone",phoneNumber);
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

    @Override
    public void onBackPressed() {
        if(FLAG.equals("Employer")){
            Intent i =new Intent(getApplicationContext(),R_editProfile.class);
            startActivity(i);
            finish();
        }else{
        Intent i=new Intent(getApplicationContext(),PreHome.class);
        startActivity(i);
        finish();
        }
    }
}