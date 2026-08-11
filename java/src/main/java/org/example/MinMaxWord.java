package org.example;

//| Approach            | Time | Space |
//        | ------------------- | ---- | ----- |
//        | Split + Loop        | O(n) | O(n)  |
//        | One-pass (no split) | O(n) | O(1)  |


public class MinMaxWord {

    public static void findMinMaxWord(String str) {
        int minLen = Integer.MAX_VALUE, maxLen = 0;
        String minWord = "", maxWord = "", word = "";

        for (int i = 0; i <= str.length(); i++) {
            if (i == str.length() || str.charAt(i) == ' ') {
                if (!word.isEmpty()) {
                    int len = word.length();

                    if (len < minLen) {
                        minLen = len;
                        minWord = word;
                    }
                    if (len > maxLen) {
                        maxLen = len;
                        maxWord = word;
                    }
                }
                word = "";
            } else {
                word += str.charAt(i);
            }
        }

        System.out.println("Minimum length word: " + minWord);
        System.out.println("Maximum length word: " + maxWord);
    }

    public static void main(String[] args) {
        String input = "This is a test string";
        findMinMaxWord(input);
    }
}

