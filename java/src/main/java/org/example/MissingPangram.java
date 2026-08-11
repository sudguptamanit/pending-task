package org.example;

import java.util.*;


//Optimal Approach (O(n) time, O(1) space)


public class MissingPangram {

    public static String findMissingLetters(String str) {
        // Boolean array to track letters
        boolean[] present = new boolean[26];

        // Convert string to lowercase
        str = str.toLowerCase();

        // Mark characters present in the string
        for (char ch : str.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                present[ch - 'a'] = true;
            }
        }

        // Collect missing characters
        StringBuilder missing = new StringBuilder();

        for (int i = 0; i < 26; i++) {
            if (!present[i]) {
                missing.append((char) (i + 'a'));
            }
        }

        return missing.toString();
    }

    public static void main(String[] args) {
        String input = "The quick brown fox jumps over the dog";

        String result = findMissingLetters(input);

        if (result.isEmpty()) {
            System.out.println("The string is a Pangram");
        } else {
            System.out.println("Missing letters: " + result);
        }
    }
}