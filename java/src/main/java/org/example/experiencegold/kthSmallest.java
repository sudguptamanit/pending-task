package org.example.experiencegold;

import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

public class kthSmallest {
//    Using Sorting - O(n log(n)) Time and O(1) Space
    static int kthSmallest1(int[] arr, int k) {

        // Sort the given array
        Arrays.sort(arr);

        // Return k'th element in the sorted array
        return arr[k - 1];
    }

//    [Expected Approach] Using Max-Heap - O(n * log(k)) Time and O(k) Space

    static int kthSmallest(int[] arr, int k)
    {
        // Create a max heap
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        // Iterate through the array elements
        for (int val : arr)
        {
            // Push the current element onto the max heap
            pq.add(val);

            // If the size of the max heap exceeds k,
            // remove the largest element
            if (pq.size() > k)
                pq.poll();
        }

        // Return the kth smallest element (top of the max heap)
        return pq.peek();
    }

}
