package com.healthmanagement;

import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class Main {
    private static Scanner scanner;
    private static User currentUser;
    private static SimpleDateFormat dateFormat;

    public static void main(String[] args) {
        initialize();
        showWelcomeMessage();

        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void initialize() {
        scanner = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    private static void showWelcomeMessage() {
        System.out.println("=================================");
        System.out.println("Welcome to Health Management System");
        System.out.println("=================================");
    }

    private static void showLoginMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Please select an option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleLogin();
                break;
            case "2":
                handleRegistration();
                break;
            case "3":
                System.out.println("Thank you for using our system. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void showMainMenu() {
        System.out.println("\nWelcome, " + currentUser.getName());
        System.out.println("1. Upload Health Data");
        System.out.println("2. Set Health Goal");
        System.out.println("3. View Health Report");
        System.out.println("4. Logout");
        System.out.print("Please select an option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleHealthDataUpload();
                break;
            case "2":
                handleHealthGoalSetting();
                break;
            case "3":
                handleHealthReport();
                break;
            case "4":
                handleLogout();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = User.login(username, password);
        if (currentUser != null) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void handleRegistration() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Gender (M/F): ");
        String gender = scanner.nextLine();

        User newUser = new User(username, password, name, age, gender);
        if (newUser.register()) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("Registration failed. Username might already exist.");
        }
    }

    private static void handleHealthDataUpload() {
        try {
            System.out.print("Heart Rate (40-200 bpm): ");
            Double heartRate = Double.parseDouble(scanner.nextLine());

            System.out.print("Blood Pressure (e.g., 120/80): ");
            String bloodPressure = scanner.nextLine();

            System.out.print("Body Temperature (35-42 °C): ");
            Double bodyTemperature = Double.parseDouble(scanner.nextLine());

            System.out.print("Weight (kg): ");
            Double weight = Double.parseDouble(scanner.nextLine());

            System.out.print("Steps: ");
            int steps = Integer.parseInt(scanner.nextLine());

            HealthData data = new HealthData(
                    currentUser.getUserID(),
                    heartRate,
                    bloodPressure,
                    bodyTemperature,
                    weight,
                    steps);

            if (currentUser.uploadHealthData(data)) {
                System.out.println("Health data uploaded successfully!");
            } else {
                System.out.println("Failed to upload health data.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format.");
        }
    }

    private static void handleHealthGoalSetting() {
        try {
            System.out.println("Available goal types: STEPS, WEIGHT, HEART_RATE");
            System.out.print("Enter goal type: ");
            String goalType = scanner.nextLine().toUpperCase();

            System.out.print("Enter target value: ");
            Double targetValue = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter start date (yyyy-MM-dd): ");
            String startDateStr = scanner.nextLine();
            Date startDate = dateFormat.parse(startDateStr);

            System.out.print("Enter end date (yyyy-MM-dd): ");
            String endDateStr = scanner.nextLine();
            Date endDate = dateFormat.parse(endDateStr);

            HealthGoal goal = new HealthGoal(
                    currentUser.getUserID(),
                    goalType,
                    targetValue,
                    startDate,
                    endDate);

            currentUser.setHealthGoal(goal);
        } catch (Exception e) {
            System.out.println("Invalid input format. Please try again.");
        }
    }

    private static void handleHealthReport() {
        currentUser.viewHealthReport();
    }

    private static void handleLogout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
}