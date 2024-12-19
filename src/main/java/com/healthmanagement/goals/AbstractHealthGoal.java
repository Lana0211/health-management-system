/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/
package com.healthmanagement.goals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.healthmanagement.User;
import com.healthmanagement.config.DatabaseConfig;
import com.healthmanagement.observer.GoalNotifier;
import com.healthmanagement.observer.GoalObserver;
import com.healthmanagement.util.IDGenerator;

public abstract class AbstractHealthGoal implements HealthGoalInterface {
    protected String goalID;
    protected User user;
    protected Double targetValue;
    protected Date startDate;
    protected Date endDate;
    protected String status;
    protected GoalNotifier goalNotifier;

    public AbstractHealthGoal(User user) {
        this.goalID = IDGenerator.generateID("GOAL");
        this.user = user;
        this.status = "IN_PROGRESS";
        this.goalNotifier = new GoalNotifier();
    }

    @Override
    public String getGoalID() {
        return goalID;
    }

    @Override
    public String getUserID() {
        return user.getUserID();
    }

    @Override
    public Double getTargetValue() {
        return targetValue;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean isExpired() {
        boolean expired = new Date().after(endDate);
        if (expired && !"EXPIRED".equals(status)) {
            System.out.println("Goal " + goalID + " has expired");
            setStatus("EXPIRED");
            String sql = "UPDATE health_goals SET status = ? WHERE goal_id = ? AND user_id = ?";
            try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setString(2, goalID);
                pstmt.setString(3, user.getUserID());
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Updated expired goal status in database. Rows affected: " + rowsAffected);
            } catch (SQLException e) {
                System.out.println("Failed to update expired goal status: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return expired;
    }

    @Override
    public void addObserver(GoalObserver observer) {
        goalNotifier.addObserver(observer);
    }

    @Override
    public void removeObserver(GoalObserver observer) {
        goalNotifier.removeObserver(observer);
    }

    protected boolean validateGoalParameters() {
        if (targetValue == null || startDate == null || endDate == null) {
            System.out.println("One or more parameters are null");
            return false;
        }

        Date now = new Date();
        if (endDate.before(now)) {
            System.out.println("Goal has expired");
            return false;
        }

        return true;
    }

    public void setUser(User user) {
        this.user = user;
    }
}