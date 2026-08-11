package org.example;


//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(1)** |



public class SumOfProductPairs {

    public static int sumOfProducts(int[] arr) {
        int sum = 0;
        int sumOfSquares = 0;

        for (int num : arr) {
            sum += num;
            sumOfSquares += num * num;
        }

        return (sum * sum - sumOfSquares) / 2;
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 4};
        System.out.println(sumOfProducts(arr)); // 19
    }
}