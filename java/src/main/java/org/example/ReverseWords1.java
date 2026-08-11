package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |

public class ReverseWords1 {

    public static String reverseEachWord(String str) {
        if (str == null || str.length() == 0) return str;

        StringBuilder result = new StringBuilder();
        StringBuilder word = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch != ' ') {
                word.append(ch);
            } else {
                // Reverse current word and append
                result.append(word.reverse());
                result.append(" ");
                word.setLength(0); // clear word
            }
        }

        // Append last word
        result.append(word.reverse());

        return result.toString();
    }

    public static void main(String[] args) {
        String input = "I AM A TESTER";
        System.out.println(reverseEachWord(input));
        // Output: I MA A RETSET
    }
}