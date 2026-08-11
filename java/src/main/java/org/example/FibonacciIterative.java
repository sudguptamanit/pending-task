package org.example;


//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(1)** |

public class FibonacciIterative {

    public static int fib(int n) {
        if (n <= 1) return n;

        int prev = 0, curr = 1;

        for (int i = 2; i <= n; i++) {
            int next = prev + curr;
            prev = curr;
            curr = next;
        }

        return curr;
    }

    public static void main(String[] args) {
        int n = 10;
        System.out.println(fib(n)); // 55
    }
}