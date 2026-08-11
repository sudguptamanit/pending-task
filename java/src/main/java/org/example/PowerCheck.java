package org.example;

//Value
//Time	O(logₓ n)
//Space	O(1)

public class PowerCheck {

    public static boolean isPower(int n, int x) {
        // Edge cases
        if (n < 1 || x <= 1) {
            return n == 1; // Only 1^k = 1
        }

        // Keep dividing n by x
        while (n % x == 0) {
            n /= x;
        }

        return n == 1;
    }

    public static void main(String[] args) {
        int n = 81;
        int x = 3;

        System.out.println(isPower(n, x)); // true
    }
}