package org.example;


//| Metric | Value                           |
//        | ------ | ------------------------------- |
//        | Time   | **O(d)** (d = number of digits) |
//        | Space  | **O(1)**                        |



public class ArmstrongNumber {

    public static boolean isArmstrong(int num) {
        int original = num;
        int sum = 0;

        while (num > 0) {
            int digit = num % 10;
            sum += digit * digit * digit; // cube
            num /= 10;
        }

        return sum == original;
    }

    public static void main(String[] args) {
        int n = 371;

        if (isArmstrong(n)) {
            System.out.println(n + " is an Armstrong number");
        } else {
            System.out.println(n + " is NOT an Armstrong number");
        }
    }
}
