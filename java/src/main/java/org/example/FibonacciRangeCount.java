package org.example;

//| Metric | Value          |
//        | ------ | -------------- |
//        | Time   | ⭐ **O(log n)** |
//        | Space  | ⭐ **O(1)**     |

public class FibonacciRangeCount {

    static final double PHI = (1 + Math.sqrt(5)) / 2;
    static final double SQRT5 = Math.sqrt(5);

    // log base phi
    static double logPhi(double x) {
        return Math.log(x) / Math.log(PHI);
    }

    public static int countFibonacciInRange(long L, long R) {
        if (L > R) return 0;

        // Find approximate indices
        int lowIndex = (int) Math.ceil(logPhi(L * SQRT5));
        int highIndex = (int) Math.floor(logPhi(R * SQRT5));

        int count = highIndex - lowIndex + 1;

        return Math.max(count, 0);
    }

    public static void main(String[] args) {
        long L = 10;
        long R = 100;

        System.out.println(countFibonacciInRange(L, R)); // Output: 5
    }
}
