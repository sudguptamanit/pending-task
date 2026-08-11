package org.example;

import java.util.*;

public class PairSum {

    // ─── Approach 1: HashSet — O(n) time, O(n) space (OPTIMAL) ───────────────
    public static int countPairsHashSet(int[] arr, int target) {
        Set<Integer> seen = new HashSet<>();
        Set<String> counted = new HashSet<>(); // avoid duplicate pair counting
        int count = 0;

        for (int num : arr) {
            int complement = target - num;
            if (seen.contains(complement)) {
                // Normalise the pair key so (2,7) and (7,2) aren't double-counted
                String key = Math.min(num, complement) + "," + Math.max(num, complement);
                if (counted.add(key)) count++;
            }
            seen.add(num);
        }
        return count;
    }

    // ─── Approach 2: Two Pointers — O(n log n) time, O(1) extra space ────────
    public static int countPairsTwoPointers(int[] arr, int target) {
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        int lo = 0, hi = sorted.length - 1, count = 0;

        while (lo < hi) {
            int sum = sorted[lo] + sorted[hi];
            if (sum == target)      { count++; lo++; hi--; }
            else if (sum < target)  { lo++; }
            else                    { hi--; }
        }
        return count;
    }

    // ─── Approach 3: Brute Force — O(n²) time, O(1) space ───────────────────
    public static int countPairsBruteForce(int[] arr, int target) {
        int count = 0;
        for (int i = 0; i < arr.length; i++)
            for (int j = i + 1; j < arr.length; j++)
                if (arr[i] + arr[j] == target) count++;
        return count;
    }

    // ─── With pair details ────────────────────────────────────────────────────
    public static List<int[]> getPairs(int[] arr, int target) {
        List<int[]> pairs = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        Set<String> added = new HashSet<>();

        for (int num : arr) {
            int comp = target - num;
            if (seen.contains(comp)) {
                String key = Math.min(num, comp) + "," + Math.max(num, comp);
                if (added.add(key)) pairs.add(new int[]{Math.min(num, comp), Math.max(num, comp)});
            }
            seen.add(num);
        }
        return pairs;
    }

    public static void main(String[] args) {
        int[] arr    = {2, 7, 4, 1, 3, 6};
        int   target = 9;

        System.out.println("Array : " + Arrays.toString(arr));
        System.out.println("Target: " + target);
        System.out.println();
        System.out.println("HashSet      → " + countPairsHashSet(arr, target)     + " pair(s)");
        System.out.println("Two Pointers → " + countPairsTwoPointers(arr, target) + " pair(s)");
        System.out.println("Brute Force  → " + countPairsBruteForce(arr, target)  + " pair(s)");
        System.out.println();

        System.out.println("Pairs that sum to " + target + ":");
        for (int[] p : getPairs(arr, target))
            System.out.println("  (" + p[0] + ", " + p[1] + ")");
    }
}