package org.example.code;

//Time        O(log₁₀n)
//Space       O(1)

public class PowerTen {

    public static boolean isPowerOf10(int x) {
        if (x <= 0) return false;
        while (x % 10 == 0) {
            x /= 10;
        }
        return x == 1;
    }

    public static boolean doTestsPass() {
        int[] isPowerList    = {1, 10, 100, 1000, 10000, 1000000000};
        int[] isNotPowerList = {0, 3, -10, 11, 99, 200, 500, Integer.MAX_VALUE};

        for (int i : isPowerList) {
            if (!isPowerOf10(i)) {
                System.out.println("Test failed for: " + i);
                return false;
            }
        }

        for (int i : isNotPowerList) {
            if (isPowerOf10(i)) {
                System.out.println("Test failed for: " + i);
                return false;
            }
        }

        System.out.println("All tests passed.");
        return true;
    }

    public static void main(String[] args) {
        doTestsPass();
    }
}