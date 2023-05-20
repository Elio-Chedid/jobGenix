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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVer extends AppCompatActivity {
    EditText etEmail;
    Button btnEver,btnNext;
    public static String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_ver);
        etEmail=findViewById(R.id.etEmail);
        btnEver=findViewById(R.id.btnEver);
        btnNext=findViewById(R.id.btnNext);
        btnEver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Next();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    private void Next() {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,"123456").addOnCompleteListener((task)-> {
           if(task.isSuccessful()){
               if (firebaseAuth.getCurrentUser().isEmailVerified()){
                   Intent i=new Intent(getApplicationContext(),PhoneVer.class);
                   startActivity(i);
                   finish();
               }else{
                   Toast.makeText(getApplicationContext(),"please verify your email first",Toast.LENGTH_LONG).show();
               }
           }else{
               Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
           }
        });
    }

    private void sendCode() {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),"123456").addOnCompleteListener((task) -> {
           if (task.isSuccessful()){
               firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(getApplicationContext(),"verification link sent please verify your email",Toast.LENGTH_LONG).show();
                           email=etEmail.getText().toString();
                           etEmail.setText("");
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
}