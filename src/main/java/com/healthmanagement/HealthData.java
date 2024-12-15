/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/
package com.healthmanagement;

import java.time.LocalDateTime;
import java.util.UUID;

public class HealthData {
    // 屬性
    private String dataID;
    private String userID;
    private Double weight;
    private int steps;
    private LocalDateTime recordedAt;

    // 建構子
    public HealthData(String userID, Double weight, int steps) {
        this.dataID = generateDataID();
        this.userID = userID;
        this.weight = weight;
        this.steps = steps;
        this.recordedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getDataID() {
        return dataID;
    }

    public String getUserID() {
        return userID;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    // 數據驗證方法
    public boolean validateData() {
        return validateWeight() && validateSteps();
    }

    private boolean validateWeight() {
        return weight != null && weight > 0 && weight <= 200;
    }

    private boolean validateSteps() {
        return steps >= 0 && steps <= 100000;
    }

    // 輔助方法
    private String generateDataID() {
        return "DATA_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public String toString() {
        return "HealthData{" +
                "dataID='" + dataID + '\'' +
                ", userID='" + userID + '\'' +
                ", weight=" + weight +
                ", steps=" + steps +
                ", recordedAt=" + recordedAt +
                '}';
    }
}