package org.example.code;

//Time        O(n³)O(n²) pairs × O(n) substring comparison
//Space       O(n²)O(n) dp array + O(n) substring copies per comparison
//
/*
Question:
Combine ingredients in a specific order, any of which may be repeated

As an example, consider the following
(A,B,C,D) in 11 steps: A, B, A, B, C, A, B, A, B, C, E

Encode the string above using only 6 characters: A,B,*,C,*,E

Implement function that takes as input an un-encoded potion and returns the
minimum number of characters required to encode

*/

public class MinimumStep {

    private static int minimalSteps(String ingredients) {
        int n = ingredients.length();
        // dp[i] = minimum encoded length for ingredients[0..i-1]
        int[] dp = new int[n + 1];
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            // Option 1: encode current character as-is (cost = 1)
            dp[i] = dp[i - 1] + 1;

            // Option 2: find the longest substring ending at i
            // that appeared as a prefix somewhere before index i
            // If ingredients[j..i-1] == ingredients[0..i-j-1] for some j,
            // we can replace it with '*' (cost = 1 instead of i-j)
            for (int j = 1; j < i; j++) {
                int len = i - j; // length of substring ingredients[j..i-1]
                if (len < 2) break; // '*' only saves space if length >= 2

                // Check if ingredients[j..i-1] matches ingredients[0..len-1]
                if (ingredients.substring(j, i).equals(ingredients.substring(0, len))) {
                    // Replace this repeated prefix with '*', saving (len - 1) chars
                    int candidate = dp[j] + 1; // cost up to j, plus 1 for '*'
                    dp[i] = Math.min(dp[i], candidate);
                }
            }
        }

        return dp[n];
    }

    private static boolean doTestsPass() {
        return minimalSteps("ABCDABCE") == 8  // no repeated prefix → no savings
                && minimalSteps("ABCABCE")  == 5  // "ABCABC" → "ABC*E" = 5 chars
                && minimalSteps("ABABCABABCE") == 6; // example from problem: A,B,*,C,*,E
    }

    public static void main(String[] args) {
        if (doTestsPass()) {
            System.out.println("Pass");
        } else {
            System.out.println("Fail");
        }
    }
}