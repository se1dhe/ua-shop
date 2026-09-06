package com.cyfrahub.backend.infrastructure.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:#{null}}")
    private String databaseUrl;

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();

        String url = dataSource.getJdbcUrl();
        if (databaseUrl != null && !databaseUrl.isBlank()) {
            url = databaseUrl.trim();
        }

        if (url != null) {
            if (url.startsWith("postgresql://")) {
                url = "jdbc:" + url;
            } else if (url.startsWith("postgres://")) {
                url = "jdbc:postgresql://" + url.substring("postgres://".length());
            }
            dataSource.setJdbcUrl(url);
        }

        return dataSource;
    }
}
