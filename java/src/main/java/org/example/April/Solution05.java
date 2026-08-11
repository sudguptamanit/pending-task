package org.example.April;

import java.util.*;

public class Solution05
{
    /**
     * Return an array containing prime numbers whose product is x
     * Examples:
     * primeFactorization( 6 ) == [2,3]
     * primeFactorization( 5 ) == [5]
     *
     * ✅ Key Idea (Optimized)
     * Remove all factors of 2 first
     * Then check only odd numbers up to √x
     * If anything remains (x > 1), it’s a prime
     *
     * ⚡ Complexity
     * Time: O(√n) ✅
     * Space: O(log n) (for storing factors)
     *
     */
    public static ArrayList<Integer> primeFactorization(int x)
    {
        ArrayList<Integer> result = new ArrayList<>();
        if (x <= 1) {
            return result; // no prime factors
        }
        // Handle factor 2 separately
        while (x % 2 == 0) {
            result.add(2);
            x /= 2;
        }
        // Check odd factors from 3 to sqrt(x)
        for (int i = 3; i * i <= x; i += 2) {
            while (x % i == 0) {
                result.add(i);
                x /= i;
            }
        }
        // If remaining x is prime
        if (x > 1) {
            result.add(x);
        }
        return result;
    }



    public static void main(String args[])
    {

        System.out.println(primeFactorization(6) + " " + primeFactorization(5));
        if(primeFactorization(6).equals(Arrays.asList(2,3))
                &&
                primeFactorization(5).equals(Arrays.asList(5))
        ) {
            System.out.println("All passed");
        }else {
            System.out.println("Failed");
        }

    }
}