package com.example.conductorapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.conductorapp.AddRouteActivity;
import com.example.conductorapp.R;
import com.example.conductorapp.models.Point;

import java.util.ArrayList;
import java.util.List;

public class PointListAdapter extends ArrayAdapter<Point> {

    ArrayList<Point> points;
    Activity context;

    public PointListAdapter( Activity context, ArrayList<Point> objects) {
        super(context,R.layout.layout, objects);
        this.points=objects;
        this.context=context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        if(convertView==null)convertView=inflater.inflate(R.layout.layout,parent,false);
        TextView tv= convertView.findViewById(R.id.route);
        tv.setText(points.get(position).getName()+"("+points.get(position).getCost()+")");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : Edit Node

            }
        });

        return convertView;
    }
}
