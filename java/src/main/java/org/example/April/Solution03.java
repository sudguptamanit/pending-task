package org.example.April;

public class Solution03
{

    /*
     * public static int FindMin(int a[])
     * Returns the smallest number in array that has been rotated
     * For example - Array {3,4,5,6,1,2} returns 1
     *
     * ⚡ Complexity
Time: O(log n) ✅
Space: O(1) ✅
*
*
* ⚠️ Follow-up (Interview Twist)

If duplicates are allowed (e.g. {2,2,2,0,1}):

Worst case becomes O(n)
Need slight modification:
if (a[mid] == a[right]) {
  right--; // shrink search space
}
*
     */

    public static int FindMin(int a[])
    {
        if (a == null || a.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }
        int left = 0;
        int right = a.length - 1;
        // If array is not rotated
        if (a[left] <= a[right]) {
            return a[left];
        }
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (a[mid] > a[right]) {
                // Minimum is in right half
                left = mid + 1;
            } else {
                // Minimum is in left half including mid
                right = mid;
            }
        }
        return a[left];
    }

    public static void main(String args[])
    {
        boolean result = true;
        result = result && FindMin(new int[]{3,4,5,6,1,2}) == 1;
        result = result && FindMin(new int[]{2,1}) == 1;
        result = result && FindMin(new int[]{1}) == 1;

        try {
            FindMin(null);
            result = false;
        }
        catch(Exception e)
        {
            result = result && true;
        }

        if(result)
        {
            System.out.println("All tests pass");
        }
        else
        {
            System.out.println("There are test failures");
        }
    }
}