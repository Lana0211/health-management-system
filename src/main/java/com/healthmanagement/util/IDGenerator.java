/*
組員: 409261536 資工四甲 詹依燃, 410262169 資工四乙 李若榛
*/

package com.healthmanagement.util;

public class IDGenerator {
    private static long counter = 0;

    public static String generateID(String prefix) {
        counter++;
        return prefix + "_" + System.currentTimeMillis() + "_" + counter;
    }
}