package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterAceeptedE extends RecyclerView.Adapter<AdapterAceeptedE.viewHolder> {
    Context ctx;
    ArrayList<Accepted> accepteds;
    public AdapterAceeptedE(Context ctx,ArrayList<Accepted> accepteds){
        this.ctx=ctx;
        this.accepteds=accepteds;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v= LayoutInflater.from(ctx).inflate(R.layout.view_accepted_jobs,parent,false);
        return new viewHolder(v);
    }
    @Override
    public void onBindViewHolder (@NonNull AdapterAceeptedE.viewHolder holder ,int position){
        Accepted accepted = accepteds.get(position);
        holder.txtCnameE.setText(accepted.getC_name());
        holder.txtJdescE.setText(accepted.getJdesc());
        holder.txtJtitleE.setText(accepted.getJtitle());
    }

    @Override
    public int getItemCount(){return accepteds.size();}








    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView txtJtitleE,txtJdescE,txtCnameE;

        public viewHolder(@NonNull View itemView){
            super(itemView);
            txtJtitleE=itemView.findViewById(R.id.txtJtitleE);
            txtJdescE=itemView.findViewById(R.id.txtJdescE);
            txtCnameE=itemView.findViewById(R.id.txtCnameE);
        }
    }
}
