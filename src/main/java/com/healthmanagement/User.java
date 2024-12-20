/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/
package com.healthmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.healthmanagement.config.DatabaseConfig;
import com.healthmanagement.goals.AbstractHealthGoal;
import com.healthmanagement.goals.HealthGoalFactory;
import com.healthmanagement.goals.HealthGoalInterface;

public class User {
    // 屬性
    private String userID;
    private String username;
    private String password;
    private String name;
    private int age;
    private String gender;

    // 建構子
    public User(String username, String password, String name, int age, String gender) {
        this.userID = generateUserID();
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    // Getters and Setters
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // 用戶驗證相關方法
    public static User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"));
                    user.setUserID(rs.getString("user_id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 健康數據相關方法
    public boolean uploadHealthData(HealthData data) {
        if (!data.validateData()) {
            return false;
        }

        String sql = "INSERT INTO health_data (data_id, user_id, weight, steps, recorded_at) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, data.getDataID());
            pstmt.setString(2, this.userID);
            pstmt.setDouble(3, data.getWeight());
            pstmt.setInt(4, data.getSteps());

            boolean success = pstmt.executeUpdate() > 0;

            if (success) {
                List<HealthGoalInterface> activeGoals = getActiveGoals();
                for (HealthGoalInterface goal : activeGoals) {
                    if (goal instanceof AbstractHealthGoal) {
                        ((AbstractHealthGoal) goal).setUser(this);
                    }
                    goal.isGoalAchieved(data);
                }
            }

            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 輔助方法
    private String generateUserID() {
        return "USER_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }

    public boolean register() {
        if (isUsernameExists(this.username)) {
            return false;
        }

        String sql = "INSERT INTO users (user_id, username, password, name, age, gender) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.userID);
            pstmt.setString(2, this.username);
            pstmt.setString(3, this.password);
            pstmt.setString(4, this.name);
            pstmt.setInt(5, this.age);
            pstmt.setString(6, this.gender);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 添加獲取用戶健康數據的方法
    public HealthData getLatestHealthData() {
        String sql = "SELECT * FROM health_data WHERE user_id = ? ORDER BY recorded_at DESC LIMIT 1";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.userID);
            System.out.println("Executing query for user ID: " + this.userID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Found health data record");
                    return new HealthData(
                            rs.getString("user_id"),
                            rs.getDouble("weight"),
                            rs.getInt("steps"));
                } else {
                    System.out.println("No health data found for user");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving health data: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 添加獲取用戶目標的方法
    public List<HealthGoalInterface> getActiveGoals() {
        String sql = "SELECT * FROM health_goals WHERE user_id = ? AND end_date >= CURRENT_DATE AND status = 'IN_PROGRESS'";
        List<HealthGoalInterface> goals = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.getUserID());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    goals.add(HealthGoalFactory.createHealthGoal(
                            this,
                            rs.getString("goal_type"),
                            rs.getDouble("target_value"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goals;
    }

    // 添加生成健康報告的方法
    public HealthReport generateHealthReport() {
        HealthData latestData = getLatestHealthData();
        List<HealthGoalInterface> activeGoals = getActiveGoals();

        // 檢查並更新所有目標的狀態
        if (latestData != null && activeGoals != null) {
            for (HealthGoalInterface goal : activeGoals) {
                if (goal instanceof AbstractHealthGoal) {
                    ((AbstractHealthGoal) goal).setUser(this);
                }
                goal.isGoalAchieved(latestData);
            }
        }

        HealthReport report = new HealthReport(userID, latestData, activeGoals);
        saveHealthReport(report);
        return report;
    }

    public boolean setHealthGoal(HealthGoalInterface goal) {
        String sql = "INSERT INTO health_goals (goal_id, user_id, goal_type, target_value, start_date, end_date, status) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, goal.getGoalID());
            pstmt.setString(2, this.userID);
            pstmt.setString(3, goal.getGoalType());
            pstmt.setDouble(4, goal.getTargetValue());
            pstmt.setDate(5, new java.sql.Date(goal.getStartDate().getTime()));
            pstmt.setDate(6, new java.sql.Date(goal.getEndDate().getTime()));
            pstmt.setString(7, goal.getStatus());

            boolean success = pstmt.executeUpdate() > 0;

            if (success) {
                // 設置目標後,立即檢查最新的健康數據
                HealthData latestData = getLatestHealthData();
                if (latestData != null) {
                    if (goal instanceof AbstractHealthGoal) {
                        ((AbstractHealthGoal) goal).setUser(this);
                    }
                    goal.isGoalAchieved(latestData);
                }
            }

            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewHealthReport() {
        HealthReport report = generateHealthReport();
        if (report != null) {
            System.out.println(report.getReportContent());
        } else {
            System.out.println("Unable to generate health report.");
        }
    }

    private boolean saveHealthReport(HealthReport report) {
        String sql = "INSERT INTO health_reports (report_id, user_id, report_date, report_content) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, report.getReportID());
            pstmt.setString(2, this.userID);
            pstmt.setTimestamp(3, new java.sql.Timestamp(report.getReportDate().getTime()));
            pstmt.setString(4, report.getReportContent());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGoalStatus(HealthGoalInterface goal) {
        String sql = "UPDATE health_goals SET status = ? WHERE goal_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Updating goal status:");
            System.out.println("Goal ID: " + goal.getGoalID());
            System.out.println("User ID: " + this.userID);
            System.out.println("New Status: " + goal.getStatus());

            pstmt.setString(1, goal.getStatus());
            pstmt.setString(2, goal.getGoalID());
            pstmt.setString(3, this.userID);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating goal status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}