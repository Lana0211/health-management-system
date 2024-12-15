package com.healthmanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.healthmanagement.goals.HealthGoalFactory;
import com.healthmanagement.goals.HealthGoalInterface;
import com.healthmanagement.observer.GoalObserver;

public class GoalStatusUpdateTest {
    private User user;
    private HealthGoalInterface stepsGoal;
    private HealthGoalInterface weightGoal;
    private TestGoalObserver observer;

    @Before
    public void setUp() {
        // 使用已存在的用戶帳戶登入
        user = User.login("test", "123");
        assertNotNull("Login failed", user);

        // 設置目標
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1000); // 一週後

        stepsGoal = HealthGoalFactory.createHealthGoal(user, "STEPS", 10000.0, startDate, endDate);
        weightGoal = HealthGoalFactory.createHealthGoal(user, "WEIGHT", 70.0, startDate, endDate);

        // 添加觀察者
        observer = new TestGoalObserver();
        stepsGoal.addObserver(observer);
        weightGoal.addObserver(observer);
    }

    @Test
    public void testStepsGoalAchievement() {
        // 創建達到目標的健康數據
        HealthData data = new HealthData(
                user.getUserID(),
                75.0, // weight
                12000 // steps - 超過目標的 10000
        );

        assertTrue(stepsGoal.isGoalAchieved(data));
        assertEquals("ACHIEVED", stepsGoal.getStatus());
        assertTrue(observer.wasNotified());
    }

    @Test
    public void testWeightGoalAchievement() {
        // 創建達到目標的健康數據
        HealthData data = new HealthData(
                user.getUserID(),
                65.0, // weight - 低於目標的 70.0
                5000 // steps
        );

        assertTrue(weightGoal.isGoalAchieved(data));
        assertEquals("ACHIEVED", weightGoal.getStatus());
        assertTrue(observer.wasNotified());
    }

    // 測試用的觀察者類別
    private class TestGoalObserver implements GoalObserver {
        private boolean notified = false;

        @Override
        public void onGoalAchieved(HealthGoalInterface goal) {
            notified = true;
        }

        public boolean wasNotified() {
            return notified;
        }
    }
}