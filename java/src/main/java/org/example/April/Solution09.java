package org.example.April;

public class Solution09 {
    /*
     *   double squareRoot( double x )
     *⚡ Complexity
Time: O(log n)
Space: O(1)
     */

    public static double squareRoot(double x) {
        if (x < 0) {
            throw new IllegalArgumentException("Negative input");
        }
        if (x == 0 || x == 1) {
            return x;
        }
        double left = 0;
        double right = x;
        double precision = 0.00001;
        // For numbers < 1 (e.g., 0.25)
        if (x < 1) {
            right = 1;
        }
        while ((right - left) > precision) {
            double mid = left + (right - left) / 2;

            if (mid * mid < x) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return (left + right) / 2;
    }

    public static void main( String args[])
    {
        double[] inputs = {2, 4, 100};
        double[] expected_values = { 1.41421, 2, 10 };
        double threshold = 0.001;
        for(int i=0; i < inputs.length; i++)
        {
            if( Math.abs(squareRoot(inputs[i])-expected_values[i])>threshold )
            {
                System.out.printf( "Test failed for %f, expected=%f, actual=%f\n", inputs[i], expected_values[i], squareRoot(inputs[i]) );
            }
        }
        System.out.println( "All tests passed");
    }
}