package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |

//new StringBuilder(str).reverse().toString();

public class ReverseString {

    public static String reverse(String str) {
        if (str == null || str.length() <= 1) return str;

        StringBuilder result = new StringBuilder();

        for (int i = str.length() - 1; i >= 0; i--) {
            result.append(str.charAt(i));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(reverse("hello")); // olleh
    }
}

//| Metric | Value                      |
//        | ------ | -------------------------- |
//        | Time   | **O(n)**                   |
//        | Space  | **O(1)** (ignoring output) |


//public class ReverseStringInPlace {
//
//    public static String reverse(String str) {
//        if (str == null) return null;
//
//        char[] arr = str.toCharArray();
//        int left = 0, right = arr.length - 1;
//
//        while (left < right) {
//            char temp = arr[left];
//            arr[left] = arr[right];
//            arr[right] = temp;
//
//            left++;
//            right--;
//        }
//
//        return new String(arr);
//    }
//
//    public static void main(String[] args) {
//        System.out.println(reverse("hello")); // olleh
//    }
//}
//
//// BUGGY VERSION
//public static String reverse(String str) {
//    String result = "";
//    for (int i = 0; i <= str.length(); i++) { // ❌ out of bounds
//        result += str.charAt(i);              // ❌ wrong direction
//    }
//    return result;
//}