package org.example.code;

//        Time        O(n×m) — every cell visited exactly once
//        Space       O(n×m) — dp table; reducible to O(m) with a single row

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

class OptimalPath
{

    public static Integer optimalPath(Integer[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;

        int rows = grid.length;
        int cols = grid[0].length;
        int[][] dp = new int[rows][cols];

        // Start is bottom-left: grid[rows-1][0]
        // End   is top-right:   grid[0][cols-1]
        // Movement: up (row-1) or right (col+1)

        // Base: starting cell
        dp[rows - 1][0] = grid[rows - 1][0];

        // Fill bottom row (can only move right)
        for (int c = 1; c < cols; c++) {
            dp[rows - 1][c] = dp[rows - 1][c - 1] + grid[rows - 1][c];
        }

        // Fill rightmost column (can only move up)
        for (int r = rows - 2; r >= 0; r--) {
            dp[r][0] = dp[r + 1][0] + grid[r][0];
        }

        // Fill rest: best of coming from below or from the left
        for (int r = rows - 2; r >= 0; r--) {
            for (int c = 1; c < cols; c++) {
                dp[r][c] = grid[r][c] + Math.max(dp[r + 1][c],   // came from below (moved up)
                        dp[r][c - 1]);  // came from left  (moved right)
            }
        }

        return dp[0][cols - 1]; // top-right = New York
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