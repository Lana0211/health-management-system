/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/

package com.healthmanagement.goals;

import com.healthmanagement.HealthData;
import com.healthmanagement.User;

public class WeightGoal extends AbstractHealthGoal {

    public WeightGoal(User user) {
        super(user);
    }

    @Override
    public String getGoalType() {
        return "WEIGHT";
    }

    @Override
    public boolean isGoalAchieved(HealthData latestData) {
        if (!validateGoalParameters() || isExpired()) {
            System.out.println("Weight goal validation failed or expired");
            return false;
        }

        boolean achieved = latestData.getWeight() <= targetValue;
        System.out.println("Weight goal check: current=" + latestData.getWeight() +
                ", target=" + targetValue + ", achieved=" + achieved);

        if (achieved) {
            setStatus("ACHIEVED");
            goalNotifier.notifyGoalAchieved(this);
            if (user != null) {
                System.out.println("Updating weight goal status in database");
                user.updateGoalStatus(this);
            } else {
                System.out.println("User object is null, cannot update weight goal status");
            }
        }
        return achieved;
    }
}