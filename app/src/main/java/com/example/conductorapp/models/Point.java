package com.example.conductorapp.models;

public class Point {
    String name,cost;
    double lon,lat;

   public Point(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Point(String name, double lat, double lon, String cost) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.cost = cost;
    }
}
