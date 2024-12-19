/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/

package com.healthmanagement.goals;

import java.util.Date;

import com.healthmanagement.User;

public class HealthGoalFactory {
    public static HealthGoalInterface createHealthGoal(User user, String goalType,
            Double targetValue, Date startDate, Date endDate) {
        AbstractHealthGoal goal;

        switch (goalType.toUpperCase()) {
            case "STEPS":
                goal = new StepsGoal(user);
                break;
            case "WEIGHT":
                goal = new WeightGoal(user);
                break;
            default:
                throw new IllegalArgumentException("Invalid goal type: " + goalType);
        }

        goal.setTargetValue(targetValue);
        goal.setStartDate(startDate);
        goal.setEndDate(endDate);

        return goal;
    }
}