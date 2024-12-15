package com.healthmanagement.observer;

import com.healthmanagement.goals.HealthGoalInterface;

public interface GoalObserver {
    void onGoalAchieved(HealthGoalInterface goal);
}