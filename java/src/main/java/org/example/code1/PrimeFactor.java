package org.example.code1;

import java.util.*;

//⏱️ Time Complexity
//O(√n) → checking up to √x
//🧠 Space Complexity
//O(k) → number of prime factors
//

public class PrimeFactor {

    /**
     * Return an array containing prime numbers whose product is x
     */
    public static ArrayList<Integer> primeFactorization(int x) {
        ArrayList<Integer> result = new ArrayList<>();

        if (x <= 1) return result;

        // Step 1: handle factor 2
        while (x % 2 == 0) {
            result.add(2);
            x /= 2;
        }

        // Step 2: check odd factors up to sqrt(x)
        for (int i = 3; i * i <= x; i += 2) {
            while (x % i == 0) {
                result.add(i);
                x /= i;
            }
        }

        // Step 3: if remaining x is prime
        if (x > 1) {
            result.add(x);
        }

        return result;
    }

    public static void main(String args[]) {

        System.out.println(primeFactorization(6) + " " + primeFactorization(5));

        if (primeFactorization(6).equals(Arrays.asList(2, 3))
                && primeFactorization(5).equals(Arrays.asList(5))) {
            System.out.println("All passed");
        } else {
            System.out.println("Failed");
        }
    }
}