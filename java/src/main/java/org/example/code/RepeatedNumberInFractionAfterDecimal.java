package org.example.code;

import java.util.LinkedHashMap;
import java.util.Map;

//Time        O(den) — at most den unique remainders before a cycle
//Space       O(den) — remainder map stores at most den entries

public class RepeatedNumberInFractionAfterDecimal {
    /**
     * Return the fraction output in the following way Examples: If after decimal,
     * repeating numbers are there in the output . eg. 1/3=0.333333333, this should
     * be represented as 0.(3) 6/11=0.54545454, this should be represented as 0.(54)
     * fractionRepresentation(1,2)=0.5 fractionRepresentation(1,3)=0.(3)
     * fractionRepresentation(6,11)=0.(54)
     */
    public static String fractionRepresentation(int num, int den) {
        if (den == 0) return "undefined";

        StringBuilder result = new StringBuilder();

        // Handle negative results
        if ((num < 0) ^ (den < 0)) result.append("-");
        num = Math.abs(num);
        den = Math.abs(den);

        // Integer part
        result.append(num / den).append(".");
        int remainder = num % den;

        if (remainder == 0) return result.toString().replace(".", ".0").equals(result.toString())
                ? result.append("0").toString()
                : result.toString();

        // Decimal part: map remainder -> position where it first appeared
        Map<Integer, Integer> remainderIndex = new LinkedHashMap<>();
        StringBuilder decimals = new StringBuilder();

        while (remainder != 0) {
            if (remainderIndex.containsKey(remainder)) {
                // Repeating block found — insert parentheses
                int repeatStart = remainderIndex.get(remainder);
                decimals.insert(repeatStart, "(");
                decimals.append(")");
                break;
            }
            remainderIndex.put(remainder, decimals.length());
            remainder *= 10;
            decimals.append(remainder / den);
            remainder %= den;
        }

        return result.append(decimals).toString();
    }
//    public static String fractionRepresentation(int num, int den) {
//        float d = (float) num / (float) den;
//        String number = String.valueOf(d);
//        String result = "";
//        String subString = number.substring(number.indexOf(".") + 1, number.length());
//        result = number.substring(0, number.indexOf(".") + 1);
//
//        String intermediateSubString = "";
//        int i = 0;
//        boolean repeated = false;
//        while (i < subString.length()) {
//            if (intermediateSubString.length() > 0 && (i + intermediateSubString.length() < subString.length())
//                    && subString.substring(i, i + intermediateSubString.length()).equals(intermediateSubString)) {
//                repeated = true;
//                break;
//
//            } else {
//                intermediateSubString = intermediateSubString + subString.charAt(i);
//                i++;
//            }
//        }
//
//        if (repeated) {
//            result = result + "(" + intermediateSubString + ")";
//        } else {
//            result = result + subString;
//        }
//        // System.out.println(result);
//        return result;
//    }

    public static void main(String args[]) {
        // float f=6/11f;
        // System.out.println(f);
        System.out.println(fractionRepresentation(1, 2) + " " + fractionRepresentation(1, 3) + " "
                + fractionRepresentation(6, 11));

        if (fractionRepresentation(1, 2).equals("0.5") && fractionRepresentation(6, 11).equals("0.(54)")
                && fractionRepresentation(1, 3).equals("0.(3)")) {
            System.out.println("All passed");
        } else {
            System.out.println("Failed");
        }

    }
}