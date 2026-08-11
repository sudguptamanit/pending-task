package org.example;

import java.util.*;

//| Metric | Value          |
//        | ------ | -------------- |
//        | Time   | **O(n log n)** |
//        | Space  | **O(n)**       |


public class SortStringDesc {

    public static String sortDescending(String str) {
        if (str == null || str.length() <= 1) return str;

        char[] arr = str.toCharArray();

        // Step 1: Sort ascending
        Arrays.sort(arr);

        // Step 2: Reverse to get descending
        int left = 0, right = arr.length - 1;
        while (left < right) {
            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;

            left++;
            right--;
        }

        return new String(arr);
    }

    public static void main(String[] args) {
        String input = "mupursingh";
        System.out.println(sortDescending(input)); // uusrpnmihg
    }
}