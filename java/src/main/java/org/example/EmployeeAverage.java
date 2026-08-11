package org.example;

import java.util.*;


//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |



public class EmployeeAverage {

    public static int maxAverage(String[][] data) {
        Map<String, Integer> sumMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        // Step 1: Aggregate sum and count
        for (String[] entry : data) {
            String name = entry[0];
            int marks = Integer.parseInt(entry[1]);

            sumMap.put(name, sumMap.getOrDefault(name, 0) + marks);
            countMap.put(name, countMap.getOrDefault(name, 0) + 1);
        }

        // Step 2: Find max average
        int maxAvg = Integer.MIN_VALUE;

        for (String name : sumMap.keySet()) {
            int sum = sumMap.get(name);
            int count = countMap.get(name);

            int avg = sum / count; // integer division
            maxAvg = Math.max(maxAvg, avg);
        }

        return maxAvg;
    }

    public static void main(String[] args) {
        String[][] data = {
                {"Alia", "-678"},
                {"Bobby", "100"},
                {"Alex", "223"},
                {"Alex", "-23"},
                {"Bobby", "723"}
        };

        System.out.println(maxAverage(data)); // 315
    }
}