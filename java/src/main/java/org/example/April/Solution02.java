package org.example.April;

/**
 * Pangram FInder
 *
 * The sentence "The quick brown fox jumps over the lazy dog" contains
 * every single letter in the alphabet. Such sentences are called pangrams.
 * Write a function findMissingLetters, which takes a String `sentence`,
 * and returns all the letters it is missing
 *
 * 💡 Complexity
 * Time: O(n) (single pass through string)
 * Space:
 * Boolean array → O(1) (26 fixed)
 *
 *
 */
class Solution02 {

    private static class PanagramDetector {
        private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

        public String findMissingLetters(String sentence) {
            if (sentence == null || sentence.length() == 0) {
                return ALPHABET;
            }
            boolean[] seen = new boolean[26];
            // Mark seen characters
            for (int i = 0; i < sentence.length(); i++) {
                char ch = Character.toLowerCase(sentence.charAt(i));
                if (ch >= 'a' && ch <= 'z') {
                    seen[ch - 'a'] = true;
                }
            }
            // Build missing letters
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                if (!seen[i]) {
                    result.append((char) ('a' + i));
                }
            }
            return result.toString();
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