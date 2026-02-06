package com.bookit.catalog.movie.inbound.service;

import java.util.List;

public record MoviePageServiceDto(Integer pages, List<MovieServiceDto> movies) { }
