package org.example.experiencegold;

import java.util.HashSet;
import java.util.Set;

public class CountSpecialWord {


//    ⏱️ Time Complexity
//    O(n²)
//    Each center expands at most O(n)
//🧠 Space Complexity
//    O(k)
//    k = number of unique palindromes

    public static int countSpecialWords(String s) {
        Set<String> uniquePalindromes = new HashSet<>();

        for (int i = 0; i < s.length(); i++) {
            // Odd length palindromes
            expand(s, i, i, uniquePalindromes);

            // Even length palindromes
            expand(s, i, i + 1, uniquePalindromes);
        }

        return uniquePalindromes.size();
    }

    private static void expand(String s, int left, int right, Set<String> set) {
        while (left >= 0 && right < s.length() &&
                s.charAt(left) == s.charAt(right)) {

            set.add(s.substring(left, right + 1));
            left--;
            right++;
        }
    }

    public static void main(String[] args) {
        System.out.println(countSpecialWords("aabcabacf")); // 7
        System.out.println(countSpecialWords("aba"));       // 3
        System.out.println(countSpecialWords("aaa"));       // 3
        System.out.println(countSpecialWords("abcd"));      // 4
        System.out.println(countSpecialWords("aa"));        // 2
    }
}