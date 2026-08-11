package org.example.code1;

//⏱️ Time Complexity
//O(log exp) → exponent halves each iteration
//🧠 Space Complexity
//O(1) → iterative (no recursion stack)
//

public class PowerFuction {

    public static double power(double base, int exp) {
        // Handle negative exponent
        if (exp < 0) {
            base = 1 / base;
            exp = -exp;
        }

        double result = 1.0;

        while (exp > 0) {
            // If exponent is odd
            if ((exp & 1) == 1) {
                result *= base;
            }

            base *= base;   // square the base
            exp >>= 1;      // divide exponent by 2
        }

        return result;
    }

    /* returns true if all tests pass, false otherwise */
    public static boolean doTestsPass() {
        boolean doTestsPass = true;

        doTestsPass &= (power(2, 2) == 4);
        doTestsPass &= (power(2, 3) == 8);
        doTestsPass &= (power(2, -2) == 0.25);
        doTestsPass &= (power(5, 0) == 1);

        return doTestsPass;
    }

    public static void main(String[] args) {
        if (doTestsPass())
            System.out.println("All Tests Pass");
        else
            System.out.println("There are test failures");
    }
}