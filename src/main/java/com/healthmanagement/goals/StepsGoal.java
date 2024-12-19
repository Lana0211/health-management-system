/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/

package com.healthmanagement.goals;

import com.healthmanagement.HealthData;
import com.healthmanagement.User;

public class StepsGoal extends AbstractHealthGoal {

    public StepsGoal(User user) {
        super(user);
    }

    @Override
    public String getGoalType() {
        return "STEPS";
    }

    @Override
    public boolean isGoalAchieved(HealthData latestData) {
        if (!validateGoalParameters() || isExpired()) {
            System.out.println("Goal validation failed or expired");
            return false;
        }

        boolean achieved = latestData.getSteps() >= targetValue;
        System.out.println("Steps goal check: current=" + latestData.getSteps() +
                ", target=" + targetValue + ", achieved=" + achieved);

        if (achieved) {
            setStatus("ACHIEVED");
            goalNotifier.notifyGoalAchieved(this);
            if (user != null) {
                System.out.println("Updating goal status in database");
                user.updateGoalStatus(this);
            } else {
                System.out.println("User object is null, cannot update database");
            }
        }
        return achieved;
    }
}