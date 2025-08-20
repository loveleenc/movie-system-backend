package com.bookit.application.types;

import java.util.List;
import java.util.stream.Stream;

public enum MovieLanguage {
    ENGLISH("English"),
    HINDI("Hindi"),
    TAMIL("Tamil"),
    TELUGU("Telugu"),
    KANNADA("Kannada"),
    MALAYALAM("Malayalam"),
    BENGALI("Bengali"),
    MARATHI("Marathi"),
    PUNJABI("Punjabi"),
    GUJARATI("Gujarati"),
    BHOJPURI("Bhojpuri");

    private final String code;

    MovieLanguage(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static List<String> getAllCodes(){
        return Stream.of(MovieLanguage.values()).map(MovieLanguage::getCode).toList();
    }

    public static boolean isMovieLanguageEnum(String possibleEnum){
        for(MovieLanguage language: MovieLanguage.values()){
            if(language.getCode().equals(possibleEnum)){
                return true;
            }
        }
        return false;
    }
}
