package com.example.test;

import static com.example.test.ForgetPassword.codeSent;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneVer_forget extends AppCompatActivity {
    EditText etF_OTP;
    Button btnF_Verify;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_ver_forget);
        etF_OTP = findViewById(R.id.etF_OTP);
        btnF_Verify=findViewById(R.id.btnF_Verify);
        mAuth=FirebaseAuth.getInstance();


        btnF_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });
    }

    private void verifyCode(){
        String cd=etF_OTP.getText().toString();
        try{
            //code sent imported from forget password class
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
                            Intent intent;
                            intent=new Intent(getApplicationContext(),F_changePass.class);
                            startActivity(intent);
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