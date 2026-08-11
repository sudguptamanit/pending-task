package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(1)** |

public class LastIndex {

    public static int lastIndexOfChar(String str, char x) {
        if (str == null || str.length() == 0) return -1;

        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == x) {
                return i;
            }
        }

        return -1; // not found
    }

    public static void main(String[] args) {
        String str = "hello world";
        char x = 'o';

        System.out.println(lastIndexOfChar(str, x)); // 7
    }
}