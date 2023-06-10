package com.mosaic.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public DataSource getDefaultDatasource() {
        DriverManagerDataSource dm = new DriverManagerDataSource();
        dm.setDriverClassName("org.sqlite.JDBC");

        return dm;
    }

}
