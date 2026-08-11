package org.example.code1;

//⏱️ Time Complexity
//O(n) → Traverse string once
//🧠 Space Complexity
//O(n) → Due to char array (since Java strings are immutable)
//
public class Reverse  {
    /**
     * Example: reverseStr("abcd") -> "dcba"
     */
    public static String reverseStr(String str) {
        // Handle edge cases
        if (str == null || str.length() <= 1) {
            return str;
        }

        char[] arr = str.toCharArray();
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
        boolean result = true;

        result = result && reverseStr("abcd").equals("dcba");

        if (result) {
            System.out.println("All tests pass");
        } else {
            System.out.println("There are test failures");
        }
    }
}