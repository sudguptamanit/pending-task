package org.example.code;

//Time            O(n × len)n-len+1 windows, each substring() costs O(len)
//Space           O(n × len)at most n-len+1 unique strings, each of length len

import java.util.HashSet;

public class UniqueTuple {

    public static HashSet<String> uniqueTuples(String input, int len) {
        HashSet<String> result = new HashSet<>();
        if (input == null || input.length() < len || len <= 0) return result;

        for (int i = 0; i <= input.length() - len; i++) {
            result.add(input.substring(i, i + len));
        }

        return result;
    }

    public static void main(String[] args) {
        // Test 1: basic case from prompt
        {
            HashSet<String> result = uniqueTuples("aab", 2);
            boolean pass = result.contains("aa") && result.contains("ab");
            System.out.println("Test 1: " + (pass ? "Pass" : "Fail"));
            System.out.println("  input='aab', len=2 → " + result); // [aa, ab]
        }

        // Test 2: all unique substrings
        {
            HashSet<String> result = uniqueTuples("abcd", 2);
            boolean pass = result.contains("ab") && result.contains("bc")
                    && result.contains("cd") && result.size() == 3;
            System.out.println("Test 2: " + (pass ? "Pass" : "Fail"));
            System.out.println("  input='abcd', len=2 → " + result); // [ab, bc, cd]
        }

        // Test 3: all duplicate substrings collapse into one
        {
            HashSet<String> result = uniqueTuples("aaaa", 2);
            boolean pass = result.size() == 1 && result.contains("aa");
            System.out.println("Test 3: " + (pass ? "Pass" : "Fail"));
            System.out.println("  input='aaaa', len=2 → " + result); // [aa]
        }

        // Test 4: len == input length
        {
            HashSet<String> result = uniqueTuples("abc", 3);
            boolean pass = result.size() == 1 && result.contains("abc");
            System.out.println("Test 4: " + (pass ? "Pass" : "Fail"));
            System.out.println("  input='abc', len=3 → " + result); // [abc]
        }

        // Test 5: len > input length → empty set
        {
            HashSet<String> result = uniqueTuples("ab", 5);
            boolean pass = result.isEmpty();
            System.out.println("Test 5: " + (pass ? "Pass" : "Fail"));
            System.out.println("  input='ab', len=5 → " + result); // []
        }

        // Test 6: trigrams
        {
            HashSet<String> result = uniqueTuples("abcabc", 3);
            boolean pass = result.contains("abc") && result.contains("bca")
                    && result.contains("cab") && result.size() == 4;
            System.out.println("Test 6: " + (pass ? "Pass" : "Fail"));
            System.out.println("  input='abcabc', len=3 → " + result); // [abc, bca, cab]
        }
    }
}