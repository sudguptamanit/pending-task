package org.example;

import java.util.*;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |


public class FirstRepeatedWord {

    public static String firstRepeatedWord(String str) {
        if (str == null || str.isEmpty()) return null;

        String[] words = str.split("\\s+");
        Set<String> seen = new HashSet<>();

        for (String word : words) {
            if (seen.contains(word)) {
                return word;
            }
            seen.add(word);
        }

        return null; // no repetition
    }

    public static void main(String[] args) {
        String input = "Ravi had been saying that he had been there";
        System.out.println(firstRepeatedWord(input)); // had
    }
}

//public static String firstRepeatedWord(String str) {
//    if (str == null || str.isEmpty()) return null;
//
//    str = str.toLowerCase().replaceAll("[^a-z ]", "");
//    String[] words = str.split("\\s+");
//
//    Set<String> seen = new HashSet<>();
//
//    for (String word : words) {
//        if (seen.contains(word)) return word;
//        seen.add(word);
//    }
//
//    return null;
//}