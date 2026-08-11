package org.example.experiencegold;

//Question 1-
//To find the n numbers which is generated from the sorted array and print the elements in sorted manner.
//        Input:
//arr = {2, 4, 5}, n = 7
//Output:
//        [2, 4, 5, 24, 25, 42, 45, 245]
//
//Input:
//arr = {1, 2, 3, 4}, n = 15
//Output:
//        [1, 2, 3, 4, 12, 13, 14, 21, 23, 24, 31, 32, 34, 41, 42]
//

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//⏱️ Time Complexity
//Each number generation: up to O(k) (string operations)
//Total:
//O(n * k)
//
//Where k = number of digits in generated number
//
//🧠 Space Complexity
//Queue stores up to O(n) elements
//
public class SortedArray {

    public static List<Integer> generateNumbers(int[] arr, int n) {
        List<Integer> result = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();

        // Step 1: initialize queue with single digits
        for (int num : arr) {
            queue.offer(String.valueOf(num));
        }

        // Step 2: BFS generation
        while (!queue.isEmpty() && result.size() <= n) {
            String current = queue.poll();
            result.add(Integer.parseInt(current));

            // Generate next numbers
            for (int num : arr) {
                String digit = String.valueOf(num);

                // Avoid repetition of digit in same number
                if (!current.contains(digit)) {
                    queue.offer(current + digit);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr1 = {2, 4, 5};
        int[] arr2 = {1, 2, 3, 4};

        System.out.println(generateNumbers(arr1, 7));
        System.out.println(generateNumbers(arr2, 15));
    }
}