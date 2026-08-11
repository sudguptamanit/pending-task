package org.example;

//(O(n) time, O(1) extra space)

public class StringCompression {

    public static String compress(String str) {
        if (str == null || str.length() == 0) return "";

        StringBuilder result = new StringBuilder();
        int count = 1;

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            } else {
                result.append(count).append(str.charAt(i - 1));
                count = 1; // reset count
            }
        }

        // Append last character group
        result.append(count).append(str.charAt(str.length() - 1));

        return result.toString();
    }

    public static void main(String[] args) {
        String input = "SSSSSTTPPQ";
        System.out.println(compress(input)); // 5S2T2P1Q
    }
}