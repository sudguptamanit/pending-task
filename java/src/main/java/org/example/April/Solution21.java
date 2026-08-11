package org.example.April;

import java.util.*;

public class Solution21 {

    public static HashSet<String> uniqueTuples(String input, int len) {
        HashSet<String> result = new HashSet<>();

        if (input == null || len <= 0 || len > input.length()) {
            return result;
        }

        for (int i = 0; i <= input.length() - len; i++) {
            result.add(input.substring(i, i + len));
        }

        return result;
    }

    public static void main( String[] args ) {
        String input = "aab";
        HashSet<String> result = uniqueTuples( input, 2 );
        if( result.contains( "aa" ) && result.contains( "ab" ) ) {
            System.out.println( "Test passed." );

        } else {
            System.out.println( "Test failed." );

        }
    }
}