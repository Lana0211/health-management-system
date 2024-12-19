/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/

package com.healthmanagement.observer;

import java.util.ArrayList;
import java.util.List;

import com.healthmanagement.goals.HealthGoalInterface;

public class GoalNotifier {
    private List<GoalObserver> observers = new ArrayList<>();

    public void addObserver(GoalObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GoalObserver observer) {
        observers.remove(observer);
    }

    public void notifyGoalAchieved(HealthGoalInterface goal) { // 修改這裡
        for (GoalObserver observer : observers) {
            observer.onGoalAchieved(goal);
        }
    }
}