package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterAcceptedR extends RecyclerView.Adapter<AdapterAcceptedR.viewHolder> {
    Context ctx;
    ArrayList<AcceptedR> accepteds;
    public AdapterAcceptedR(Context ctx,ArrayList<AcceptedR> accepteds){
        this.ctx=ctx;
        this.accepteds=accepteds;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v= LayoutInflater.from(ctx).inflate(R.layout.view_acceptedr_jobs,parent,false);
        return new viewHolder(v);
    }
    @Override
    public void onBindViewHolder (@NonNull AdapterAcceptedR.viewHolder holder ,int position){
        AcceptedR accepted = accepteds.get(position);
        holder.txtfnameAR.setText(accepted.getFname());
        holder.txtlnameAR.setText(accepted.getLname());
        holder.txtphoneAR.setText(accepted.getPhone());
        holder.txtfnameAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:"+accepted.getLat()+","+accepted.getLongt()));
                Intent chooser=Intent.createChooser(intent,"Launch Maps");
                ctx.startActivity(chooser);
            }
        });
    }

    @Override
    public int getItemCount(){return accepteds.size();}








    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView txtfnameAR,txtlnameAR,txtphoneAR;

        public viewHolder(@NonNull View itemView){
            super(itemView);
            txtfnameAR=itemView.findViewById(R.id.txtfnameAR);
            txtlnameAR=itemView.findViewById(R.id.txtlnameAR);
            txtphoneAR=itemView.findViewById(R.id.txtphoneAR);
        }
    }
}
