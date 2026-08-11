package org.example;

public class SecondSmallestRotatedArray {

//    Complexity
//    TimeO(log n) — binary search onlySpaceO(1) — no extra memory
//1️⃣ Why lo + (hi - lo) / 2 instead of (lo + hi) / 2 ?
//    Both calculate the midpoint, but one is safer.
//    The Overflow Problem:
//    java// Suppose lo = 1_500_000_000, hi = 2_000_000_000
//
//    // ❌ WRONG way:
//    int mid = (lo + hi) / 2;
// lo + hi = 3_500_000_000 → EXCEEDS Integer.MAX_VALUE (2_147_483_647)
// Result: OVERFLOW → negative number → wrong mid → infinite loop or crash

//    2️⃣ Why (minIndex + 1) % n ?
//    This is the circular/wraparound next index.
//    The Problem it solves:
//    java// Array = {2, 3, 4, 5, 1}  → minIndex = 4 (last position)
    //                                                    ↑
// Next index = 4 + 1 = 5 → DOES NOT EXIST (array size is 5, valid indices: 0-4)
// arr[5] → ArrayIndexOutOfBoundsException 💥
//    The Fix — modulo wraps around:
//    javaint nextIndex = (minIndex + 1) % n;

    // Normal case:  minIndex=2, n=6 → (2+1) % 6 = 3  ✅ (no wraparound needed)
// Edge case:    minIndex=5, n=6 → (5+1) % 6 = 0  ✅ (wraps to start)
// Edge case:    minIndex=4, n=5 → (4+1) % 5 = 0  ✅ (wraps to start)
//
//    3️⃣ Why Math.min(arr[nextIndex], arr[0]) ?
//    After finding minIndex, the 2nd smallest can only be one of two candidates — never anywhere else.
//    Why only these two?
//    A sorted rotated array always looks like this internally:
//            [ large values ... | smallest | small values ... ]
//    LEFT HALF       ↑           RIGHT HALF
//    minIndex
//
//    Everything in the LEFT half is large (close to maximum)
//    Everything in the RIGHT half is small (close to minimum)
//    The only elements near the minimum are:
//
//    arr[nextIndex] → immediate right neighbour of min (smallest of right half)
//    arr[0]         → start of left half (smallest of left half)
//
//
//
//    java// Example 1:  {5, 6, 1, 2, 3, 4}   minIndex=2
//
//   LEFT:  5, 6      RIGHT: 2, 3, 4
//   arr[nextIndex] = arr[3] = 2   ← smallest on right
//   arr[0]                 = 5   ← smallest on left
//   2nd smallest = min(2, 5) = 2 ✅

// Example 2:  {2, 3, 4, 5, 1}     minIndex=4
//
//   LEFT:  2, 3, 4, 5    RIGHT: (empty, wrapped to index 0)
//   arr[nextIndex] = arr[0] = 2   ← wraps around
//   arr[0]                 = 2
//   2nd smallest = min(2, 2) = 2 ✅

    // Example 3:  {1, 2, 3, 4, 5}     not rotated → return arr[1] early
//   2nd smallest = 2 ✅

    public static int findSecondSmallest(int[] arr) {
        int n = arr.length;

        if (arr == null || n < 2)
            throw new IllegalArgumentException("Array must have at least 2 elements");

        // Edge case: not rotated at all (e.g. 1,2,3,4,5)
        if (arr[0] < arr[n - 1])
            return arr[1];

        // Binary search for rotation point (index of smallest element)
        int lo = 0, hi = n - 1;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (arr[mid] > arr[hi])
                lo = mid + 1;   // rotation point is in right half
            else
                hi = mid;       // rotation point is in left half or at mid
        }

        // lo == hi == index of SMALLEST element (rotation point)
        int minIndex = lo;

        // 2nd smallest is either next after min, or arr[0]
        int nextIndex = (minIndex + 1) % n;
        return Math.min(arr[nextIndex], arr[0]);
    }

    public static void main(String[] args) {
        System.out.println(findSecondSmallest(new int[]{5, 6, 1, 2, 3, 4}));  // 2
        System.out.println(findSecondSmallest(new int[]{3, 4, 5, 1, 2}));     // 2
        System.out.println(findSecondSmallest(new int[]{2, 3, 4, 5, 1}));     // 2
        System.out.println(findSecondSmallest(new int[]{1, 2, 3, 4, 5}));     // 2 (not rotated)
        System.out.println(findSecondSmallest(new int[]{2, 1}));               // 2
    }
}