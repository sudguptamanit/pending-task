package org.example.April;

public class Solution12 {

    /* Given base and integer exponent, compute value of base raised to the power of exponent.

    ⚡ Complexity
Time: O(log n) 🔥
Space: O(1)


     */
    public static double power(double base, int exp) {
        if (exp == 0) return 1;

        if (exp < 0) return 1 / power(base, -exp);

        double half = power(base, exp / 2);

        if (exp % 2 == 0) {
            return half * half;
        } else {
            return base * half * half;
        }
    }

    public static boolean doTestPass() {
        boolean testsPass = true;
        double result = power(2,2);
        return testsPass && result==4;
    }

    public static void main( String[] args ) {
        if(doTestPass()){
            System.out.println("Pass");
        }
        else{
            System.out.println("There are failures");
        }
    }
}