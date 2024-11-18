/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/
package com.healthmanagement;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;

import org.junit.Test;

import com.healthmanagement.config.DatabaseConfig;

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