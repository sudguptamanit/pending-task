package org.example.April;

import java.util.HashMap;
import java.util.Map;

public class Solution33_1 {

//    Summary
//    Complexity
//    Time          O(den)
//    Space         O(den)
//
    public static String fractionRepresentation(int num, int den) {
        StringBuilder result = new StringBuilder();
        num = Math.abs(num);
        den = Math.abs(den);
        result.append(num / den);
        int remainder = num % den;
        if (remainder == 0) return result.toString();
        result.append('.');
        Map<Integer, Integer> remainderIndexMap = new HashMap<>();
        StringBuilder decimals = new StringBuilder();
        while (remainder != 0) {
            if (remainderIndexMap.containsKey(remainder)) {
                // We've seen this remainder before — repeating block starts here
                int repeatStart = remainderIndexMap.get(remainder);
                decimals.insert(repeatStart, '(');
                decimals.append(')');
                return result.append(decimals).toString();
            }
            remainderIndexMap.put(remainder, decimals.length());
            remainder *= 10;
            decimals.append(remainder / den);
            remainder %= den;
        }
        return result.append(decimals).toString();
    }

    public static void main(String[] args) {
        System.out.println(fractionRepresentation(1, 2));   // 0.5
        System.out.println(fractionRepresentation(1, 3));   // 0.(3)
        System.out.println(fractionRepresentation(6, 11));  // 0.(54)

        if (fractionRepresentation(1, 2).equals("0.5")
                && fractionRepresentation(6, 11).equals("0.(54)")
                && fractionRepresentation(1, 3).equals("0.(3)")) {
            System.out.println("All passed");
        } else {
            System.out.println("Failed");
        }
    }
}