package com.healthmanagement;

import java.util.UUID;

public class HealthData {
    // 屬性
    private String dataID;
    private String userID;
    private Double heartRate;
    private String bloodPressure; // 可能是類似 "120/80" 的格式
    private Double bodyTemperature;
    private Double weight;
    private int steps;

    // 建構子
    public HealthData(String userID, Double heartRate, String bloodPressure,
            Double bodyTemperature, Double weight, int steps) {
        this.dataID = generateDataID();
        this.userID = userID;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.bodyTemperature = bodyTemperature;
        this.weight = weight;
        this.steps = steps;
    }

    // Getters and Setters
    public String getDataID() {
        return dataID;
    }

    public String getUserID() {
        return userID;
    }

    public Double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Double heartRate) {
        this.heartRate = heartRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Double getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(Double bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
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

    // 數據驗證方法
    public boolean validateData() {
        return validateHeartRate() &&
                validateBloodPressure() &&
                validateBodyTemperature() &&
                validateWeight() &&
                validateSteps();
    }

    // 個別數據驗證方法
    private boolean validateHeartRate() {
        return heartRate != null && heartRate >= 40 && heartRate <= 200;
    }

    private boolean validateBloodPressure() {
        if (bloodPressure == null || !bloodPressure.contains("/")) {
            return false;
        }
        try {
            String[] parts = bloodPressure.split("/");
            int systolic = Integer.parseInt(parts[0]);
            int diastolic = Integer.parseInt(parts[1]);
            return systolic >= 70 && systolic <= 190 &&
                    diastolic >= 40 && diastolic <= 130;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateBodyTemperature() {
        return bodyTemperature != null &&
                bodyTemperature >= 35.0 &&
                bodyTemperature <= 42.0;
    }

    private boolean validateWeight() {
        return weight != null && weight > 0 && weight <= 500;
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
                ", heartRate=" + heartRate +
                ", bloodPressure='" + bloodPressure + '\'' +
                ", bodyTemperature=" + bodyTemperature +
                ", weight=" + weight +
                ", steps=" + steps +
                '}';
    }
}