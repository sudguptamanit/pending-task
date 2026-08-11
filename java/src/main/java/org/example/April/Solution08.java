package org.example.April;

public class Solution08 {

    /**
     * Given two fractions passed in as int arrays,
     * returns the fraction which is result of adding the two input fractions.
     *
     * ⚡ Complexity
     * Time: O(log n) (due to GCD)
     * Space: O(1)
     *
     */
    public static int[] addFractions(int[] fraction1, int[] fraction2) {
        int a = fraction1[0];
        int b = fraction1[1];
        int c = fraction2[0];
        int d = fraction2[1];
        // Compute numerator and denominator
        int numerator = a * d + b * c;
        int denominator = b * d;
        // Simplify using GCD
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        numerator /= gcd;
        denominator /= gcd;
        return new int[]{numerator, denominator};
    }

    private static int gcd(int x, int y) {
        while (y != 0) {
            int temp = y;
            y = x % y;
            x = temp;
        }
        return x;
    }

    public static void main( String[] args ) {
        int[] result = addFractions( new int[]{ 2, 3 }, new int[]{ 1, 2 } );

        if( result[ 0 ] == 7 && result[ 1 ] == 6 ) {
            System.out.println( "Test passed." );
            //return true;
        } else {
            System.out.println( "Test failed." );
            //return false;
        }
    }
}