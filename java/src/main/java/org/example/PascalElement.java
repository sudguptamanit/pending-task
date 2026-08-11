package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(r)** |
//        | Space  | **O(1)** |
//

public class PascalElement {

    public static int getElement(int row, int col) {
        // Convert to 0-based index
        int n = row - 1;
        int r = col - 1;

        // nCr
        long res = 1;

        // Since C(n, r) == C(n, n-r)
        r = Math.min(r, n - r);

        for (int i = 0; i < r; i++) {
            res = res * (n - i);
            res = res / (i + 1);
        }

        return (int) res;
    }

    public static void main(String[] args) {
        int row = 5, col = 2;
        System.out.println(getElement(row, col)); // 4
    }
}
