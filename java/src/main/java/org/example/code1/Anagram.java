package org.example.code1;

import java.util.*;

//⏱️ Time Complexity
//Splitting words: O(n)
//Sorting each word: O(k log k)
//Total: O(n * k log k)
//n = number of words
//        k = average word length
//🧠 Space Complexity
//O(n * k) → storing grouped words

class Anagram {

    static void setOfAnagrams(String inputString) {
        if (inputString == null || inputString.length() == 0) return;

        String[] words = inputString.split(" ");

        // Map: sorted word -> list of anagrams
        Map<String, List<String>> map = new LinkedHashMap<>();

        for (String word : words) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
        }

        // Print grouped anagrams
        StringBuilder result = new StringBuilder();
        for (List<String> group : map.values()) {
            for (String word : group) {
                result.append(word).append(" ");
            }
        }

        System.out.println(result.toString().trim());
    }

    public static void main(String[] args) {
        String input = "cat dog tac sat tas god dog";
        setOfAnagrams(input);
    }
}