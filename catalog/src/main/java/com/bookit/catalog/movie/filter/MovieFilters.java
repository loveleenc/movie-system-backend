package com.bookit.catalog.movie.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieFilters {

    @Bean
    public FilterRegistrationBean<MovieGenreLanguageFilters> createGenreLanguageFilter(){
        FilterRegistrationBean<MovieGenreLanguageFilters> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new MovieGenreLanguageFilters());
        registrationBean.addUrlPatterns("/movies/filter");
        registrationBean.setOrder(2);

        return registrationBean;
    }
}
