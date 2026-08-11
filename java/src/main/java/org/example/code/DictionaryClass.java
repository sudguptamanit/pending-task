package org.example.code;


// Given a a string of letters and a dictionary, the function longestWord should
//     find the longest word or words in the dictionary that can be made from the letters
//     Input: letters = "oet", dictionary = {"to","toe","toes"}
//     Output: {"toe"}

import java.util.*;

class Dictionary {
    private String[] entries;

    public Dictionary(String[] entries) {
        this.entries = entries;
    }

    public boolean contains(String word) {
        return Arrays.asList(entries).contains(word);
    }
    public String[] getEntries() {
        return entries;
    }
}

public class DictionaryClass {
//    public static Set<String> longestWord(String letters, Dictionary dict) {
//        Set<String> result = new HashSet<String>();
//        if (dict.contains(letters)) {
//            result.add(letters);
//        }
//        return result;
//    }
public static Set<String> longestWord(String letters, Dictionary dict) {
    Set<String> result = new HashSet<>();

    // Frequency of given letters
    int[] freq = new int[26];
    for (char c : letters.toCharArray()) {
        freq[c - 'a']++;
    }

    int maxLen = 0;

    for (String word : dict.getEntries()) {

        if (canForm(word, freq)) {
            if (word.length() > maxLen) {
                result.clear();
                result.add(word);
                maxLen = word.length();
            } else if (word.length() == maxLen) {
                result.add(word);
            }
        }
    }

    return result;
}
    private static boolean canForm(String word, int[] freq) {
        int[] temp = new int[26];

        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            temp[idx]++;
            if (temp[idx] > freq[idx]) {
                return false;
            }
        }

        return true;
    }


    public static boolean pass() {
        Dictionary dict = new Dictionary(new String[]{"to", "toe", "toes", "doe", "dog", "god", "dogs", "banana"});
        boolean r = new HashSet<String>(Arrays.asList("toe")).equals(longestWord("toe", dict));
        return r;
    }

    public static void main(String[] args) {
        if(pass()) {
            System.out.println("Pass");
        } else {
            System.err.println("Fails");
        }
    }
}