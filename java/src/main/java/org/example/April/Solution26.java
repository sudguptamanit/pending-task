package org.example.April;

// Given a a string of letters and a dictionary, the function longestWord should
//     find the longest word or words in the dictionary that can be made from the letters
//     Input: letters = "oet", dictionary = {"to","toe","toes"}
//     Output: {"toe"}
//
//⚡ Complexity
//        Let:
//        n = number of dictionary words
//        k = average word length
//
//        👉 Time: O(n × k)
//        👉 Space: O(1) (fixed 26 array)

//👉 Given some letters, find the longest word(s) from a dictionary that can be formed using those letters (without reusing letters more than available).
//
//        🧠 Problem Understanding
//        Input:
//        letters = "oet"
//        dictionary = {"to","toe","toes"}
//        Output:
//        {"toe"}
//
//        👉 Because:
//
//        "toe" can be formed using o, e, t
//        "toes" ❌ needs extra 's'
//        🔑 Core Idea
//        Count available letters
//        For each word in dictionary:
//        Check if it can be formed
//        Track the longest valid word(s)
//        🔹 Step 1: Count Letters
//        int[] letterCount = new int[26];
//        for (char c : letters.toCharArray()) {
//        letterCount[c - 'a']++;
//        }
//
//        👉 Converts letters into frequency array
//
//        🔍 Example
//        letters = "toe"
//        Letter	Count
//        t	1
//        o	1
//        e	1
//
//        All others = 0
//
//        🔹 Step 2: Loop Dictionary
//        for (String word : dict.getEntries())
//
//        We check each word:
//
//        "to", "toe", "toes", "doe", ...
//        🔹 Step 3: Check if word can be formed
//        if (canForm(word, letterCount))
//        🔍 canForm() Logic
//        int[] temp = new int[26];
//
//        👉 Count letters required for this word
//
//        Example: "toe"
//        Letter	temp count	allowed?
//        t	1	✅
//        o	1	✅
//        e	1	✅
//
//        👉 Valid word
//
//        Example: "toes"
//        Letter	temp count	allowed?
//        t	1	✅
//        o	1	✅
//        e	1	✅
//        s	1	❌ (not in input)
//
//        👉 Invalid → return false
//
//        🔹 Step 4: Track Longest Word
//        if (word.length() > maxLen) {
//        result.clear();
//        result.add(word);
//        maxLen = word.length();
//        }
//
//        👉 If longer word found:
//
//        Clear old results
//        Add new word
//        else if (word.length() == maxLen) {
//        result.add(word);
//        }
//
//        👉 If same length → keep both
//
//        🔥 Full Example Walkthrough
//        Input:
//        letters = "toe"
//        dictionary = {"to", "toe", "toes", "doe"}
//        Iteration 1: "to"
//
//        ✔ valid
//
//        maxLen = 2
//        result = {"to"}
//        Iteration 2: "toe"
//
//        ✔ valid
//
//        maxLen = 3
//        result = {"toe"}   ← replaces "to"
//        Iteration 3: "toes"
//
//        ❌ invalid → skip
//
//        Iteration 4: "doe"
//
//        ❌ invalid (no 'd')
//
//        ✅ Final Output:
//        {"toe"}
//

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

public class Solution26 {
    public static Set<String> longestWord(String letters, Dictionary dict) {
        Set<String> result = new HashSet<>();
        // Step 1: Count frequency of given letters
        int[] letterCount = new int[26];
        for (char c : letters.toCharArray()) {
            letterCount[c - 'a']++;
        }
        int maxLen = 0;
        // Step 2: Check each dictionary word
        for (String word : dict.getEntries()) {
            if (canForm(word, letterCount)) {
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
    private static boolean canForm(String word, int[] letterCount) {
        int[] temp = new int[26];

        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            temp[idx]++;
            if (temp[idx] > letterCount[idx]) {
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
