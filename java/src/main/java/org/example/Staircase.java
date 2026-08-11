package org.example;

//| Metric | Value     |
//        | ------ | --------- |
//        | Time   | **O(n²)** |
//        | Space  | **O(1)**  |


public class Staircase {

    public static void printStaircase(int n) {
        for (int i = 0; i < n; i++) {

            // Print spaces
            for (int j = 0; j < n - i - 1; j++) {
                System.out.print(" ");
            }

            // Print '#'
            for (int j = 0; j <= i; j++) {
                System.out.print("#");
            }

            System.out.println(); // next line
        }
    }

    public static void main(String[] args) {
        int n = 5;
        printStaircase(n);

//    #
//   ##
//  ###
// ####
//#####

    }
}