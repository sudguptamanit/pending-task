package org.example.code1;

//⏱️ Time Complexity
//O(log n) → exponent is halved each step
//🧠 Space Complexity
//O(1) → iterative approach (no recursion stack)
//

public class Power  {

    /* Given base and integer exponent, compute base^exp */
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

    public static boolean doTestPass() {
        boolean testsPass = true;
        double result = power(2, 2);
        return testsPass && result == 4;
    }

    public static void main(String[] args) {
        if (doTestPass()) {
            System.out.println("Pass");
        } else {
            System.out.println("There are failures");
        }
    }
}