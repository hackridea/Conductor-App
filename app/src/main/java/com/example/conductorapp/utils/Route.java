package com.example.conductorapp.utils;

public class Route {
    public String  id;
    public String from;
    public String to;

    public Route(String id, String from, String to, String rid) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.rid = rid;
    }

    public String rid;

    public Route(){

    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Route(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
