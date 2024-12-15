package com.healthmanagement.observer;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.healthmanagement.goals.HealthGoalInterface;

public class LineNotificationObserver implements GoalObserver {
    private static final String LINE_NOTIFY_API_URL = "https://notify-api.line.me/api/notify";
    private static final String ACCESS_TOKEN = "JPRa0KTcjJuouPWrDj3RBPst3Dz7lhztE12QyrOsWGE"; // 替換為您生成的權杖

    @Override
    public void onGoalAchieved(HealthGoalInterface goal) {
        sendLineNotification(goal);
    }

    private void sendLineNotification(HealthGoalInterface goal) {
        try {
            URL url = new URL(LINE_NOTIFY_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String message = "恭喜！您已達成健康目標：" + goal.getGoalType() + "，目標值：" + goal.getTargetValue();
            String postData = "message=" + message;

            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("LINE notification sent successfully.");
            } else {
                System.out.println("Failed to send LINE notification. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}