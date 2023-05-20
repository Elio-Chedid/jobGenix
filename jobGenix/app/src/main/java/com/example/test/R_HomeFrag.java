package com.example.test;

import static android.content.Context.MODE_PRIVATE;
import static com.example.test.LogIn.sharedpref;
import static com.example.test.Welcome.accepteds;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link R_HomeFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class R_HomeFrag extends Fragment {
    RecyclerView recAcceptedR;
    RequestQueue requestQueue;
    AdapterAcceptedR adapterAcceptedR;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public R_HomeFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment R_HomeFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static R_HomeFrag newInstance(String param1, String param2) {
        R_HomeFrag fragment = new R_HomeFrag();
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
        View rootview=inflater.inflate(R.layout.fragment_r__home, container, false);
        recAcceptedR=rootview.findViewById(R.id.recAcceptedR);
        adapterAcceptedR=new AdapterAcceptedR(getActivity(),accepteds);
        requestQueue= Volley.newRequestQueue(getContext());
        //sendData();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recAcceptedR.setLayoutManager(linearLayoutManager);

        recAcceptedR.setAdapter(adapterAcceptedR);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterAcceptedR.notifyDataSetChanged();
            }
        },2000);



        return rootview;
    }



    private void sendData() {
        accepteds=new ArrayList<>();
        String URL = MyIP.getIP()+"/viewAccR";
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
                            String U_fname=array.getString(0);
                            String U_lname=array.getString(1);
                            String U_phone=array.getString(2);
                            String U_longtitude=array.getString(3);
                            String U_latitude=array.getString(4);
                            AcceptedR accepted=new AcceptedR(U_fname,U_lname,U_phone,U_longtitude,U_latitude);
                            if (accepteds.size()==0){
                                accepteds.add(accepted);
                            }else if(!checkExist(accepted)){
                                accepteds.add(accepted);
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
                SharedPreferences sharedPreferences=getContext().getSharedPreferences(sharedpref,MODE_PRIVATE);
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

    private boolean checkExist(AcceptedR acceptedR){
        for(AcceptedR a:accepteds){
            if(a.getPhone().equals(acceptedR.getPhone())){
                return true;
            }
        }
        return false;
    }

}