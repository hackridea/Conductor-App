package com.example.conductorapp.utils;

import java.util.ArrayList;

public class RouteList {
    private int length=0;

    public int getLength() {
        return length;
    }

    ArrayList<Route> list;
    public RouteList(){
        list=new ArrayList<Route>();
    }

    public boolean add(Route route){
        try{
        list.add(route);
        length+=1;
        return true;
        }catch (Exception e){
            return false;
        }
    }
    boolean contains(Route route){
        return list.contains(route);
    }

    public Route get(int position) {
        return list.get(position);
    }
}
