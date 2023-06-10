package com.example.plant_leaf_disase_app;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterDisplay extends  RecyclerView.Adapter<AdapterDisplay.ViewHolder>{

    ArrayList<My> mList;
    Context context;

    public AdapterDisplay(ArrayList<My> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        My vacancy1 = mList.get(position);
        holder.txtname.setText("Shop Name "+vacancy1.getName());
        holder.txtaddress.setText("Address "+vacancy1.getAddress());
        holder.txtprofile.setText("Number "+vacancy1.getContactno());
        holder.txtpro.setText("Product: "+vacancy1.getProduct());
        holder.btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.google.co.in/maps/dir/"+"/"+ vacancy1.getAddress());

                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        Glide.with(holder.img1.getContext()).load(vacancy1.getImageurl()).into(holder.img1);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtname,txtaddress,txtprofile,txtpro;

        Button btnsend;
        ImageView img1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtname = itemView.findViewById(R.id.nametext);
            txtaddress = itemView.findViewById(R.id.coursetext);
            txtprofile = itemView.findViewById(R.id.emailtext);
            img1 = itemView.findViewById(R.id.img1);
            txtpro = itemView.findViewById(R.id.text);

            btnsend = itemView.findViewById(R.id.btnsend);
        }
    }

}
