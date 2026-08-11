package org.example.April;

/*
Question:
Combine ingredients in a specific order, any of which may be repeated

As an example, consider the following
(A,B,C,D) in 11 steps: A, B, A, B, C, A, B, A, B, C, E

Encode the string above using only 6 characters: A,B,*,C,*,E

Implement function that takes as input an un-encoded potion and returns the
minimum number of characters required to encode

🔥 Example Walkthrough
Input:
s = "ABCABCE"
Build step-by-step:
i = 1 → "A"
dp[1] = 1
i = 2 → "AB"
dp[2] = 2
i = 3 → "ABC"
dp[3] = 3
i = 4 → "ABCA"

Check repetition:

"A" != "C" → no match
dp[4] = 4
i = 5 → "ABCAB"

Check:

len = 2:
"AB" == "AB" ✅

👉 So:

dp[5] = dp[3] + 1 = 3 + 1 = 4

Instead of "ABCAB" → "ABC*"

i = 6 → "ABCABC"

Check:

len = 3:
"ABC" == "ABC" ✅

👉 So:

dp[6] = dp[3] + 1 = 3 + 1 = 4

Representation:

"ABC*"
i = 7 → "ABCABCE"

No repetition

dp[7] = dp[6] + 1 = 5
✅ Final Answer:
5

*/

public class Solution19
{

        public static int minimalSteps(String s) {
            int n = s.length();
            int[] dp = new int[n + 1];
            // max worst case: no compression
            for (int i = 0; i <= n; i++) {
                dp[i] = i;
            }

            for (int i = 1; i <= n; i++) {
                // Case 1: add single character
                dp[i] = dp[i - 1] + 1;

                // Case 2: check for repeating substrings
                for (int len = 1; len <= i / 2; len++) {
                    String pattern = s.substring(i - len, i);
                    String prev = s.substring(i - 2 * len, i - len);

                    if (pattern.equals(prev)) {
                        dp[i] = Math.min(dp[i], dp[i - len] + 1); // use '*'
                    }
                }
            }
            return dp[n];
        }

        public static void main(String[] args) {

            if (minimalSteps("ABCDABCE") == 8 &&
                    minimalSteps("ABCABCE") == 5) {
                System.out.println("Pass");
            } else {
                System.out.println("Fail");
            }
        }
    }
