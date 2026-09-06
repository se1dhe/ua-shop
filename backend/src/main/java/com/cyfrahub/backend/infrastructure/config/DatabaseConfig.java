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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
            String cleanUrl = rawUrl.startsWith("jdbc:") ? rawUrl.substring(5) : rawUrl;

            if (cleanUrl.startsWith("postgres://") || cleanUrl.startsWith("postgresql://")) {
                try {
                    URI uri = new URI(cleanUrl);
                    String host = uri.getHost();
                    int port = uri.getPort() != -1 ? uri.getPort() : 5432;
                    String path = uri.getPath();
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

        // Clean legacy beauty-shop schema if invalid 'users' table exists without 'email' column
        cleanLegacyTablesIfPresent(dataSource);

        return dataSource;
    }

    private void cleanLegacyTablesIfPresent(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            boolean hasLegacyUsers = false;
            ResultSet rs = stmt.executeQuery(
                    "SELECT column_name FROM information_schema.columns " +
                    "WHERE table_name = 'users' AND column_name = 'email'"
            );
            if (!rs.next()) {
                // Table 'users' exists but does NOT have 'email' column -> legacy beauty shop table!
                ResultSet tableCheck = stmt.executeQuery(
                        "SELECT 1 FROM information_schema.tables WHERE table_name = 'users'"
                );
                if (tableCheck.next()) {
                    hasLegacyUsers = true;
                }
            }

            if (hasLegacyUsers) {
                log.warn("Detected legacy beauty-shop database schema! Wiping public schema to initialize fresh CyfraHub tables...");
                stmt.execute("DROP SCHEMA public CASCADE; CREATE SCHEMA public;");
                log.info("Legacy schema wiped successfully. Ready for fresh CyfraHub initialization.");
            }
        } catch (Exception e) {
            log.info("Schema check completed (or empty database): {}", e.getMessage());
        }
    }
}
