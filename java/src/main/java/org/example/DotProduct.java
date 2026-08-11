package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(1)** |


public class DotProduct {

    public static int dotProduct(int[] arr1, int[] arr2) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) {
            throw new IllegalArgumentException("Arrays must be non-null and of equal length");
        }

        int result = 0;

        for (int i = 0; i < arr1.length; i++) {
            result += arr1[i] * arr2[i];
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 3, -5};
        int[] arr2 = {4, -2, -1};

        System.out.println(dotProduct(arr1, arr2)); // Output: 3
    }
}