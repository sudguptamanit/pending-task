package org.example.code;

import java.util.Arrays;

//Complexity
//Time        O(n log n) — dominated by sorting
//Space       O(1) — only pointers and counters

public class Min_Num_Platforms_Required {

    public static int findPlatform(int arr[], int dep[]) {
        Arrays.sort(arr);
        Arrays.sort(dep);

        int platforms = 1;
        int maxPlatforms = 1;
        int i = 1; // pointer for arrivals
        int j = 0; // pointer for departures

        while (i < arr.length && j < dep.length) {
            // Next event is an arrival → need one more platform
            if (arr[i] <= dep[j]) {
                platforms++;
                i++;
            } else {
                // Next event is a departure → free up one platform
                platforms--;
                j++;
            }
            maxPlatforms = Math.max(maxPlatforms, platforms);
        }

        return maxPlatforms;
    }

    public static void main(String[] args) {
        int arr[] = { 900, 940, 950, 1100, 1500, 1800 };
        int dep[] = { 910, 1200, 1120, 1130, 1900, 2000 };

        int arr1[] = { 900, 940 };
        int dep1[] = { 910, 1200 };

        if (findPlatform(arr, dep) == 3 && findPlatform(arr1, dep1) == 1)
            System.out.println("All Tests Pass");
        else
            System.out.println("There are test failures");
    }
}