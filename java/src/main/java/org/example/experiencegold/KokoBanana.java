package org.example.experiencegold;

//Question 2:-
//Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards have gone and will come back in h hours.
//Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of bananas and eats k bananas from that pile. If the pile has less than k bananas, she eats all of them instead and will not eat any more bananas during this hour.
//Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.
//Return the minimum integer k such that she can eat all the bananas within h hours.
//Input: piles = [3,6,7,11], h = 8
//Output: 4
//Input: piles = [30,11,23,4,20], h = 5
//Output: 30

public class KokoBanana {
//
//⏱️ Time Complexity
//    Binary Search: O(log max(pile))
//    Each check: O(n)
//
//👉 Total:
//
//    O(n * log(max(pile)))
//            🧠 Space Complexity
//    O(1) → no extra space
//
    public static int minEatingSpeed(int[] piles, int h) {
        int left = 1;
        int right = 0;

        // Find max pile (upper bound)
        for (int pile : piles) {
            right = Math.max(right, pile);
        }

        int result = right;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (canFinish(piles, h, mid)) {
                result = mid;
                right = mid - 1; // try smaller k
            } else {
                left = mid + 1;  // need bigger k
            }
        }

        return result;
    }

    private static boolean canFinish(int[] piles, int h, int k) {
        int hours = 0;

        for (int pile : piles) {
            // ceil(pile / k) without using floating point
            hours += (pile + k - 1) / k;
        }

        return hours <= h;
    }

    public static void main(String[] args) {
        System.out.println(minEatingSpeed(new int[]{3,6,7,11}, 8)); // 4
        System.out.println(minEatingSpeed(new int[]{30,11,23,4,20}, 5)); // 30
    }
}