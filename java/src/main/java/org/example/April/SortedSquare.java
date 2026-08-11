package org.example.April;

import java.util.Arrays;

//
//Complexity
//                        Reason
//Time    O(N)        Single pass, each element visited exactly once
//Space   O(N)        Output array only — no extra working space


public class SortedSquare {

    public static int[] sortedSquares(int[] arr) {
        int[] result = new int[arr.length];

        int left = 0;
        int right = arr.length - 1;
        int pos = arr.length - 1;   // fill result from the back (largest first)

        while (left <= right) {
            int leftSq  = arr[left]  * arr[left];
            int rightSq = arr[right] * arr[right];

            if (leftSq > rightSq) {
                result[pos--] = leftSq;
                left++;
            } else {
                result[pos--] = rightSq;
                right--;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr = {-3, -2, -1, 1, 2, 4, 6};
        System.out.println(Arrays.toString(sortedSquares(arr)));
        // Output: [1, 1, 4, 4, 9, 16, 36]
    }
}