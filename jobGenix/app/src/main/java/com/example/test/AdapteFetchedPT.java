package com.example.test;

import static android.content.Context.MODE_PRIVATE;
import static com.example.test.LogIn.sharedpref;
import static com.example.test.PreHome.accepteds;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdapteFetchedPT extends RecyclerView.Adapter<AdapteFetchedPT.viewHolder> {
    Context ctx;
    ArrayList<Fetched> fetcheds;

    public AdapteFetchedPT(Context ctx, ArrayList<Fetched> fetcheds){
        this.ctx=ctx;
        this.fetcheds=fetcheds;
    }



    @NonNull
    @Override
    public AdapteFetchedPT.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.view_pt_fetched_jobs,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapteFetchedPT.viewHolder holder, int position) {
        Fetched fetched=fetcheds.get(position);
        holder.txtCname.setText(fetched.getCname());
        holder.txtJdesc.setText(fetched.getJdesc());
        holder.txtJtitle.setText(fetched.getJtitle());
        holder.txtSchedule.setText("Days: "+fetched.getDays()+"\nHours: "+fetched.getHours());
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue requestQueue= Volley.newRequestQueue(ctx);

                String URL = MyIP.getIP()+"/Accept";
                StringRequest request=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Boolean success=jsonObject.getBoolean("success");
                            if(success){
                                ctx.startActivity(new Intent(ctx,EmployeePanel.class));
                                ((Activity)ctx).finish();
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                holder.btnAccept.setClickable(false);
                            }else{
                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
                        SharedPreferences sharedPreferences=ctx.getSharedPreferences(sharedpref,ctx.MODE_PRIVATE);
                        String token=sharedPreferences.getString("token",null);
                        Map<String,String> params=new HashMap<String,String>();
                        params.put("token",token);
                        params.put("R_id",fetched.getId());
                        params.put("jtitle",fetched.getJtitle());
                        params.put("jdesc",fetched.getJdesc());
                        params.put("cname",fetched.getCname());
                        return params;
                    }
                };
                try{
                    request.setRetryPolicy(new DefaultRetryPolicy(10000,1,1.0f));
                    requestQueue.add(request);
                }catch (Exception e){
                    e.printStackTrace();
                }
                sendData();

            }
        });

    }

    @Override
    public int getItemCount() {
        return fetcheds.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtJtitle,txtJdesc,txtCname,txtSchedule;
        Button btnAccept;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtJtitle=itemView.findViewById(R.id.txtJtitle);
            btnAccept=itemView.findViewById(R.id.btnAccept);
            txtJdesc=itemView.findViewById(R.id.txtJdesc);
            txtCname=itemView.findViewById(R.id.txtCname);
            txtSchedule=itemView.findViewById(R.id.schedule);
        }
    }

    private void sendData() {
        String URL = MyIP.getIP()+"/viewAccE";
        RequestQueue requestQueue= Volley.newRequestQueue(ctx);

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
                        Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
                SharedPreferences sharedPreferences=ctx.getSharedPreferences(sharedpref,MODE_PRIVATE);
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
