package org.example;

//| Metric | Value                              |
//        | ------ | ---------------------------------- |
//        | Time   | **O(m × n)**                       |
//        | Space  | **O(min(m, n))** (recursion stack) |


public class ReverseSpiralMatrix {

    public static void reverseSpiral(int[][] matrix) {
        printReverseSpiral(matrix, 0, matrix.length - 1, 0, matrix[0].length - 1);
    }

    private static void printReverseSpiral(int[][] mat, int top, int bottom, int left, int right) {

        // Base condition
        if (top > bottom || left > right) return;

        // Step 1: Go inside first (recursion)
        printReverseSpiral(mat, top + 1, bottom - 1, left + 1, right - 1);

        // Step 2: Print current layer (outer after inner)

        // Top row
        for (int i = left; i <= right; i++) {
            System.out.print(mat[top][i] + " ");
        }

        // Right column
        for (int i = top + 1; i <= bottom; i++) {
            System.out.print(mat[i][right] + " ");
        }

        // Bottom row
        if (top < bottom) {
            for (int i = right - 1; i >= left; i--) {
                System.out.print(mat[bottom][i] + " ");
            }
        }

        // Left column
        if (left < right) {
            for (int i = bottom - 1; i > top; i--) {
                System.out.print(mat[i][left] + " ");
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        reverseSpiral(matrix);
        // Output: 5 6 9 8 7 4 1 2 3
    }
}