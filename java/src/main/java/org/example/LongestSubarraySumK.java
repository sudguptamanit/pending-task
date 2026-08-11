package org.example;

import java.util.*;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |


public class LongestSubarraySumK {

    public static int longestSubarray(int[] arr, int k) {
        Map<Integer, Integer> map = new HashMap<>();

        int sum = 0;
        int maxLen = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];

            // Case 1: sum itself equals k
            if (sum == k) {
                maxLen = i + 1;
            }

            // Case 2: (sum - k) seen before
            if (map.containsKey(sum - k)) {
                int len = i - map.get(sum - k);
                maxLen = Math.max(maxLen, len);
            }

            // Store first occurrence only
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }

        return maxLen;
    }

    public static void main(String[] args) {
        int[] arr = {10, 5, 2, 7, 1, 9};
        int k = 15;

        System.out.println(longestSubarray(arr, k)); // Output: 4
    }
}