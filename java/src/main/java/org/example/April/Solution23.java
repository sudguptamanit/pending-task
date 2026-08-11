package org.example.April;

public class Solution23
{

    // Takes a string str and returns the int value represented by
    // the string.
//    ⚡ Complexity
//    Time: O(n)
//    Space: O(1)
    //For example, atoi("42") returns 42.

    public static int atoi(String str)
    {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Invalid input");
        }
        int i = 0;
        int sign = 1;
        int result = 0;
        if (str.charAt(i) == '-') {
            sign = -1;
            i++;
        } else if (str.charAt(i) == '+') {
            i++;
        }
        while (i < str.length()) {
            char ch = str.charAt(i);
            if (ch < '0' || ch > '9') {
                throw new IllegalArgumentException("Invalid character: " + ch);
            }
            int digit = ch - '0';
            result = result * 10 + digit;
            i++;
        }
        return result * sign;
    }


    public static void main(String[] args)
    {
        System.out.println(atoi("42"));     // 42
        System.out.println(atoi("-123"));   // -123
        System.out.println(atoi("+56"));    // 56
    }
}