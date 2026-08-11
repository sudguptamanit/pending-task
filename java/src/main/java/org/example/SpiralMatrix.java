package org.example;

import java.util.*;

//Complexity
//TimeO(m×n) — every element visited exactly onceSpaceO(1) — no extra space (output list excluded)
//
//top, bottom, left, right
//
//Go RIGHT  → shrink top    (top++)
//Go DOWN   → shrink right  (right--)
//Go LEFT   → shrink bottom (bottom--)
//Go UP     → shrink left   (left++)
//
//Repeat until boundaries cross.
//
//
//Matrix:
//        1   2   3   4
//        5   6   7   8
//        9  10  11  12
//        13  14  15  16
//
//Step 1 → RIGHT along top row:      1  2  3  4       top++  → top=1
//Step 2 → DOWN along right col:     8 12 16          right-- → right=2
//Step 3 → LEFT along bottom row:   15 14 13          bottom--→ bottom=2
//Step 4 → UP along left col:        9  5             left++ → left=1
//Step 5 → RIGHT along new top:      6  7             top++  → top=2
//Step 6 → DOWN along new right:    11                right-- → right=1
//Step 7 → LEFT along new bottom:   10                bottom--→ bottom=1
//Step 8 → boundaries cross → STOP
//
//Output: 1 2 3 4 8 12 16 15 14 13 9 5 6 7 11 10 ✅
//
//
//Matrix:         Boundaries: top=0 bottom=2 left=0 right=2
//        1  2  3
//        4  5  6
//        7  8  9
//
//        ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//Iteration 1:
//        ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//        → RIGHT  row=0, col=0→2 : add 1,2,3     top=1
//        ↓ DOWN   col=2, row=1→2 : add 6,9       right=1
//        ← LEFT   row=2, col=1→0 : add 8,7       bottom=1
//        ↑ UP     col=0, row=1→1 : add 4         left=1
//
//        ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//Iteration 2:  top=1 bottom=1 left=1 right=1
//        ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//        → RIGHT  row=1, col=1→1 : add 5         top=2
//        ↓ DOWN   col=1, row=2→1 : (empty)       right=0
//
//top(2) > bottom(1) → skip LEFT
//left(1) > right(0) → skip UP
//
//━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//top(2) > bottom(1) → EXIT LOOP
//━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//
//Result: [1,2,3,6,9,8,7,4,5] ✅
//

public class SpiralMatrix {

    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();

        if (matrix == null || matrix.length == 0)
            return result;

        int top    = 0;
        int bottom = matrix.length - 1;
        int left   = 0;
        int right  = matrix[0].length - 1;
//        System.out.println(bottom +"  "+right);
//        System.out.println(matrix.length +"  "+matrix[0].length);
        while (top <= bottom && left <= right) {

            // 1. Move RIGHT across top row
            for (int col = left; col <= right; col++)
                result.add(matrix[top][col]);
            top++;

            // 2. Move DOWN along right column
            for (int row = top; row <= bottom; row++)
                result.add(matrix[row][right]);
            right--;

            // 3. Move LEFT across bottom row (if still valid)
            if (top <= bottom) {
                for (int col = right; col >= left; col--)
                    result.add(matrix[bottom][col]);
                bottom--;
            }

            // 4. Move UP along left column (if still valid)
            if (left <= right) {
                for (int row = bottom; row >= top; row--)
                    result.add(matrix[row][left]);
                left++;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Test 1 — 4x4 matrix
        int[][] m1 = {
                { 1,  2,  3,  4},
                { 5,  6,  7,  8},
                { 9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        //System.out.println(spiralOrder(m1));
        // [1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10]

        // Test 2 — 3x3 matrix
        int[][] m2 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        //System.out.println(spiralOrder(m2));
        // [1,2,3,6,9,8,7,4,5]

        // Test 3 — single row
        int[][] m3 = {{1, 2, 3, 4}};
        //System.out.println(spiralOrder(m3));
        // [1,2,3,4]

        // Test 4 — single column
        int[][] m4 = {{1},{2},{3},{4}};
        //System.out.println(spiralOrder(m4));
        // [1,2,3,4]

        // Test 5 — 3x5 rectangle
        int[][] m5 = {
                { 1,  2,  3,  4,  5},
                { 6,  7,  8,  9, 10},
                {11, 12, 13, 14, 15}
        };
        System.out.println(spiralOrder(m5));
        // [1,2,3,4,5,10,15,14,13,12,11,6,7,8,9]
    }
}