package org.example.code1;

//⏱️ Time Complexity
//O(log n) → binary search
//🧠 Space Complexity
//O(1) → constant space
//
public class SmallestNumber
{

    /*
     * public static int FindMin(int a[])
     * Returns the smallest number in array that has been rotated
     * For example - Array {3,4,5,6,1,2} returns 1
     */


        /*
         * Returns the smallest number in a rotated sorted array
         */
        public static int FindMin(int[] a) {
            if (a == null || a.length == 0) {
                throw new IllegalArgumentException("Invalid input");
            }

            int left = 0, right = a.length - 1;

            while (left < right) {
                int mid = left + (right - left) / 2;

                if (a[mid] > a[right]) {
                    // Minimum is in right half
                    left = mid + 1;
                } else {
                    // Minimum is in left half including mid
                    right = mid;
                }
            }

            return a[left];
        }

        public static void main(String args[]) {
            boolean result = true;
            result = result && FindMin(new int[]{3,4,5,6,1,2}) == 1;
            result = result && FindMin(new int[]{2,1}) == 1;
            result = result && FindMin(new int[]{1}) == 1;

            try {
                FindMin(null);
                result = false;
            } catch (Exception e) {
                result = result && true;
            }

            if (result) {
                System.out.println("All tests pass");
            } else {
                System.out.println("There are test failures");
            }
        }

}