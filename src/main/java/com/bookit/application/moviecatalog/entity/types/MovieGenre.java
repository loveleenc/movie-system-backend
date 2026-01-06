package com.bookit.application.types;

import java.util.List;
import java.util.stream.Stream;

public enum MovieGenre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    FILM_NOIR("Film-Noir"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSICAL("Musical"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi"),
    SHORT("Short"),
    SPORT("Sport"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western");

    private final String code;

    MovieGenre(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static List<String> getAllCodes(){
        return Stream.of(MovieGenre.values()).map(MovieGenre::code).toList();
    }

    public static boolean isMovieGenreEnum(String possibleGenre){
        for(MovieGenre genre: MovieGenre.values()){
            if(genre.code().equals(possibleGenre)){
                return true;
            }
        }
        return false;
    }


}

