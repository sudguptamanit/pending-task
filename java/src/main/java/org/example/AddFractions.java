package org.example;


//a/b + c/d = (a*d + c*b) / (b*d)
//
//Then simplify by dividing numerator & denominator by their GCD.
//
//Example:
//        1/3 + 1/6 = (1*6 + 1*3) / (3*6)
//        = (6 + 3) / 18
//        = 9/18
//        = 1/2  ← simplified by GCD(9,18)=9
//
//
//Step 1 — Cross multiply:
//numerator   = (a * d) + (c * b)
//denominator = b * d
//
//Step 2 — Simplify using GCD:
//gcd         = GCD(numerator, denominator)
//numerator   = numerator / gcd
//        denominator = denominator / gcd
//
//Step 3 — Handle negatives:
//        if denominator < 0 → flip both signs
//e.g: -1/-2 → 1/2
//
//
//a=1, b=3, c=1, d=6
//
//Step 1 — Cross multiply:
//numerator   = (1 * 6) + (1 * 3) = 6 + 3 = 9
//denominator = 3 * 6              = 18
//
//Step 2 — Simplify:
//GCD(9, 18):
//        18 % 9 = 0 → GCD = 9
//numerator   = 9  / 9 = 1
//denominator = 18 / 9 = 2
//
//Step 3 — denominator=2 > 0 → no sign flip needed
//
//Result = 1/2 ✅
//
//GCD(9, 18):
//a=9,  b=18 → temp=18, b=9%18=9,  a=18  ← wait, abs first
//a=18, b=9  → temp=9,  b=18%9=0,  a=9
//b=0 → return a=9 ✅
//
//GCD(62, 63):            ← for 3/7 + 5/9
//a=62, b=63 → b=62%63=62...
//a=63, b=62 → b=63%62=1
//a=62, b=1  → b=62%1=0
//b=0 → return 1  (no simplification possible) ✅


public class AddFractions {

    // ── Core: GCD via Euclidean algorithm ──────────────────────
    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // ── Fraction represented as int[]{numerator, denominator} ──
    public static int[] addFractions(int a, int b, int c, int d) {
        if (b == 0 || d == 0)
            throw new IllegalArgumentException("Denominator cannot be zero");

        // Step 1 — Add fractions: a/b + c/d = (a*d + c*b) / (b*d)
        int numerator   = (a * d) + (c * b);
        int denominator = b * d;

        // Step 2 — Simplify using GCD
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        numerator   /= gcd;
        denominator /= gcd;

        // Step 3 — Normalize sign (denominator always positive)
        if (denominator < 0) {
            numerator   = -numerator;
            denominator = -denominator;
        }

        return new int[]{numerator, denominator};
    }

    // ── Pretty print helper ─────────────────────────────────────
    private static String format(int[] f) {
        System.out.println(f[0] + " and "+f[1]);
        if (f[1] == 1) return String.valueOf(f[0]);  // whole number
        return f[0] + "/" + f[1];
    }

    public static void main(String[] args) {
        // Test 1 — basic
        int[] r1 = addFractions(1, 3, 1, 6);
        System.out.println("1/3 + 1/6 = " + format(r1));       // 1/2

        // Test 2 — same denominator
        int[] r2 = addFractions(1, 4, 3, 4);
        System.out.println("1/4 + 3/4 = " + format(r2));       // 1

        // Test 3 — negative fraction
        int[] r3 = addFractions(-1, 3, 1, 3);
        System.out.println("-1/3 + 1/3 = " + format(r3));      // 0/1 → 0

        // Test 4 — result is whole number
        int[] r4 = addFractions(1, 2, 1, 2);
        System.out.println("1/2 + 1/2 = " + format(r4));       // 1

        // Test 5 — large numbers
        int[] r5 = addFractions(3, 7, 5, 9);
        System.out.println("3/7 + 5/9 = " + format(r5));       // 62/63

        // Test 6 — one fraction is zero
        int[] r6 = addFractions(0, 5, 3, 7);
        System.out.println("0/5 + 3/7 = " + format(r6));       // 3/7

        // Test 7 — negative denominator
        int[] r7 = addFractions(1, -3, 1, 6);
        System.out.println("1/-3 + 1/6 = " + format(r7));      // -1/6
    }
}