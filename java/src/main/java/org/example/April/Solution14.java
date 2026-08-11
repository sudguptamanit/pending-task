package org.example.April;

import java.util.Collections;

public class Solution14 {
    /**
     * public static String reverseStr( String str )
     * Example: reverseStr(str) where str is "abcd" returns "dcba".
     *
     * ⚡ Complexity
     * Time: O(n)
     * Space: O(n) (char array)
     *
     */
    public static String reverseStr(String str) {
        if (str == null || str.length() <= 1) {
            return str;
        }

        char[] arr = str.toCharArray();
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;

            left++;
            right--;
        }

        return new String(arr);
    }

    public static void main(String[] args){

        String testString;
        String solution;
        boolean result = true;

        result = result && reverseStr("abcd").equals("dcba");

        if(result){
            System.out.println("All tests pass");
        }
        else{
            System.out.println("There are test failures");
        }

    }
}