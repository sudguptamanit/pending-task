package org.example.experiencegold;

import java.util.Arrays;

public class ArrangeArray {
//⏱️ Time Complexity
//    O(n + k)
//    n = array size
//    k = range (max - min)
//🧠 Space Complexity
//    O(k)

    public static void groupAndSort(int[] arr) {
        if (arr == null || arr.length == 0) return;

        // Step 1: Find min and max
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int num : arr) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        // Step 2: Frequency array
        int[] freq = new int[max - min + 1];

        for (int num : arr) {
            freq[num - min]++;
        }

        // Step 3: Rebuild array
        int index = 0;
        for (int i = 0; i < freq.length; i++) {
            while (freq[i]-- > 0) {
                arr[index++] = i + min;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {4, 2, 2, 8, 3, 3, 1};

        groupAndSort(arr);

        System.out.println(Arrays.toString(arr));
        // Output: [1, 2, 2, 3, 3, 4, 8]
    }
}