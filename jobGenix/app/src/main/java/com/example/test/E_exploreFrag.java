package com.example.test;

import static android.content.Context.MODE_PRIVATE;
import static com.example.test.LogIn.sharedpref;
import static com.example.test.PreHome.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link E_exploreFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class E_exploreFrag extends Fragment {
    Button EbtnOnDemand,EbtnFullTime,EbtnPartTime;
    RequestQueue requestQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public E_exploreFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment E_exploreFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static E_exploreFrag newInstance(String param1, String param2) {
        E_exploreFrag fragment = new E_exploreFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_e_explore, container, false);
        EbtnOnDemand=rootview.findViewById(R.id.EbtnOnDemand);
        EbtnFullTime=rootview.findViewById(R.id.EbtnFullTime);
        EbtnPartTime=rootview.findViewById(R.id.EbtnPartTime);
        EbtnPartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData1("partTime");
                startActivity(new Intent(getActivity(),Epart_time.class));
                getActivity().finish();

            }
        });
        EbtnOnDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData1("onDemand");
                startActivity(new Intent(getActivity(),EonDemand.class));
                getActivity().finish();
            }
        });

        EbtnFullTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData1("fullTime");
                startActivity(new Intent(getActivity(),EfullTime.class));
                getActivity().finish();
            }
        });



        return rootview;
    }


    private void sendData1(String category) {
        requestQueue= Volley.newRequestQueue(getContext());
        String URL = MyIP.getIP()+"/FetchJobs";
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
                            String cname=array.getString(0);
                            String longt=array.getString(1);
                            String lat=array.getString(2);
                            String expneeded=array.getString(3);
                            String jtitle=array.getString(4);
                            String jdesc=array.getString(5);
                            String id=array.getString(6);
                            String category=array.getString(7);
                            String days=array.getString(8);
                            String hours=array.getString(9);
                            Fetched fetched;
                            if(category.equals("partTime")){
                                fetched=new Fetched(cname,longt,lat,expneeded,jtitle,jdesc,id,category,days,hours);

                            }else {
                                fetched=new Fetched(cname,longt,lat,expneeded,jtitle,jdesc,id,category);

                            }
                            if (list.size()==0){
                                list.add(fetched);
                            }else if(!checkExist1(fetched)){
                                list.add(fetched);
                            }
                        }



                    }else{
                        Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences=getActivity().getSharedPreferences(sharedpref,MODE_PRIVATE);
                String token=sharedPreferences.getString("token",null);
                Map<String,String> params=new HashMap<String,String>();
                params.put("category",category);
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

    private boolean checkExist1(Fetched fetched){
        for(Fetched f:list){
            if(f.getId()==fetched.getId()){
                return true;
            }
        }
        return false;
    }
}