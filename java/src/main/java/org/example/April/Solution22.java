package org.example.April;

//Implement the 'walk' method.
//Assume robot inital position is (0,0).
//walk method take path where each character corresponds to a movement of the robot.
// The robot moves up, down, left, and right represented by the characters 'U', 'D', 'L', and 'R'
// Ignore other characters.

//✅ Key Idea
//        Start at (0,0)
//        For each character:
//        'U' → y++
//        'D' → y--
//        'L' → x--
//        'R' → x++
//        Ignore everything else

//⚡ Complexity
//        Time: O(n)
//        Space: O(1)


import java.util.Arrays;

public class Solution22 {

    // your code
    public static Integer[] walk(String path) {
        int x = 0, y = 0;
        if (path == null || path.length() == 0) {
            return new Integer[]{0, 0};
        }
        for (char ch : path.toCharArray()) {
            switch (ch) {
                case 'U':
                    y++;
                    break;
                case 'D':
                    y--;
                    break;
                case 'L':
                    x--;
                    break;
                case 'R':
                    x++;
                    break;
                default:
                    // ignore other characters
            }
        }
        return new Integer[]{x, y};
    }

    public static boolean isEqual(Integer[] a, Integer[] b) {
        return Arrays.equals(a, b);
    }

    public static boolean pass() {
        boolean res = true;

        {
            String test = "UUU";
            Integer[] result = walk(test);
            res &= isEqual(result, new Integer[]{0, 3});
        }

        {
            String test = "ULDR";
            Integer[] result = walk(test);
            res &= isEqual(result, new Integer[]{0, 0});
        }

        {
            String test = "ULLLDUDUURLRLR";
            Integer[] result = walk(test);
            res &= isEqual(result, new Integer[]{-2, 2});
        }

        {
            String test = "UP LEFT 2xDOWN DOWN RIGHT RIGHT UP UP";
            Integer[] result = walk(test);
            res &= isEqual(result, new Integer[]{1, 1});
        }

        return res;
    }

    public static void main(String[] args) {
        if(pass()){
            System.out.println("Pass");
        } else {
            System.out.println("Test failures");
        }
    }
}