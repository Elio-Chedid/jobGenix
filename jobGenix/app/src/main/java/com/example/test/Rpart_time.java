package com.example.test;

import static com.example.test.LogIn.sharedpref;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Rpart_time extends AppCompatActivity {
    TextView tvDay,tvHours;
    boolean[] selectedDay;
    ArrayList<Integer> dayList=new ArrayList<>();
    String [] dayArray={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    boolean[] selectedHour;
    ArrayList<Integer> hourList=new ArrayList<>();
    String [] hourArray={"8-10","10-12","12-14","14-16","16-18","18-20","20-22","22-00"};

    Spinner jobtitlept,Experience;
    EditText editEmpNeeded,etjobDesc;
    Button post;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpart_time);
        jobtitlept=findViewById(R.id.jobtitlept);
        Experience=findViewById(R.id.Experience);
        editEmpNeeded=findViewById(R.id.editEmpNeeded);
        etjobDesc=findViewById(R.id.etjobDesc);
        post=findViewById(R.id.post);

        tvDay=findViewById(R.id.day);
        tvHours=findViewById(R.id.hours);


        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.Jtitle, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobtitlept.setAdapter(adapter);
        jobtitlept.setSelection(0);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        ArrayAdapter <CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.Eneeded, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Experience.setAdapter(adapter1);
        Experience.setSelection(0);

        selectedDay = new boolean[dayArray.length];
        selectedHour= new boolean[hourArray.length];
        tvHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(Rpart_time.this);
                builder.setTitle("Select Hours");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(hourArray, selectedHour, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            hourList.add(i);
                            Collections.sort(hourList);
                        }else {
                            hourList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder=new StringBuilder();
                        for(int j=0;j<hourList.size();j++){
                            stringBuilder.append(hourArray[hourList.get(j)]);
                            if(j!=hourList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        tvHours.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j=0;j<selectedHour.length;j++){
                            selectedHour[j]=false;
                            hourList.clear();
                            tvHours.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
        tvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(Rpart_time.this);
                builder.setTitle("Select Days");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            dayList.add(i);
                            Collections.sort(dayList);
                        }else {
                            dayList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder=new StringBuilder();
                        for(int j=0;j<dayList.size();j++){
                            stringBuilder.append(dayArray[dayList.get(j)]);
                            if(j!=dayList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        tvDay.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j=0;j<selectedDay.length;j++){
                            selectedDay[j]=false;
                            dayList.clear();
                            tvDay.setText("");
                        }
                    }
                });
                builder.show();
            }
        });


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
                params.put("days",tvDay.getText().toString());
                params.put("hours",tvHours.getText().toString());
                params.put("category","partTime");
                params.put("jTitle",jobtitlept.getSelectedItem().toString());
                params.put("jDesc",etjobDesc.getText().toString());
                params.put("expNeeded",Experience.getSelectedItem().toString());
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