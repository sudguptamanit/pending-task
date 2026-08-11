package org.example.code1;

//⏱️ Time Complexity
//O(n) → single pass through string
//🧠 Space Complexity
//O(1) → fixed array of size 26

public class Panagram {

/**
 * Pangram FInder
 *
 * The sentence "The quick brown fox jumps over the lazy dog" contains
 * every single letter in the alphabet. Such sentences are called pangrams.
 * Write a function findMissingLetters, which takes a String `sentence`,
 * and returns all the letters it is missing
 *
 */

    private static class PanagramDetector {
        private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

        public String findMissingLetters(String sentence) {
            boolean[] present = new boolean[26];

            // Step 1: mark present characters
            for (char ch : sentence.toLowerCase().toCharArray()) {
                if (ch >= 'a' && ch <= 'z') {
                    present[ch - 'a'] = true;
                }
            }

            // Step 2: collect missing letters
            StringBuilder missing = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                if (!present[i]) {
                    missing.append((char) ('a' + i));
                }
            }

            return missing.toString();
        }
    }

    public static void main(String[] args) {
        PanagramDetector pd = new PanagramDetector();
        boolean success = true;

        success = success && "".equals(pd.findMissingLetters("The quick brown fox jumps over the lazy dog"));
        success = success && "abcdefghijklmnopqrstuvwxyz".equals(pd.findMissingLetters(""));

        if (success) {
            System.out.println("Pass ");
        } else {
            System.out.println("Failed");
        }
    }

}