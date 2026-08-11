package org.example.April;

//Complexity: O(n) time · O(1) space
//
public class MinFlipsAlternating {
    public static int minFlips(String str) {
        int flipsStartWith0 = 0; // flips if target is "010101..."
        int flipsStartWith1 = 0; // flips if target is "101010..."

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // At even indices: expected '0' for pattern1, '1' for pattern2
            if (i % 2 == 0) {
                if (c != '0') flipsStartWith0++;
                if (c != '1') flipsStartWith1++;
            } else {
                // At odd indices: expected '1' for pattern1, '0' for pattern2
                if (c != '1') flipsStartWith0++;
                if (c != '0') flipsStartWith1++;
            }
        }
        return Math.min(flipsStartWith0, flipsStartWith1);
    }

    public static void main(String[] args) {
        System.out.println(minFlips("001"));        // Output: 1
        System.out.println(minFlips("0001010111")); // Output: 2
    }
}