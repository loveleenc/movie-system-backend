package com.bookit.application.showscheduling.dto.inbound.api;


import java.time.LocalDateTime;

public record ShowTheatreDto(TheatreDto theatreDto, LocalDateTime startTime, LocalDateTime endTime, String language, String id) {}
