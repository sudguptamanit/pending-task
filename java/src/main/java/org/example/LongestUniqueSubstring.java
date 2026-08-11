package org.example;

import java.util.*;

//| Metric | Value                        |
//        | ------ | ---------------------------- |
//        | Time   | **O(n)**                     |
//        | Space  | **O(k)** (set of characters) |


public class LongestUniqueSubstring {

    public static String longestUniqueSubstring(String s) {
        if (s == null || s.length() == 0) return "";

        Set<Character> set = new HashSet<>();
        int left = 0;

        int maxLen = 0;
        int startIndex = 0;

        for (int right = 0; right < s.length(); right++) {

            // If duplicate found, shrink window
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }

            set.add(s.charAt(right));

            // Update max substring
            if (right - left + 1 > maxLen) {
                maxLen = right - left + 1;
                startIndex = left;
            }
        }

        return s.substring(startIndex, startIndex + maxLen);
    }

    public static void main(String[] args) {
        String input = "aaabcbdeaf";
        System.out.println(longestUniqueSubstring(input)); // cbdeaf
    }
}