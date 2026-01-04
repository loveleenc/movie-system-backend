package com.bookit.application.theatremanagement.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TheatreDto {
    private String name;
    private String location;
    private Integer id;
    @JsonProperty("theatreRows")
    private List<TheatreRowDto> theatreRowDtos;


    @JsonCreator
    public TheatreDto(String name, String location, Integer id) {
        this.name = name;
        this.location = location;
        this.id = id;
    }

    public TheatreDto(String name, String location, List<TheatreRowDto> theatreRowDtos) {
        this.name = name;
        this.location = location;
        this.theatreRowDtos = theatreRowDtos;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<TheatreRowDto> getRowDetailsDto() {
        return theatreRowDtos;
    }
}
