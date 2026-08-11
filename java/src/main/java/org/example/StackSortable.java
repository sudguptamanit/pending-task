package org.example;

import java.util.Stack;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |


public class StackSortable {

    public static boolean isStackSortable(int[] arr) {
        Stack<Integer> stack = new Stack<>();
        int expected = 1;
        int i = 0;
        int n = arr.length;

        while (i < n) {
            // Push current element
            stack.push(arr[i]);

            // Pop while top matches expected
            while (!stack.isEmpty() && stack.peek() == expected) {
                stack.pop();
                expected++;
            }

            i++;
        }

        // Final check
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        int[] arr1 = {4, 1, 3, 2};  // Not stack sortable
        int[] arr2 = {3, 1, 2, 4};  // Stack sortable

        System.out.println(isStackSortable(arr1)); // false
        System.out.println(isStackSortable(arr2)); // true
    }
}
