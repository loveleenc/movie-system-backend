package com.bookit.application.entity;

import java.util.List;

public class Theatre {
    private String name;
    private String location;
    private List<Seat> seats;
    private long id;


    public Theatre(String name, String location, Long id) {
        this.name = name;
        this.location = location;
        this.id = id;
    }

    public Theatre(){}

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
