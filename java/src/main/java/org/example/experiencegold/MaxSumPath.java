package org.example.experiencegold;

//⏱️ Time Complexity
//O(N × M) → visit each cell once
//🧠 Space Complexity
//O(M) → optimized from O(N×M) to single row
//
public class MaxSumPath {

//    1.	Maximum sum path in a matrix from top-left to bottom-right
//    Given a matrix mat[][] of dimensions N * M, the task is to find the path from the top-left cell (0, 0) to the bottom-right cell (N - 1, M - 1) of the given matrix such that sum of the elements in the path is maximum. The only moves allowed from any cell (i, j) of the matrix are (i + 1, j) or (i, j + 1).
//    Examples:
//    Input: mat[][] = {{3, 7}, {9, 8}}
//    Output: 20
//    Explanation:
//    Path with maximum sum is 3 => 9 => 8 as 20.
//    Input: mat[][] = {{1, 2}, {3, 5}}
//    Output: 9
//    Explanation:
//    Path with maximum sum is 1 => 3 => 5 as 9


//    ✅ Optimal Approach: Dynamic Programming (Grid DP)
//
//    We can only move:
//
//    Right → (i, j+1)
//    Down → (i+1, j)
//
//            👉 So, at each cell:
//
//    dp[i][j] = mat[i][j] + max(dp[i-1][j], dp[i][j-1])
//

    static int MaximumPath(int [][]grid)
    {
        // Dimensions of grid[][]
        int N = grid.length;
        int M = grid[0].length;

        // Stores maximum sum at each cell
        // sum[i][j] from cell sum[0][0]
        int [][]sum = new int[N + 1][M + 1];

        // Iterate to compute the maximum
        // sum path in the grid
        for (int i = 1; i <= N; i++)
        {
            for (int j = 1; j <= M; j++)
            {
                // Update the maximum path sum
                sum[i][j] = Math.max(sum[i - 1][j],
                        sum[i][j - 1]) +
                        grid[i - 1][j - 1];
            }
        }

        // Return the maximum sum
        return sum[N][M];
    }
        public static int maxSumPath(int[][] mat) {
            if (mat == null || mat.length == 0) return 0;

            int n = mat.length;
            int m = mat[0].length;

            // Use 1D DP for space optimization
            int[] dp = new int[m];

            // Initialize first cell
            dp[0] = mat[0][0];

            // First row
            for (int j = 1; j < m; j++) {
                dp[j] = dp[j - 1] + mat[0][j];
            }

            // Fill remaining rows
            for (int i = 1; i < n; i++) {
                dp[0] += mat[i][0]; // first column

                for (int j = 1; j < m; j++) {
                    dp[j] = mat[i][j] + Math.max(dp[j], dp[j - 1]);
                }
            }

            return dp[m - 1];
        }

        public static void main(String[] args) {
            int[][] mat1 = {{3, 7}, {9, 8}};
            int[][] mat2 = {{1, 2}, {3, 5}};

            System.out.println(maxSumPath(mat1)); // 20
            System.out.println(maxSumPath(mat2)); // 9

            System.out.println(MaximumPath(mat1)); // 20
            System.out.println(MaximumPath(mat2));
        }
    }

