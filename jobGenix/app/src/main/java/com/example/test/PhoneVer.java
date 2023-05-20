package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVer extends AppCompatActivity {
    EditText etPhone,etOTP;
    Button btnPver,btnSub;
    FirebaseAuth mAuth;
    String codeSent;
    String phoneNumber;
    public static String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_ver);
        etPhone=findViewById(R.id.etPhone);
        etOTP=findViewById(R.id.etOTP);
        btnPver=findViewById(R.id.btnPver);
        btnSub=findViewById(R.id.btnSub);
        mAuth=FirebaseAuth.getInstance();

        btnPver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerification();
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),EmailVer.class);
        startActivity(i);
        finish();
    }

    private void verifyCode(){
        String cd=etOTP.getText().toString();
        try{
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, cd);
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }


    }

    private void getVerification() {
        phoneNumber=etPhone.getText().toString();
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
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent;
                            phone=phoneNumber;
                            if (MainActivity.FLAG.equals("Employer")){
                                intent=new Intent(getApplicationContext(),EmployerSignUp.class);
                                startActivity(intent);
                            }else {
                                intent =new Intent(getApplicationContext(),EmployeeSignUp.class);
                                startActivity(intent);
                            }
                            finish();
                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}