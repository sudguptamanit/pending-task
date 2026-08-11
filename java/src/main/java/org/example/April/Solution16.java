package org.example.April;

public class Solution16 {

    /**
     *
     * You have an integer array.
     * Starting from arr[startIndex], follow each element to the index it points to.
     * Continue to do this until you find a cycle.
     * Return the length of the cycle. If no cycle is found return -1
     * ⚡ Complexity
     * Time: O(n)
     * Space: O(1) 🔥 (no extra memory)
     *
     */
    public static int countLengthOfCycle(int[] arr, int startIndex) {
        if (arr == null || arr.length == 0) return -1;
        int slow = startIndex;
        int fast = startIndex;
        // Step 1: Detect cycle
        while (true) {
            if (fast < 0 || fast >= arr.length) return -1;
            fast = arr[fast];

            if (fast < 0 || fast >= arr.length) return -1;
            fast = arr[fast];

            slow = arr[slow];
            if (slow == fast) break; // cycle detected
        }
        // Step 2: Find cycle length
        int length = 1;
        int current = arr[slow];
        while (current != slow) {
            current = arr[current];
            length++;
        }
        return length;
    }


    public static void main( String[] args ) {

        boolean testsPassed = true;

        testsPassed &= countLengthOfCycle(new int[]{1, 0}, 0) == 2;
        testsPassed &= countLengthOfCycle(new int[]{1, 2, 0}, 0) == 3;//0 → 1 → 2 → 0 (cycle)

        if(testsPassed) {
            System.out.println( "Test passed." );
            //return true;
        } else {
            System.out.println( "Test failed." );
            //return false;
        }


    }
}