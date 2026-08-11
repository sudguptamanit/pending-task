package org.example.April;

//| Metric | Value      |
//        | ------ | ---------- |
//        | Time   | ⭐ **O(n)** |
//        | Space  | ⭐ **O(1)** |

public class JosephusProblem {

    // Returns safe position (1-based index)
    public static int josephus(int n, int k) {
        int result = 0; // J(1, k) = 0 (0-based)

        for (int i = 2; i <= n; i++) {
            result = (result + k) % i;
        }

        return result + 1; // Convert to 1-based index
    }

    public static int josephus1(int n, int k) {
        if(n == 1)
            return 0;

        return (josephus1(n-1,k)+k)%n;
    }

    public static void main(String[] args) {
        int n = 14;
        int k = 2;

        System.out.println("Safe Position: " + josephus(n, k));
        System.out.println("Safe Position: " + Integer.parseInt(String.valueOf(josephus1(n, k)+1)));
    }
}