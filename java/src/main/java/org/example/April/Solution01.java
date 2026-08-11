package org.example.April;

/**
 *
 *  e.g.
 *      for the input: "abbbccda" the longest uniform substring is "bbb" (which starts at index 1 and is 3 characters long).
 *
 *      💡 Why this is optimal
 * Time Complexity: O(n) (single traversal)
 * Space Complexity: O(1) (no extra data structures)
 * Avoids substring creation → no unnecessary memory overhead
 */

import java.util.*;

public class Solution01 {

    private static final Map<String, int[]> testCases = new HashMap<String, int[]>();

    static int[] longestUniformSubstring(String input){
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
                // Reset for new character sequence
                currentStart = i;
                currentLength = 1;
            }
            // Update longest if needed
            if (currentLength > longestLength) {
                longestLength = currentLength;
                longestStart = currentStart;
            }
        }
        return new int[]{longestStart, longestLength};
    }

    public static void main(String[] args) {
        testCases.put("", new int[]{-1, 0});
        testCases.put("10000111", new int[]{1, 4});
        testCases.put("aabbbbbCdAA", new int[]{2, 5});

        boolean pass = true;
        for(Map.Entry<String,int[]> testCase : testCases.entrySet()){
            int[] result = longestUniformSubstring(testCase.getKey());
            pass = pass && (Arrays.equals(result, testCase.getValue()));
        }
        if(pass){
            System.out.println("Pass!");
        } else {
            System.out.println("Failed! ");
        }
    }
}