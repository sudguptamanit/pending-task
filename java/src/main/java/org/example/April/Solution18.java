package org.example.April;

public class Solution18 {

/**
 *
 * Given two arrays of integers, returns the dot product of the arrays
 *
 * ⚡ Complexity
 * Time: O(n)
 * Space: O(1)
 */

public static int dotProduct(int[] array1, int[] array2) {
    if (array1 == null || array2 == null || array1.length != array2.length) {
        throw new IllegalArgumentException("Arrays must be non-null and of same length");
    }

    int result = 0;

    for (int i = 0; i < array1.length; i++) {
        result += array1[i] * array2[i];
    }

    return result;
}

public static void main( String[] args ) {
    int[] array1 = { 1, 2 };
    int[] array2 = { 2, 3 };
    int result = dotProduct( array1, array2 );

    if( result == 8 ) {
        System.out.println( "Passed." );
        //return true;
    } else {
        System.out.println( "Failed." );
        //return false;
    }
}
}