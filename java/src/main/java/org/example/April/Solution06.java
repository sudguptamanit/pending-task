package org.example.April;

import java.util.*;
/*
 * This program prints set of anagrams together in given string
 *
 * eg.
 * setOfAnagrams("cat dog tac sat tas god dog") should print "cat tac dog god dog sat tas"
 *
 * ⚡ Complexity
Time: O(n * k log k) (sorting each word)
Space: O(n * k)
*
 */

class Solution06 {

    static String input = "cat dog tac sat tas god dog";

    static void setOfAnagrams(String inputString){
        if (inputString == null || inputString.length() == 0) {
            return;
        }
        String[] words = inputString.split(" ");
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (String word : words) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars); // normalize
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