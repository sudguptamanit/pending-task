package org.example.code;

//| Metric | Value             |
//        | ------ | ----------------- |
//        | Time   | ⭐ **O(n)**        |
//        | Space  | **O(n)** (output) |


public class Solution {

    public static String rle(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int count = 1;

        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == input.charAt(i - 1)) {
                count++;
            } else {
                result.append(input.charAt(i - 1)).append(count);
                count = 1;
            }
        }

        // Append last character group
        result.append(input.charAt(input.length() - 1)).append(count);

        return result.toString();
    }

    public static void main(String[] args)  {

        if("".equals(rle("")) &&
                "a1".equals(rle("a")) &&
                "a3".equals(rle("aaa"))){
            System.out.println("Passed");
        } else {
            System.out.println("Failed");
        }
    }
}