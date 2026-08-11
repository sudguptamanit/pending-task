package org.example.April;


/*
 **  Below formation is called Pascals Triangle.
 **
 **  Example:
 **               1
 **              1 1
 **             1 2 1
 **            1 3 3 1
 **           1 4 6 4 1
 **         1 5 10 10 5 1
 **        1 6 15 20 15 6 1
 **
 **  Complete the 'pascal' function below so that given a
 **  col and a row it will return the value in that positon.
 **
 **  Example, pascal(1,2) should return 2
 **
 */

/*
 *					******** IMPORTANT ********
 *
 * THIS IS SAMPLE SOLUTION. IF YOU FIND BETTER SOLUTION PLEASE CONSIDER USING SAME.
 * USE YOUR OWN VARIABLE NAMES - @@@ DO NOT COPY @@@ EXACT VARIABLE NAMES
 *
 *
 * ⚡ Complexity
Time: O(col) 🔥
Space: O(1)
 */
import java.util.HashMap;
import java.util.Map;

public class Solution13 {

    public static int pascal(int col, int row) {
        if (col < 0 || col > row) return 0;

        // symmetry optimization: C(n, k) = C(n, n-k)
        col = Math.min(col, row - col);

        long result = 1;

        for (int i = 1; i <= col; i++) {
            result = result * (row - i + 1) / i;
        }

        return (int) result;
    }

    public static void main(String[] args) {
        if(Solution13.pascal(0,0) ==  1 &&
                Solution13.pascal(1,2) ==  2 &&
                Solution13.pascal(5,6) ==  6 &&
                Solution13.pascal(4,8) ==  70 &&
                Solution13.pascal(6,6) ==  1) {
            System.out.println("Pass");
        }else {
            System.out.println("Failed");
        }
    }
}