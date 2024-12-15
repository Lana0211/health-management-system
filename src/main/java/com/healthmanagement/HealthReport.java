/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/
package com.healthmanagement;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.healthmanagement.goals.HealthGoalInterface;

public class HealthReport {
    // 屬性
    private String reportID;
    private String userID;
    private Date reportDate;
    private String reportContent;

    // 建構子
    public HealthReport(String userID) {
        this.reportID = generateReportID();
        this.userID = userID;
        this.reportDate = new Date();
    }

    public HealthReport(String userID, HealthData healthData, List<HealthGoalInterface> goals) {
        this.reportID = generateReportID();
        this.userID = userID;
        this.reportDate = new Date();
        generateReport(healthData, goals);
    }

    // Getters and Setters
    public String getReportID() {
        return reportID;
    }

    public String getUserID() {
        return userID;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public String getReportContent() {
        return reportContent;
    }

    // 生成報告的方法
    public void generateReport(HealthData latestData, List<HealthGoalInterface> goals) {
        StringBuilder report = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 添加報告標題
        report.append("Health Report\n");
        report.append("Generated on: ").append(dateFormat.format(reportDate)).append("\n");
        report.append("User ID: ").append(userID).append("\n\n");

        // 添加最新健康數據
        report.append("Current Health Status:\n");
        if (latestData != null) {
            report.append("Weight: ").append(latestData.getWeight()).append(" kg\n");
            report.append("Steps: ").append(latestData.getSteps()).append("\n\n");
        } else {
            report.append("No health data available\n\n");
        }

        // 添加健康目標狀態
        report.append("Health Goals:\n");
        if (goals != null && !goals.isEmpty()) {
            // 在生成報告時再次檢查所有目標
            for (HealthGoalInterface goal : goals) {
                if (latestData != null) {
                    goal.isGoalAchieved(latestData);
                }
            }

            Map<String, HealthGoalInterface> latestGoals = goals.stream()
                    .collect(Collectors.groupingBy(
                            HealthGoalInterface::getGoalType,
                            Collectors.collectingAndThen(
                                    Collectors.maxBy(Comparator.comparing(HealthGoalInterface::getStartDate)),
                                    Optional::get)));

            for (HealthGoalInterface goal : latestGoals.values()) {
                report.append("Goal Type: ").append(goal.getGoalType()).append("\n");
                report.append("Target Value: ").append(goal.getTargetValue()).append("\n");
                report.append("Start Date: ").append(dateFormat.format(goal.getStartDate())).append("\n");
                report.append("End Date: ").append(dateFormat.format(goal.getEndDate())).append("\n");
                report.append("Status: ").append(goal.getStatus()).append("\n\n");
            }
        } else {
            report.append("No active health goals\n\n");
        }

        // 添加健康建議
        report.append("Health Recommendations:\n");
        addHealthRecommendations(report, latestData);

        this.reportContent = report.toString();
    }

    // 添加健康建議
    private void addHealthRecommendations(StringBuilder report, HealthData data) {
        if (data == null) {
            report.append("No data available for recommendations\n");
            return;
        }

        // 步數建議
        if (data.getSteps() < 8000) {
            report.append("- Try to increase your daily steps to at least 8,000 steps.\n");
        }

        // BMI相關建議（如果有體重數據）
        if (data.getWeight() != null) {
            report.append("- Maintain a balanced diet and regular exercise routine.\n");
        }

        report.append("\n");
    }

    // 輔助方法
    private String generateReportID() {
        return "REPORT_" + System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "HealthReport{" +
                "reportID='" + reportID + '\'' +
                ", userID='" + userID + '\'' +
                ", reportDate=" + reportDate +
                ", reportContent='" + (reportContent != null ? "Available" : "Not generated") + '\'' +
                '}';
    }
}