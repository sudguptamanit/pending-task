package org.example.experiencegold;

//Question 2- Merge the two sorted array.
//        Input:
//Array1 = [1, 3, 5]
//Array2 = [2, 4, 6]
//Output:
//        [1, 2, 3, 4, 5, 6]
//
//Asked for optimal approach only.
//
//

import java.util.*;
    //⏱️ Time Complexity
    //O(n + m)
    //Each element is processed exactly once
    //🧠 Space Complexity
    //O(n + m)
    //For the result array
    //
    //
public class MergeArray {

    public static int[] mergeSortedArrays(int[] arr1, int[] arr2) {
        int n1 = arr1.length, n2 = arr2.length;
        int[] result = new int[n1 + n2];

        int i = 0, j = 0, k = 0;

        // Merge both arrays
        while (i < n1 && j < n2) {
            if (arr1[i] <= arr2[j]) {
                result[k++] = arr1[i++];
            } else {
                result[k++] = arr2[j++];
            }
        }

        // Copy remaining elements
        while (i < n1) {
            result[k++] = arr1[i++];
        }

        while (j < n2) {
            result[k++] = arr2[j++];
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 3, 5};
        int[] arr2 = {2, 4, 6};

        int[] merged = mergeSortedArrays(arr1, arr2);
        System.out.println(Arrays.toString(merged)); // [1,2,3,4,5,6]
    }
}