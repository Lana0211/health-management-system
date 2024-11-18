package com.healthmanagement.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {
    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/health_db");
            config.setUsername("health_user");
            config.setPassword("health_password");
            config.setMaximumPoolSize(10);

            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}