package org.example.April;

/*
 ** Instructions to candidate.
 **  1) You are an avid rock collector who lives in southern California. Some rare
 **     and desirable rocks just became available in New York, so you are planning
 **     a cross-country road trip. There are several other rare rocks that you could
 **     pick up along the way.
 **
 **     You have been given a grid filled with numbers, representing the number of
 **     rare rocks available in various cities across the country.  Your objective
 **     is to find the optimal path from So_Cal to New_York that would allow you to
 **     accumulate the most rocks along the way.
 **
 **     Note: You can only travel either north (up) or east (right).
 **  2) Consider adding some additional tests in doTestsPass().
 **  3) Implement optimalPath() correctly.
 **  4) Here is an example:
 **                                                           ^
 **                 {{0,0,0,0,5}, New_York (finish)           N
 **                  {0,1,1,1,0},                         < W   E >
 **   So_Cal (start) {2,0,0,0,0}}                             S
 **                                                           v
 **   The total for this example would be 10 (2+0+1+1+1+0+5).
 */

import java.io.*;
import java.util.*;

class Solution28
{

    public static int optimalPath(Integer[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        // DP table
        int[][] dp = new int[rows][cols];
        // Start from SoCal (bottom-left)
        dp[rows - 1][0] = grid[rows - 1][0];
        // Fill first column (only upward movement possible)
        for (int i = rows - 2; i >= 0; i--) {
            dp[i][0] = dp[i + 1][0] + grid[i][0];
        }
        // Fill bottom row (only right movement possible)
        for (int j = 1; j < cols; j++) {
            dp[rows - 1][j] = dp[rows - 1][j - 1] + grid[rows - 1][j];
        }
        // Fill rest of grid
        for (int i = rows - 2; i >= 0; i--) {
            for (int j = 1; j < cols; j++) {
                dp[i][j] = grid[i][j] + Math.max(dp[i + 1][j], dp[i][j - 1]);
            }
        }
        // Destination (New York = top-right)
        return dp[0][cols - 1];
    }


    public static boolean doTestsPass()
    {
        boolean result = true;
        result &= optimalPath(new Integer[][]{{0,0,0,0,5},
                {0,1,1,1,0},
                {2,0,0,0,0}}) == 10;
        return result;
    }

    public static void main(String[] args)
    {
        if(doTestsPass())
        {
            System.out.println("All tests pass");
        }
        else
        {
            System.out.println("Tests fail.");
        }
    }
}