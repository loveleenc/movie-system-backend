package com.bookit.catalog.movie.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
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
    @Conditional(MainDataSourceCondition.class)
    @ConfigurationProperties("spring.datasource.main")
    public DataSourceProperties getMainDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean("mainDataSource")
    @Conditional(MainDataSourceCondition.class)
    public DataSource getMainDataSource(){
        return getMainDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean("mainJdbcTemplate")
    @Conditional(MainDataSourceCondition.class)
    public JdbcTemplate getMainDataSourceJdbcTemplate(@Qualifier("mainDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
