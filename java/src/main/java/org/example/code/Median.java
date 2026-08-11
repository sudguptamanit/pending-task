package org.example.code;

// find the median of the two sorted arrays.
// ex. {1, 3} and {2} is 2

//Time        O(log(min(n,m))) — binary search on smaller array only
//Space       O(1) — no extra arrays allocated

public class Median
{

    public static double logic(int[] A, int[] B) {
        // Always binary search on the smaller array
        if (A.length > B.length) return logic(B, A);

        int lenA = A.length;
        int lenB = B.length;
        int totalLeft = (lenA + lenB + 1) / 2; // elements on the left half

        int lo = 0, hi = lenA;

        while (lo <= hi) {
            int partA = (lo + hi) / 2;          // how many elements from A in left half
            int partB = totalLeft - partA;       // how many elements from B in left half

            int maxLeftA  = (partA == 0)    ? Integer.MIN_VALUE : A[partA - 1];
            int minRightA = (partA == lenA) ? Integer.MAX_VALUE : A[partA];
            int maxLeftB  = (partB == 0)    ? Integer.MIN_VALUE : B[partB - 1];
            int minRightB = (partB == lenB) ? Integer.MAX_VALUE : B[partB];

            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                // correct partition found
                if ((lenA + lenB) % 2 == 1) {
                    return Math.max(maxLeftA, maxLeftB);           // odd total
                } else {
                    return (Math.max(maxLeftA, maxLeftB)
                            + Math.min(minRightA, minRightB)) / 2.0; // even total
                }
            } else if (maxLeftA > minRightB) {
                hi = partA - 1;  // move left in A
            } else {
                lo = partA + 1;  // move right in A
            }
        }

        throw new IllegalArgumentException("Input arrays are not sorted");
    }

    public static boolean pass()
    {
        boolean result = true;
        result = result && logic(new int[]{1, 3}, new int[]{2, 4}) == 2.5;
        return result;
    };

    public static void main(String[] args)
    {
        if(pass())
        {
            System.out.println("pass");
        }
        else
        {
            System.out.println("some failures");
        }
    }
}