package org.example;

import java.util.*;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |


public class CustomSplit {

    public static List<String> split(String str, char delim) {
        List<String> result = new ArrayList<>();

        if (str == null) return result;

        StringBuilder current = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch == delim) {
                result.add(current.toString());
                current.setLength(0); // reset
            } else {
                current.append(ch);
            }
        }

        // Add last substring
        result.add(current.toString());

        return result;
    }

    public static void main(String[] args) {
        String str = "apple,banana,,grape";
        char delim = ',';

        List<String> parts = split(str, delim);

        for (String s : parts) {
            System.out.println(s);
        }
    }
}