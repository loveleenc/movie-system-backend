package com.bookit.events.shows.dto.outbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieDto {
  private String name;
  private Integer duration;
  private String poster;
  private List<String> genreList;
  private List<String> languages;
  private LocalDate releaseDate;
  private Long id;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getDuration() {
    return duration;
  }

  public String getPoster() {
    return poster;
  }

  public List<String> getGenreList() {
    return genreList;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public List<String> getLanguages() {
    return this.languages;
  }

  @JsonCreator
  public MovieDto(String name, Integer duration, String poster, List<String> genreList, List<String> languages, LocalDate releaseDate, Long id) {
    this.name = name;
    this.duration = duration;
    this.poster = poster;
    this.genreList = genreList;
    this.languages = languages;
    this.releaseDate = releaseDate;
    this.id = id;
  }
}
