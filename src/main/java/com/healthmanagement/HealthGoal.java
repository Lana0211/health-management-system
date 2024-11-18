/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/
package com.healthmanagement;

import java.util.Date;

import com.healthmanagement.util.IDGenerator;

public class HealthGoal {
    // 屬性
    private String goalID;
    private String userID;
    private String goalType; // 例如："STEPS", "WEIGHT", "HEART_RATE"
    private Double targetValue;
    private Date startDate;
    private Date endDate;
    private String status;

    // 建構子
    public HealthGoal(String userID, String goalType, Double targetValue, Date startDate, Date endDate) {
        this.goalID = IDGenerator.generateID("GOAL");
        this.userID = userID;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "IN_PROGRESS";
    }

    // Getters and Setters
    public String getGoalID() {
        return goalID;
    }

    public String getUserID() {
        return userID;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 目標設定方法
    public void setGoal(String goalType, Double targetValue, Date startDate, Date endDate) {
        if (validateGoalParameters(goalType, targetValue, startDate, endDate)) {
            this.goalType = goalType;
            this.targetValue = targetValue;
            this.startDate = startDate;
            this.endDate = endDate;
        } else {
            throw new IllegalArgumentException("Invalid goal parameters");
        }
    }

    // 檢查目標是否達成
    public boolean isGoalAchieved(HealthData latestData) {
        if (isExpired()) {
            return false;
        }

        switch (goalType.toUpperCase()) {
            case "STEPS":
                return latestData.getSteps() >= targetValue;
            case "WEIGHT":
                // 如果目標是減重，實際體重應該小於等於目標值
                return latestData.getWeight() <= targetValue;
            case "HEART_RATE":
                // 假設目標是將心率控制在目標值以下
                return latestData.getHeartRate() <= targetValue;
            default:
                return false;
        }
    }

    // 驗證方法
    private boolean validateGoalParameters(String goalType, Double targetValue, Date startDate, Date endDate) {
        if (goalType == null || targetValue == null || startDate == null || endDate == null) {
            return false;
        }

        // 檢查日期是否合理
        Date now = new Date();
        if (startDate.before(now) || endDate.before(startDate)) {
            return false;
        }

        // 其他驗證邏輯保持不變
        return validateTargetValue(goalType, targetValue);
    }

    private boolean validateTargetValue(String goalType, Double targetValue) {
        switch (goalType.toUpperCase()) {
            case "STEPS":
                return targetValue >= 0 && targetValue <= 100000;
            case "WEIGHT":
                return targetValue > 0 && targetValue <= 500;
            case "HEART_RATE":
                return targetValue >= 40 && targetValue <= 200;
            default:
                return false;
        }
    }

    // 檢查目標是否過期
    public boolean isExpired() {
        return new Date().after(endDate);
    }

    @Override
    public String toString() {
        return "HealthGoal{" +
                "goalID='" + goalID + '\'' +
                ", userID='" + userID + '\'' +
                ", goalType='" + goalType + '\'' +
                ", targetValue=" + targetValue +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}
