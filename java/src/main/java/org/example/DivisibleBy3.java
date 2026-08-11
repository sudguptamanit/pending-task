package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(1)** |
//

public class DivisibleBy3 {

    public static boolean isDivisibleBy3(int[] digits) {
        int sum = 0;

        for (int d : digits) {
            sum += d;
        }

        return sum % 3 == 0;
    }

    public static void main(String[] args) {
        int[] digits = {1, 4, 5}; // sum = 10

        if (isDivisibleBy3(digits)) {
            System.out.println("Possible to form divisible by 3 number");
        } else {
            System.out.println("Not possible");
        }
    }
}