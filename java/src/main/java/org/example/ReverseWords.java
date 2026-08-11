package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |
//

public class ReverseWords {

    public static String reverseWords(String str) {
        if (str == null || str.trim().isEmpty()) return str;

        String[] words = str.trim().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = words.length - 1; i >= 0; i--) {
            result.append(words[i]);
            if (i != 0) result.append(" ");
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String input = "I AM A TESTER";
        System.out.println(reverseWords(input)); // TESTER A AM I
    }
}

//public class ReverseWordsInPlace {
//
//    public static String reverseWords(String s) {
//        char[] arr = s.toCharArray();
//
//        // Step 1: Reverse whole string
//        reverse(arr, 0, arr.length - 1);
//
//        // Step 2: Reverse each word
//        int start = 0;
//        for (int end = 0; end <= arr.length; end++) {
//            if (end == arr.length || arr[end] == ' ') {
//                reverse(arr, start, end - 1);
//                start = end + 1;
//            }
//        }
//
//        return new String(arr);
//    }
//
//    private static void reverse(char[] arr, int left, int right) {
//        while (left < right) {
//            char temp = arr[left];
//            arr[left++] = arr[right];
//            arr[right--] = temp;
//        }
//    }
//
//    public static void main(String[] args) {
//        System.out.println(reverseWords("I AM A TESTER"));
//    }
//}