package org.example.code1;

//⏱️ Time Complexity
//O(k) where k = min(col, row - col)
//Much better than building full triangle (O(n²))
//        🧠 Space Complexity
//O(1) → No extra storage used
//

public class PascalTraingle {

    public static int pascal(int col, int row) {
        // Edge cases
        if (col < 0 || col > row) return 0;
        if (col == 0 || col == row) return 1;

        // Use symmetry: C(n, k) = C(n, n-k)
        col = Math.min(col, row - col);

        long result = 1;

        for (int i = 1; i <= col; i++) {
            result = result * (row - i + 1) / i;
        }

        return (int) result;
    }

    public static void main(String[] args) {
        if (PascalTraingle.pascal(0,0) ==  1 &&
                PascalTraingle.pascal(1,2) ==  2 &&
                PascalTraingle.pascal(5,6) ==  6 &&
                PascalTraingle.pascal(4,8) ==  70 &&
                PascalTraingle.pascal(6,6) ==  1) {
            System.out.println("Pass");
        } else {
            System.out.println("Failed");
        }
    }
}