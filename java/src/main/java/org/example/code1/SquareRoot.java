package org.example.code1;

//⏱️ Time Complexity
//O(log(x / ε))
//ε = precision (e.g., 0.0001)
//🧠 Space Complexity
//O(1) → no extra space used
//
public class SquareRoot  {

    /*
     * double squareRoot(double x)
     */
    public static double squareRoot(double x) {
        if (x < 0) {
            throw new IllegalArgumentException("Negative input not allowed");
        }

        if (x == 0 || x == 1) return x;

        double left = 0, right = x;

        // Handle numbers < 1
        if (x < 1) {
            right = 1;
        }

        double epsilon = 0.0001; // precision

        while ((right - left) > epsilon) {
            double mid = left + (right - left) / 2;

            if (mid * mid > x) {
                right = mid;
            } else {
                left = mid;
            }
        }

        return (left + right) / 2;
    }

    public static void main(String args[]) {
        double[] inputs = {2, 4, 100};
        double[] expected_values = {1.41421, 2, 10};
        double threshold = 0.001;

        for (int i = 0; i < inputs.length; i++) {
            if (Math.abs(squareRoot(inputs[i]) - expected_values[i]) > threshold) {
                System.out.printf(
                        "Test failed for %f, expected=%f, actual=%f\n",
                        inputs[i], expected_values[i], squareRoot(inputs[i]));
                return;
            }
        }
        System.out.println("All tests passed");
    }
}