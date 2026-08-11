package org.example.experiencegold;

import java.util.HashMap;

public class KthCount {
//    O(n2) time and O(1) auxiliary space:
    public static int firstElement1(int[] arr, int n, int k) {
        // This loop is used for selection
        // of elements
        for (int i = 0; i < n; i++) {
            // Count how many time selected element
            // occurs
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (arr[i] == arr[j]) {
                    count++;
                }
            }

            // Check, if it occurs k times or not
            if (count == k) {
                return arr[i];
            }
        }

        return -1;
    }

    // Driver Code
    public static void main(String[] args) {
        int[] arr = {1, 7, 4, 3, 4, 8, 7};
        int n = arr.length;
        int k = 2;
        System.out.print(firstElement(arr, n, k));
    }

//    Using Hashing - O(n) time and O(n) auxiliary space:
static int firstElement(int arr[], int n, int k) {
    // unordered_map to count
    // occurrences of each element

    HashMap<Integer, Integer> count_map = new HashMap<>();
    for (int i = 0; i < n; i++) {
        int a = 0;
        if(count_map.get(arr[i])!=null){
            a = count_map.get(arr[i]);
        }

        count_map.put(arr[i], a+1);
    }
    //count_map[arr[i]]++;

    for (int i = 0; i < n; i++) // if count of element == k ,then
    // it is the required first element
    {
        if (count_map.get(arr[i]) == k) {
            return arr[i];
        }
    }

    // no element occurs k times
    return -1;
}
}