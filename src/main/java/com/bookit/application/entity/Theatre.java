package com.bookit.application.entity;

public class Theatre {
    private String name;
    private String location;
    private long id;
    private String theatreId;


    public Theatre(String name, String location, long id) {
        this.name = name;
        this.location = location;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public long getId() {
        return id;
    }
}
