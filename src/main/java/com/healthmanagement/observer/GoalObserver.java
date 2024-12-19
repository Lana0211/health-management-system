/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/

package com.healthmanagement.observer;

import com.healthmanagement.goals.HealthGoalInterface;

public interface GoalObserver {
    void onGoalAchieved(HealthGoalInterface goal);
}