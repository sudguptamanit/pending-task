package org.example.code1;

//⏱️ Time Complexity
//O(n) → single traversal
//🧠 Space Complexity
//O(1) → constant space
//
public class SecondSmallest {
    /**
     * int secondSmallest(int[] x)
     */
    public static int secondSmallest(int[] x) {
        if (x == null || x.length == 0) return 0;
        if (x.length == 1) return x[0];

        int smallest = Integer.MAX_VALUE;
        int secondSmallest = Integer.MAX_VALUE;

        for (int num : x) {
            if (num < smallest) {
                secondSmallest = smallest;
                smallest = num;
            } else if (num < secondSmallest) {
                secondSmallest = num;
            }
        }

        // If no valid second smallest found
        return (secondSmallest == Integer.MAX_VALUE) ? smallest : secondSmallest;
    }

    public static void main(String args[]) {

        int[] a = {0};
        int[] b = {0, 1};

        boolean result = true;
        result &= secondSmallest(a) == 0;
        result &= secondSmallest(b) == 1;

        if (result) {
            System.out.println("Pass");
        } else {
            System.out.println("Fail");
        }
    }
}