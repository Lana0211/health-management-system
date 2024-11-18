package com.healthmanagement.util;

public class IDGenerator {
    private static long counter = 0;

    public static String generateID(String prefix) {
        counter++;
        return prefix + "_" + System.currentTimeMillis() + "_" + counter;
    }
}