package org.example.April;

public class Solution31
{
    public static int subArrayExceedsSum(int arr[], int target) {
        int n = arr.length;
        int minLen = Integer.MAX_VALUE;
        int sum = 0;
        int start = 0;

        for (int end = 0; end < n; end++) {
            sum += arr[end];

            // shrink window while condition is satisfied
            while (sum > target) {
                minLen = Math.min(minLen, end - start + 1);
                sum -= arr[start];
                start++;
            }
        }

        return (minLen == Integer.MAX_VALUE) ? -1 : minLen;
    }

    /**
     * Execution entry point.
     * 🧠 Complexity
     * Time: O(n) (each element visited at most twice)
     * Space: O(1)
     */
    public static void main(String[] args)
    {
        boolean result = true;
        int[] arr = { 1, 2, 3, 4 };
        result = result && subArrayExceedsSum( arr, 6 ) == 2;
        result = result && subArrayExceedsSum( arr, 12 ) == -1;

        if( result )
        {
            System.out.println("All tests pass\n");
        }
        else
        {
            System.out.println("There are test failures\n");
        }
    }
};