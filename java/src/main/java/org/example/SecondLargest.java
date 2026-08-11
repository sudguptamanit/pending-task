package org.example;

//| Metric | Value      |
//        | ------ | ---------- |
//        | Time   | ⭐ **O(n)** |
//        | Space  | **O(1)**   |
//

public class SecondLargest {

    public static int findSecondLargest(int[] arr) {
        if (arr == null || arr.length < 2) {
            throw new IllegalArgumentException("Array must have at least 2 elements");
        }

        int first = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;

        for (int num : arr) {
            if (num > first) {
                second = first;
                first = num;
            } else if (num > second && num != first) {
                second = num;
            }
        }

        if (second == Integer.MIN_VALUE) {
            throw new RuntimeException("No second largest element (all elements may be equal)");
        }

        return second;
    }

    public static void main(String[] args) {
        int[] arr = {12, 35, 1, 10, 34, 1};

        System.out.println(findSecondLargest(arr)); // Output: 34
    }
}