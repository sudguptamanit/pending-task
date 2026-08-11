package org.example.April;

/*
 * Implement a run length encoding function.
 * For a string input the function returns output encoded as follows:
 *
 * "a"     -> "a1"
 * "aa"    -> "a2"
 * "aabbb" -> "a2b3"
 * "aabbbaaabababab"  ->  "a2b3a3b1a1b1a1b1a1b1"
 *
 * ⚡ Complexity
Time: O(n) (single pass)
Space: O(n) (output)
*
 */
public class Solution10 {

    public static String rle(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        char prev = input.charAt(0);
        int count = 1;

        for (int i = 1; i < input.length(); i++) {
            char curr = input.charAt(i);
            if (curr == prev) {
                count++;
            } else {
                result.append(prev).append(count);
                prev = curr;
                count = 1;
            }
        }
        // append last group
        result.append(prev).append(count);
        return result.toString();
    }


    public static void main(String[] args) {

        if ("".equals(rle("")) &&
                "a1".equals(rle("a")) &&
                "a3".equals(rle("aaa"))) {
            System.out.println("Passed");
        } else {
            System.out.println("Failed");
        }
    }
}