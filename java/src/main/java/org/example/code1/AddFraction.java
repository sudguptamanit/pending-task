package org.example.code1;

//⏱️ Time Complexity
//Fraction addition: O(1)
//GCD computation: O(log(min(a, b)))
//
//        👉 Overall: O(log n)
//
//🧠 Space Complexity
//O(1) → constant extra space

public class AddFraction  {

    /**
     * Given two fractions passed in as int arrays,
     * returns the fraction which is result of adding the two input fractions.
     */
    public static int[] addFractions(int[] fraction1, int[] fraction2) {
        int a = fraction1[0], b = fraction1[1];
        int c = fraction2[0], d = fraction2[1];

        // Step 1: Add fractions
        int numerator = a * d + b * c;
        int denominator = b * d;

        // Step 2: Reduce using GCD
        int gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;

        return new int[]{numerator, denominator};
    }

    // Euclidean Algorithm for GCD
    private static int gcd(int x, int y) {
        while (y != 0) {
            int temp = y;
            y = x % y;
            x = temp;
        }
        return x;
    }

    public static void main(String[] args) {
        int[] result = addFractions(new int[]{2, 3}, new int[]{1, 2});

        if (result[0] == 7 && result[1] == 6) {
            System.out.println("Test passed.");
        } else {
            System.out.println("Test failed.");
        }
    }
}