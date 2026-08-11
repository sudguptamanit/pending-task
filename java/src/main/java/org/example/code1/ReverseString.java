package org.example.code1;

//⏱️ Time Complexity
//O(n) → Each character is visited once
//🧠 Space Complexity
//O(n) → Due to char array (Java strings are immutable)
//
public class ReverseString {
    /**
     * Example: reverseStr("abcd") -> "dcba"
     */
    public static String reverseStr(String str) {
        // Edge case
        if (str == null || str.length() <= 1) {
            return str;
        }

        char[] chars = str.toCharArray();
        int left = 0, right = chars.length - 1;

        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }

        return new String(chars);
    }

    public static void main(String[] args) {
        boolean result = true;

        result = result && reverseStr("abcd").equals("dcba");

        if (result) {
            System.out.println("All tests pass");
        } else {
            System.out.println("There are test failures");
        }
    }
}