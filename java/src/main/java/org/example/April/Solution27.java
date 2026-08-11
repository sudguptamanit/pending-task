package org.example.April;

// find the median of the two sorted arrays.
// ex. {1, 3} and {2} is 2

//🧠 Complexity
//Time: O(log(min(m, n)))
//Space: O(1)
public class Solution27
{
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] ans = merge(nums1, nums2);

        double ans2;

        if(ans.length % 2 == 0){
            ans2 = (double)(ans[ans.length/2] + ans[ans.length/2 - 1]) / 2;
        } else {
            ans2 = (double)(ans[ans.length/2]);
        }

        return ans2;
    }

    public int[] merge(int[] arr1, int[] arr2){
        int[] ans = new int[arr1.length + arr2.length];

        int p1 = 0, p2 = 0, p3 = 0;

        while(p1 < arr1.length || p2 < arr2.length){
            int val1 = p1 < arr1.length ? arr1[p1] : Integer.MAX_VALUE;
            int val2 = p2 < arr2.length ? arr2[p2] : Integer.MAX_VALUE;

            if(val1 < val2){
                ans[p3++] = val1;
                p1++;
            } else {
                ans[p3++] = val2;
                p2++;
            }
        }
        return ans;
    }
    public static double logic(int[] A, int[] B) {
        // सुनिश्चित A छोटा array हो
        if (A.length > B.length) {
            return logic(B, A);
        }

//        🧠 Complexity
//        Time: O(log(min(m, n)))
//        Space: O(1)
//
        int m = A.length;
        int n = B.length;

        int low = 0, high = m;

        while (low <= high) {
            int partitionA = (low + high) / 2;
            int partitionB = (m + n + 1) / 2 - partitionA;

            int maxLeftA = (partitionA == 0) ? Integer.MIN_VALUE : A[partitionA - 1];
            int minRightA = (partitionA == m) ? Integer.MAX_VALUE : A[partitionA];

            int maxLeftB = (partitionB == 0) ? Integer.MIN_VALUE : B[partitionB - 1];
            int minRightB = (partitionB == n) ? Integer.MAX_VALUE : B[partitionB];

            // सही partition मिला
            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                // even length
                if ((m + n) % 2 == 0) {
                    return (Math.max(maxLeftA, maxLeftB) + Math.min(minRightA, minRightB)) / 2.0;
                }
                // odd length
                else {
                    return Math.max(maxLeftA, maxLeftB);
                }
            }
            // left side बड़ा है → move left
            else if (maxLeftA > minRightB) {
                high = partitionA - 1;
            }
            // right side छोटा है → move right
            else {
                low = partitionA + 1;
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