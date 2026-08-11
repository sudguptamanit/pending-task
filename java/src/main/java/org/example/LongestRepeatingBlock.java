package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(1)** |

public class LongestRepeatingBlock {

    public static int[] findLongestRepeating(String s) {
        if (s == null || s.length() == 0) return new int[]{-1, 0};

        int maxLen = 1;
        int currLen = 1;

        int startIndex = 0;
        int currStart = 0;

        for (int i = 1; i < s.length(); i++) {

            if (s.charAt(i) == s.charAt(i - 1)) {
                currLen++;
            } else {
                // Check if current block is max
                if (currLen > maxLen) {
                    maxLen = currLen;
                    startIndex = currStart;
                }
                currLen = 1;
                currStart = i;
            }
        }

        // Final check for last block
        if (currLen > maxLen) {
            maxLen = currLen;
            startIndex = currStart;
        }

        return new int[]{startIndex, maxLen};
    }

    public static void main(String[] args) {
        String input = "aabbbbddcc";

        int[] result = findLongestRepeating(input);
        System.out.println("[" + result[0] + ", " + result[1] + "]");
        // Output: [2, 4]
    }
}