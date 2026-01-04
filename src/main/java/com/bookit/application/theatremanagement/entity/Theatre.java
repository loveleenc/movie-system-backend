package com.bookit.application.theatremanagement.entity;


import java.util.*;
import java.util.stream.Collectors;

public class Theatre {
    private String name;
    private String location;
    private List<Seat> seats;
    private Integer id;
    private Long ownerId;


    public Theatre(String name, String location, Integer id) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Map<SeatCategory, Long> getSeatCategoryAndPrices(){
        Set<SeatCategory> categories = new HashSet<>();
        return this.seats.stream().filter(seat -> categories.add(seat.getSeatType())).collect(Collectors.toMap(Seat::getSeatType, Seat::getSeatPrice));
    }
}
