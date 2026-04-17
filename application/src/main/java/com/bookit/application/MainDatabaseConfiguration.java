package com.bookit.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Configuration
public class MainDatabaseConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.main")
    public DataSourceProperties getMainDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean("mainDataSource")
    public DataSource getMainDataSource(){
        return getMainDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean("mainJdbcTemplate")
    public JdbcTemplate getMainDataSourceJdbcTemplate(@Qualifier("mainDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
