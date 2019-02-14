package com.example.conductorapp.utils;


import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.conductorapp.JourneyActivity;
import com.example.conductorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class RouteListAdapter extends ArrayAdapter<Route> {
    private final ArrayList<Route> list;
    private final Activity context;



    public RouteListAdapter(ArrayList<Route> list, Activity context){
        super(context,R.layout.layout,list);
        this.context=context;
        this.list=list;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        if(convertView==null)convertView=inflater.inflate(R.layout.layout,parent,false);
        TextView tv= convertView.findViewById(R.id.route);
        tv.setText("From: "+list.get(position).from+"\tTo: "+list.get(position).to);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intt=new Intent(context, JourneyActivity.class);
               intt.putExtra("route",list.get(position).getRid());
               context.startActivity(intt);

            }
        });

        return convertView;
    }


}
