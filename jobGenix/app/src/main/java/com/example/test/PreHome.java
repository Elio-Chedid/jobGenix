package com.example.test;

import static com.example.test.LogIn.sharedpref;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreHome extends AppCompatActivity {
    Spinner spinJtitle,spinExpneed;
    Button btnAdvance;
    RequestQueue requestQueue;
    public static String EJTITLE,EEXPNEEDED;
    public static ArrayList<Accepted> accepteds;
    public static ArrayList<Fetched> list =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_home);
        list.clear();
        spinJtitle = findViewById(R.id.spinJtitle);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this, R.array.Jtitle, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinJtitle.setAdapter(adapter);
        spinJtitle.setSelection(0);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        spinExpneed=findViewById(R.id.spinExpneed);
        ArrayAdapter <CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.Eneeded, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinExpneed.setAdapter(adapter1);
        spinExpneed.setSelection(0);
        btnAdvance=findViewById(R.id.btnAdvance);
        btnAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EJTITLE=spinJtitle.getSelectedItem().toString();
                EEXPNEEDED=spinExpneed.getSelectedItem().toString();
                sendData();
                //sendData1();
                Intent i =new Intent(getApplicationContext(),EmployeePanel.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void sendData() {
        String URL = MyIP.getIP()+"/viewAccE";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        accepteds=new ArrayList<>();
                        JSONArray messageArray = jsonObject.getJSONArray("message");
                        for (int i=0;i<messageArray.length();i++){
                            JSONArray array=messageArray.getJSONArray(i);
                            String cname=array.getString(2);
                            String jtitle=array.getString(0);
                            String jdesc=array.getString(1);
                            Accepted accepted=new Accepted(jtitle,jdesc,cname);
                            if (accepteds.size()==0){
                                accepteds.add(accepted);
                            }else if(!checkExist(accepted)){
                                accepteds.add(accepted);
                            }
                        }



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

    private boolean checkExist(Accepted accepted){
        for(Accepted a:accepteds){
            if(a.getC_name().equals(accepted.getC_name())){
                return true;
            }
        }
        return false;
    }

    private void sendData1() {
        String URL = MyIP.getIP()+"/FetchJobs";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
                        list=new ArrayList<>();
                        JSONArray messageArray = jsonObject.getJSONArray("message");
                        for (int i=0;i<messageArray.length();i++){
                            JSONArray array=messageArray.getJSONArray(i);
                            String cname=array.getString(0);
                            String longt=array.getString(1);
                            String lat=array.getString(2);
                            String expneeded=array.getString(3);
                            String jtitle=array.getString(4);
                            String jdesc=array.getString(5);
                            String id=array.getString(6);
                            String category=array.getString(7);
                            Fetched fetched=new Fetched(cname,longt,lat,expneeded,jtitle,jdesc,id,category);
                            if (list.size()==0){
                                list.add(fetched);
                            }else if(!checkExist1(fetched)){
                                list.add(fetched);
                            }
                        }



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
                params.put("jTitle",PreHome.EJTITLE);
                params.put("expNeeded",PreHome.EEXPNEEDED);
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
    private void refresh(){
        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {

            }
        };
        handler.postDelayed(runnable,10000);
    }

    private boolean checkExist1(Fetched fetched){
        for(Fetched f:list){
            if(f.getId()==fetched.getId()){
                return true;
            }
        }
        return false;
    }

}