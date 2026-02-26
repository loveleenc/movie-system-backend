package com.bookit.catalog.movie.entity;

import java.util.List;

public record MoviePage(Integer pages, List<Movie> movies) {
}
