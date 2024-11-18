package com.healthmanagement;

import com.healthmanagement.config.DatabaseConfig;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;

public class DatabaseTest {

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = DatabaseConfig.getDataSource().getConnection()) {
            assertTrue(conn.isValid(1));
        } catch (Exception e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }
}