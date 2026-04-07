package com.bookit.catalog.movie.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
class DatabaseConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.movieinfo")
    public DataSourceProperties getMovieInfoDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean("movieInfoDataSource")
    public DataSource getMovieInfoDataSource(){
        return getMovieInfoDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate getMovieInfoDataSourceJdbcTemplate(@Qualifier("movieInfoDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }


    @Bean
    @ConditionalOnProperty(name = "catalog.source", havingValue = "external", matchIfMissing = true)
    @ConfigurationProperties("spring.datasource.main")
    public DataSourceProperties getMainDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean("mainDataSource")
    @ConditionalOnProperty(name = "catalog.source", havingValue = "external", matchIfMissing = true)
    public DataSource getMainDataSource(){
        return getMainDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean("mainJdbcTemplate")
    @ConditionalOnProperty(name = "catalog.source", havingValue = "external", matchIfMissing = true)
    public JdbcTemplate getMainDataSourceJdbcTemplate(@Qualifier("mainDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
