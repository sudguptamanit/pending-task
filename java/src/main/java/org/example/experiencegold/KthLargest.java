package org.example.experiencegold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

public class KthLargest {

//    Time complexity: O(n * log n)
//    Auxiliary Space: O(1)
//
    static ArrayList<Integer> kLargest(int[] arr, int k) {
        int n = arr.length;

        // Convert int type to Integer
        // for sorting with a comparator
        Integer[] arrInteger =
                Arrays.stream(arr).boxed().toArray(Integer[]::new);

        // Sort the array in descending order
        Arrays.sort(arrInteger, Collections.reverseOrder());

        // Store the first k elements in result list
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < k; i++)
            res.add(arrInteger[i]);

        return res;
    }



//    Time Complexity: O(n * log k), this solution can work in O(k + (n-k) Log K) as build heap take linear time.
//    Auxiliary Space: O(k)
//
    static ArrayList<Integer> kLargest1(int[] arr, int k) {

        // Min-heap to store the k largest elements
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);

        // Add first k elements to the heap
        for (int i = 0; i < k; i++) {
            minHeap.add(arr[i]);
        }

        // Traverse the rest of the array
        for (int i = k; i < arr.length; i++) {

            // If current element is larger than
            // the smallest in heap
            if (arr[i] > minHeap.peek()) {
                minHeap.poll();
                minHeap.add(arr[i]);
            }
        }

        // Extract elements from the heap
        ArrayList<Integer> res = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            res.add(minHeap.poll());
        }

        // Reverse the list for descending order
        Collections.reverse(res);
        return res;
    }
    public static void main(String[] args) {
        int[] arr = {1, 23, 12, 9, 30, 2, 50};
        int k = 3;

        ArrayList<Integer> res = kLargest(arr, k);
        for (int ele : res) {
            System.out.print(ele + " ");
        }
    }
}
