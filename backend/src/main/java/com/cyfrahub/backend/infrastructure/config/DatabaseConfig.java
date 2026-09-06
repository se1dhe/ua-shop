package com.cyfrahub.backend.infrastructure.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${DATABASE_URL:#{null}}")
    private String databaseUrl;

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();

        String rawUrl = databaseUrl != null && !databaseUrl.isBlank() ? databaseUrl.trim() : dataSource.getJdbcUrl();

        if (rawUrl != null) {
            // Strip leading "jdbc:" if present for URI parsing
            String cleanUrl = rawUrl.startsWith("jdbc:") ? rawUrl.substring(5) : rawUrl;

            if (cleanUrl.startsWith("postgres://") || cleanUrl.startsWith("postgresql://")) {
                try {
                    // Standard URI format: postgresql://user:password@host:port/database
                    URI uri = new URI(cleanUrl);
                    String host = uri.getHost();
                    int port = uri.getPort() != -1 ? uri.getPort() : 5432;
                    String path = uri.getPath(); // /dbname
                    String userInfo = uri.getUserInfo();

                    String jdbcUrl = String.format("jdbc:postgresql://%s:%d%s", host, port, path);
                    dataSource.setJdbcUrl(jdbcUrl);

                    if (userInfo != null && userInfo.contains(":")) {
                        String[] parts = userInfo.split(":", 2);
                        dataSource.setUsername(parts[0]);
                        dataSource.setPassword(parts[1]);
                    }

                    log.info("Configured JDBC connection to host: {}, port: {}, database: {}", host, port, path);
                } catch (Exception e) {
                    log.error("Failed to parse DATABASE_URL URI, fallback to raw url", e);
                    dataSource.setJdbcUrl(rawUrl.startsWith("jdbc:") ? rawUrl : "jdbc:" + rawUrl);
                }
            } else {
                dataSource.setJdbcUrl(rawUrl.startsWith("jdbc:") ? rawUrl : "jdbc:" + rawUrl);
            }
        }

        return dataSource;
    }
}
