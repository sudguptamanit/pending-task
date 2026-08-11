package org.example;

import java.util.*;

//| Operation            | Time         | Space |
//        | -------------------- | ------------ | ----- |
//        | Mean                 | O(n)         | O(1)  |
//        | Median (QuickSelect) | **O(n)** avg | O(1)  |
//        | Overall              | ⭐ **O(n)**   | O(1)  |


public class MeanMedian {

    public static double findMean(int[] arr) {
        long sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return (double) sum / arr.length;
    }

    // QuickSelect to find kth smallest element
    public static int quickSelect(int[] arr, int left, int right, int k) {
        if (left == right) return arr[left];

        int pivot = partition(arr, left, right);

        if (k == pivot) return arr[k];
        else if (k < pivot) return quickSelect(arr, left, pivot - 1, k);
        else return quickSelect(arr, pivot + 1, right, k);
    }

    private static int partition(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left;

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }

        swap(arr, i, right);
        return i;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static double findMedian(int[] arr) {
        int n = arr.length;
        int[] copy = Arrays.copyOf(arr, n); // avoid modifying original

        if (n % 2 == 1) {
            return quickSelect(copy, 0, n - 1, n / 2);
        } else {
            int leftMid = quickSelect(copy, 0, n - 1, n / 2 - 1);
            int rightMid = quickSelect(copy, 0, n - 1, n / 2);
            return (leftMid + rightMid) / 2.0;
        }
    }

    public static void main(String[] args) {
        int[] arr = {7, 1, 3, 4, 5, 6};

        System.out.println("Mean: " + findMean(arr));
        System.out.println("Median: " + findMedian(arr));
    }
}