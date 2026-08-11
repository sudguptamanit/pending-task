package org.example.code1;

//⏱️ Time Complexity
//O(n) → We traverse the arrays once.
//🧠 Space Complexity
//O(1) → No extra space used (just a variable for result).

public class Solution {

    /**
     * Given two arrays of integers, returns the dot product of the arrays
     */
    public static int dotProduct(int[] array1, int[] array2) {
        // Edge case: null or unequal length
        if (array1 == null || array2 == null || array1.length != array2.length) {
            throw new IllegalArgumentException("Arrays must be non-null and of equal length");
        }

        int result = 0;

        for (int i = 0; i < array1.length; i++) {
            result += array1[i] * array2[i];
        }

        return result;
    }

    public static void main(String[] args) {
        int[] array1 = {1, 2};
        int[] array2 = {2, 3};

        int result = dotProduct(array1, array2);

        if (result == 8) {
            System.out.println("Passed.");
        } else {
            System.out.println("Failed.");
        }
    }
}