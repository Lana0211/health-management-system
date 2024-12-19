/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/

package com.healthmanagement.goals;

import java.util.Date;

import com.healthmanagement.HealthData;
import com.healthmanagement.observer.GoalObserver;

public interface HealthGoalInterface {
    String getGoalID();

    String getUserID();

    String getGoalType();

    Double getTargetValue();

    Date getStartDate();

    Date getEndDate();

    String getStatus();

    void setTargetValue(Double targetValue);

    void setStartDate(Date startDate);

    void setEndDate(Date endDate);

    void setStatus(String status);

    boolean isGoalAchieved(HealthData latestData);

    boolean isExpired();

    void addObserver(GoalObserver observer);

    void removeObserver(GoalObserver observer);
}