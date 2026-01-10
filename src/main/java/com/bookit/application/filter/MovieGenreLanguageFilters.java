package com.bookit.application.filter;

import com.bookit.application.moviecatalog.entity.types.MovieGenre;
import com.bookit.application.moviecatalog.entity.types.MovieLanguage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Order(1)
public class MovieGenreLanguageFilters implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Map<String, Object> errorDetails = new HashMap<>();

        String[] genre = req.getParameterValues("genre");
        String[] languages = req.getParameterValues("language");

        if(genre != null){
            for(String possibleGenre: genre){
                if(!MovieGenre.isMovieGenreEnum(possibleGenre)){
                    res.setStatus(HttpStatus.BAD_REQUEST.value());
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    errorDetails.put("error", String.format("Provided genre is incorrect: %s", possibleGenre));
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(res.getWriter(), errorDetails);
                    return;
                }
            }
        }

        if(languages != null){
            for(String possibleLanguage: languages){
                if(!MovieLanguage.isMovieLanguageEnum(possibleLanguage)){
                    res.setStatus(HttpStatus.BAD_REQUEST.value());
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    errorDetails.put("error", String.format("Provided language is incorrect: %s", possibleLanguage));
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(res.getWriter(), errorDetails);
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}

