package org.example;

import java.util.*;

//| Metric | Value          |
//        | ------ | -------------- |
//        | Time   | **O(n log n)** |
//        | Space  | **O(n)**       |


public class LargestNumber {

    public static String formLargestNumber(int[] nums) {
        // Convert integers to strings
        String[] arr = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            arr[i] = String.valueOf(nums[i]);
        }

        // Custom sort
        Arrays.sort(arr, (a, b) -> (b + a).compareTo(a + b));

        // Edge case: all zeros
        if (arr[0].equals("0")) return "0";

        // Build result
        StringBuilder result = new StringBuilder();
        for (String s : arr) {
            result.append(s);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        int[] nums = {1, 34, 3, 98, 9, 76, 45, 4};
        System.out.println(formLargestNumber(nums)); // 998764543431
    }
}