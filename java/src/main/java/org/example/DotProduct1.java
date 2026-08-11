package org.example;

//| Metric | Value            |
//        | ------ | ---------------- |
//        | Time   | **O(min(n, m))** |
//        | Space  | **O(1)**         |


public class DotProduct1 {

    public static long dotProduct(int[] arr1, int[] arr2) {
        if (arr1 == null || arr2 == null) {
            throw new IllegalArgumentException("Arrays must not be null");
        }

        int minLength = Math.min(arr1.length, arr2.length);
        long result = 0;

        for (int i = 0; i < minLength; i++) {
            result += (long) arr1[i] * arr2[i];
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 3, -5, 7};
        int[] arr2 = {4, -2, -1};

        System.out.println(dotProduct(arr1, arr2)); // Output: 3
    }
}