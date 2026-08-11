package org.example;

import java.util.*;

public class GroupAnagrams {

    public static List<List<String>> groupAnagrams(String[] words) {
        Map<String, List<String>> map = new HashMap<>();

        for (String word : words) {
            String key = getFrequencyKey(word);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
            System.out.println(" "+map.toString());
        }

        return new ArrayList<>(map.values());
    }

    // Builds key like "#2#0#0#1..." representing char frequencies
    // No sorting needed — pure O(k) per word
    private static String getFrequencyKey(String word) {
        System.out.println(" "+word);
        int[] freq = new int[26];

        for (char c : word.toCharArray())
            freq[c - 'a']++;        // count each character

        StringBuilder sb = new StringBuilder();
        for (int count : freq)
            sb.append("#").append(count);   // "#2#0#1..." → unique key

        System.out.println(" "+sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println(groupAnagrams(words));
        // [[eat, tea, ate], [tan, nat], [bat]]
    }
}