package org.example.April;

import java.util.Collections;
import java.util.PriorityQueue;

//Complexity
//Operation   Time    Space
//addNumber() O(log n)    —
//getMedian() O(1)        —
//Overall     O(n log n)  O(n)


public class Median_In_Stream_Of_Int {

    // Max-heap: stores the smaller half of numbers
    private static PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    // Min-heap: stores the larger half of numbers
    private static PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    public static void addNumber(int num) {
        // Step 1: Add to appropriate heap
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.offer(num);
        } else {
            minHeap.offer(num);
        }
        // Step 2: Balance heaps so sizes differ by at most 1
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public static double getMedian() {
        if (maxHeap.size() == minHeap.size()) {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
        return maxHeap.peek(); // maxHeap always has the extra element
    }

    public static void printMedian(int arr[]) {
        for (int i = 0; i < arr.length; i++) {
            addNumber(arr[i]);
            System.out.println("After reading " + (i + 1) + " element(s) -> Median: " + getMedian());
        }
    }

    public static void main(String[] args) {
        int arr[] = { 5, 15, 1, 3, 2, 8, 7, 9, 10, 6, 11, 4 };
        printMedian(arr);
    }
}