package com.bookit.application.DTO.theatre;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TheatreDTO {
    private String name;
    private String location;
    private Long id;

    @JsonCreator
    public TheatreDTO(String name, String location, Long id) {
        this.name = name;
        this.location = location;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
