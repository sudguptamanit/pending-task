package org.example;


//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |



public class CamelCaseConverter {

    public static String toCamelCase(String str) {
        if (str == null || str.trim().isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char ch : str.toCharArray()) {
            if (ch == ' ') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(ch));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(ch)); // normalize
                }
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String input = "hello world java programming";
        System.out.println(toCamelCase(input));
        // Output: HelloWorldJavaProgramming
    }
}