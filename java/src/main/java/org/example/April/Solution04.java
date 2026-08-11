package org.example.April;

public class Solution04 {
    /**
     * int secondSmallest(int[] x)
     *
     * ⚡ Complexity
     * Time: O(n) (single pass)
     * Space: O(1) (no extra structures)
     *
     */
    public static int secondSmallest(int[] x) {
        if (x == null || x.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }
        // If only one element, return that element (as per test case)
        if (x.length == 1) {
            return x[0];
        }
        int min = Integer.MAX_VALUE;
        int secondMin = Integer.MAX_VALUE;

        for (int num : x) {
            if (num < min) {
                secondMin = min;
                min = num;
            } else if (num < secondMin) {
                secondMin = num;
            }
        }
        return secondMin;
    }

    public static void main(String args[]) {

        int[] a = { 0 };
        int[] b = { 0, 1 };

        boolean result = true;
        result &= secondSmallest(a) == 0;
        result &= secondSmallest(b) == 1;

        if (result) {
            System.out.println("Pass");
        } else {
            System.out.println("Fail");
        }

    }
}