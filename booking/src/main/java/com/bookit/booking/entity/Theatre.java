package com.bookit.booking.entity;


public class Theatre {
    private String name;
    private String location;
    private Integer id;


    public Theatre(String name, String location, Integer id) {
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

    public Integer getId() {
        return id;
    }

}
