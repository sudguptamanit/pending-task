package org.example.code;

//Time        O(n) — each element added and removed at most once
//Space       O(1) — only pointers and counters

public class SubArraySum
{
    public static int subArrayExceedsSum(int arr[], int target) {
        int left = 0, sum = 0;
        int minLen = Integer.MAX_VALUE;

        for (int right = 0; right < arr.length; right++) {
            sum += arr[right];

            // Shrink window from left while sum exceeds target
            while (sum > target) {
                minLen = Math.min(minLen, right - left + 1);
                sum -= arr[left];
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? -1 : minLen;
    }

    /**
     * Execution entry point.
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