package org.example;

public class FirstUniqueCharacter {
//    Two Pass O(n) with O(1) space
    public static char firstUniqueChar(String s) {
        if (s == null || s.isEmpty())
            throw new IllegalArgumentException("String cannot be null or empty");

        int[] freq = new int[26];  // fixed 26 lowercase letters → O(1) space

        // Pass 1 — count frequencies
        for (char c : s.toCharArray())
            freq[c - 'a']++;

        // Pass 2 — find first char with freq = 1
        for (char c : s.toCharArray())
            if (freq[c - 'a'] == 1)
                return c;

        throw new IllegalArgumentException("No unique character found");
    }

    public static void main(String[] args) {
        System.out.println(firstUniqueChar("aabdcce"));    // b
        System.out.println(firstUniqueChar("aabbdcced"));  // d
        System.out.println(firstUniqueChar("aabb"));       // exception: no unique
        System.out.println(firstUniqueChar("leetcode"));   // l
        System.out.println(firstUniqueChar("aadadbd"));    // b
        System.out.println(firstUniqueChar("z"));          // z
    }
}