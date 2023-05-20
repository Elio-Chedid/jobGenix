package com.example.test;

import static com.example.test.LogIn.sharedpref;
import static com.example.test.PreHome.accepteds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeePanel extends AppCompatActivity {
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_employee_panel);
        bottomNavigationView = findViewById(R.id.bottomNavigationView1);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        sendData();
        loadFragment(new E_HomeFrag());
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.e_home:
                    loadFragment(new E_HomeFrag());
                    break;

                case R.id.e_explore:
                    loadFragment(new E_exploreFrag());
                    break;

                case R.id.e_profile:
                    loadFragment(new E_ProfileFrag());
                    break;


            }

            return true;
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout1,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void sendData() {
        accepteds=new ArrayList<>();
        String URL = MyIP.getIP()+"/viewAccE";
        StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean success=jsonObject.getBoolean("success");
                    if(success){
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
}