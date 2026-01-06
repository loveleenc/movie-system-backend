package com.bookit.application.showscheduling.dto.inbound.api;

import java.time.LocalDateTime;

public record ShowMovieNameDto(LocalDateTime startTime, LocalDateTime endTime, String language, String id, String movieName) {}
