package org.example.code1;

import java.util.HashMap;
import java.util.Map;

//⏱️ Time Complexity
//Frequency count: O(n)
//Second scan: O(n)
//👉 Overall: O(n)
//🧠 Space Complexity
//O(1) → if character set is fixed (ASCII = 256)
//O(k) → for HashMap (k = unique characters)
//
public class FirstNonRepeat {

    /**
     * Finds the first character that does not repeat anywhere in the input string
     */
    public static char findFirst1(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("Invalid input");
        }

        // Step 1: Count frequency
        Map<Character, Integer> freq = new HashMap<>();
        for (char ch : input.toCharArray()) {
            freq.put(ch, freq.getOrDefault(ch, 0) + 1);
        }

        // Step 2: Find first non-repeating character
        for (char ch : input.toCharArray()) {
            if (freq.get(ch) == 1) {
                return ch;
            }
        }

        // If no unique character found
        return '\0'; // or throw exception
    }

    public static char findFirst(String input) {
        int[] freq = new int[256]; // ASCII

        for (char ch : input.toCharArray()) {
            freq[ch]++;
        }

        for (char ch : input.toCharArray()) {
            if (freq[ch] == 1) {
                return ch;
            }
        }

        return '\0';
    }

    public static void main(String args[]) {
        String[] inputs = {"apple", "racecars", "ababdc"};
        char[] outputs = {'a', 'e', 'd'};

        boolean result = true;
        for (int i = 0; i < inputs.length; i++) {
            result = result && findFirst(inputs[i]) == outputs[i];
            if (!result)
                System.out.println("Test failed for: " + inputs[i]);
            else
                System.out.println("Test passed for: " + inputs[i]);
        }
    }
}