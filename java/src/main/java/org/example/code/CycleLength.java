package org.example.code;

import java.util.HashMap;

//Time        O(n)each index visited at most once
//Space       O(n)HashMap stores at most n entries

public class CycleLength {

    public static int countLengthOfCycle(int[] arr, int startIndex) {
        if (arr == null || arr.length == 0) return -1;

        HashMap<Integer, Integer> visited = new HashMap<>();
        int current = startIndex;
        int step = 0;

        while (current >= 0 && current < arr.length) {
            if (visited.containsKey(current)) {
                return step - visited.get(current); // cycle length = current step - step when first visited
            }
            visited.put(current, step);
            current = arr[current];
            step++;
        }

        return -1; // no cycle found
    }

    public static void main(String[] args) {
        boolean testsPassed = true;

        // Test 1: 0 → 1 → 0  (cycle length 2)
        testsPassed &= countLengthOfCycle(new int[]{1, 0}, 0) == 2;

        // Test 2: 0 → 1 → 2 → 0  (cycle length 3)
        testsPassed &= countLengthOfCycle(new int[]{1, 2, 0}, 0) == 3;

        // Test 3: start mid-array, cycle not from index 0
        // 2 → 0 → 1 → 2  (cycle length 3)
        testsPassed &= countLengthOfCycle(new int[]{1, 2, 0}, 2) == 3;

        // Test 4: self-loop  0 → 0  (cycle length 1)
        testsPassed &= countLengthOfCycle(new int[]{0}, 0) == 1;

        // Test 5: tail into cycle — start leads to cycle but isn't part of it
        // 0 → 2 → 3 → 4 → 3  (cycle is 3→4→3, length 2)
        testsPassed &= countLengthOfCycle(new int[]{2, -1, 3, 4, 3}, 0) == 2;

        // Test 6: out-of-bounds pointer → no cycle
        testsPassed &= countLengthOfCycle(new int[]{1, 5}, 0) == -1;

        // Test 7: null input → no cycle
        testsPassed &= countLengthOfCycle(null, 0) == -1;

        System.out.println(testsPassed ? "All tests passed." : "Tests failed.");
    }
}