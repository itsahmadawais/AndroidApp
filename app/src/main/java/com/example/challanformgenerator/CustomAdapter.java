package com.example.challanformgenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    ArrayList<RequestsClass> dataholder;
    public Context context;
    public CustomAdapter(Context context,ArrayList<RequestsClass> dataholder)
    {
        this.context=context;
        this.dataholder=dataholder;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.applicationrow,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(dataholder.get(position).title);
        holder.date.setText(dataholder.get(position).date);
        holder.StatusColor("#38944a");
        if(dataholder.get(position).status.equals("pending") || dataholder.get(position).status.equals("Pending"))
        {
            holder.StatusColor("#ff3e30");
        }
        holder.status.setText(dataholder.get(position).status.toUpperCase());
    }

    @Override
    public int getItemCount() {
        return this.dataholder.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView name,date,status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imgUser);
            name = (TextView) itemView.findViewById(R.id.txtname);
            date = (TextView) itemView.findViewById(R.id.txtDate);
            status = (TextView) itemView.findViewById(R.id.txtApplicationStatus);
        }
        public void StatusColor(String color)
        {
            status.setTextColor(Color.parseColor(color));
        }

        @Override
        public void onClick(View v) {
            int postion = this.getAdapterPosition();
            SessionManager sessionManager = new SessionManager(v.getContext());
            if(sessionManager.getUserrole().equals("admin") || sessionManager.getUserrole().equals("coordinator"))
            {
                Intent intent= new Intent(context,RequestApproveActivity.class);
                intent.putExtra("rowID",dataholder.get(postion).rowID);
                context.startActivity(intent);

            }
        }
    }
}
