package org.example.code1;

import java.util.*;

//⏱️ Time Complexity
//O(n) → single traversal
//🧠 Space Complexity
//O(1) → constant extra space

public class LongestUnique {

/**
 *
 *  e.g.
 *      for the input: "abbbccda" the longest uniform substring is "bbb" (which starts at index 1 and is 3 characters long).
 */

        static int[] longestUniformSubstring(String input) {
            if (input == null || input.length() == 0) {
                return new int[]{-1, 0};
            }

            int longestStart = 0;
            int longestLength = 1;

            int currentStart = 0;
            int currentLength = 1;

            for (int i = 1; i < input.length(); i++) {
                if (input.charAt(i) == input.charAt(i - 1)) {
                    currentLength++;
                } else {
                    // Reset current run
                    currentStart = i;
                    currentLength = 1;
                }

                // Update longest
                if (currentLength > longestLength) {
                    longestLength = currentLength;
                    longestStart = currentStart;
                }
            }

            return new int[]{longestStart, longestLength};
        }

        public static void main(String[] args) {
            Map<String, int[]> testCases = new HashMap<>();
            testCases.put("", new int[]{-1, 0});
            testCases.put("10000111", new int[]{1, 4});
            testCases.put("aabbbbbCdAA", new int[]{2, 5});

            boolean pass = true;
            for (Map.Entry<String, int[]> testCase : testCases.entrySet()) {
                int[] result = longestUniformSubstring(testCase.getKey());
                pass = pass && Arrays.equals(result, testCase.getValue());
            }

            if (pass) {
                System.out.println("Pass!");
            } else {
                System.out.println("Failed!");
            }
        }

}