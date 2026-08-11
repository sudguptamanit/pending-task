package org.example.April;

public class SnowPack
{
    public static Integer computeSnowpack1(Integer[] arr) {
        if (arr == null || arr.length < 3) return 0;

        int n = arr.length;
        int left[] = new int[n];
        int right[] = new int[n];

        left[0] = arr[0];

        for(int i=1;i<n;i++){
            left[i] = Math.max(left[i-1],arr[i]);
        }

        right[n-1] = arr[n-1];

        for(int i=n-2;i>=0;i--){
            right[i] = Math.max(right[i+1],arr[i]);
        }

        int snow = 0;

        for(int i=0;i<n;i++){
            snow += Math.min(left[i],right[i])-arr[i];
        }

        return snow;
    }
    /*
     **  Find the amount of snow that could be captured.
     */
    public static Integer computeSnowpack(Integer[] arr) {
        if (arr == null || arr.length < 3) return 0;

        int n = arr.length;
        int left = 0, right = n - 1;
        int leftMax = 0, rightMax = 0;
        int snow = 0;

        while (left < right) {
            if (arr[left] <= arr[right]) {
                if (arr[left] >= leftMax) {
                    leftMax = arr[left];      // no snow, update wall
                } else {
                    snow += leftMax - arr[left]; // trapped water
                }
                left++;
            } else {
                if (arr[right] >= rightMax) {
                    rightMax = arr[right];    // no snow, update wall
                } else {
                    snow += rightMax - arr[right]; // trapped water
                }
                right--;
            }
        }

        return snow;
    }

    /*
     **  Returns true if the tests pass. Otherwise, returns false;
     */
    public static boolean doTestsPass()
    {
        boolean result = true;
//        result &= computeSnowpack(new Integer[]{0,1,3,0,1,2,0,4,2,0,3,0}) == 13;
//        result &= computeSnowpack(new Integer[]{3, 0, 1, 0, 4, 0, 2}) == 10;
//        result &= computeSnowpack(new Integer[]{3, 0, 2, 0, 4}) == 7;
//        result &= computeSnowpack(new Integer[]{1, 2, 3, 4}) == 0;

        return result;
    }

    /*
     **  Execution entry point.
     */
    public static void main(String[] args)
    {
//        if(doTestsPass())
//        {
//            System.out.println("All tests pass");
//        }
//        else
//        {
//            System.out.println("Tests fail.");
//        }
        System.out.println(computeSnowpack(new Integer[]{0,1,3,0,1,2,0,4,2,0,3,0}) == 13);
        System.out.println(computeSnowpack(new Integer[]{3, 0, 1, 0, 4, 0, 2}) == 10);
        System.out.println(computeSnowpack(new Integer[]{3, 0, 2, 0, 4}) == 7);
        System.out.println(computeSnowpack(new Integer[]{1, 2, 3, 4}) == 0);

        System.out.println(computeSnowpack1(new Integer[]{0,1,3,0,1,2,0,4,2,0,3,0}) == 13);
        System.out.println(computeSnowpack1(new Integer[]{3, 0, 1, 0, 4, 0, 2}) == 10);
        System.out.println(computeSnowpack1(new Integer[]{3, 0, 2, 0, 4}) == 7);
        System.out.println(computeSnowpack1(new Integer[]{1, 2, 3, 4}) == 0);
    }
}