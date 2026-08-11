package org.example.April;

//  Find the best average grade.
//  Given a list of student test scores
//  Each student may have more than one test score in the list.
//
//⚡ Complexity
//        Time: O(n)
//        Space: O(n)

import java.util.*;

class Solution24
{
    public static Integer bestAvgGrade(String[][] scores)
    {
        if (scores == null || scores.length == 0) return 0;
        // name -> [sum, count]
        Map<String, int[]> map = new HashMap<>();
        for (String[] entry : scores) {
            String name = entry[0];
            int score = Integer.parseInt(entry[1]);

            map.putIfAbsent(name, new int[2]);
            map.get(name)[0] += score; // sum
            map.get(name)[1] += 1;     // count
        }

        int maxAvg = Integer.MIN_VALUE;

        for (int[] val : map.values()) {
            int avg = val[0] / val[1]; // integer division
            maxAvg = Math.max(maxAvg, avg);
        }

        return maxAvg;
    }

    public static boolean pass()
    {
        String[][] s1 = { { "Rohan", "84" },
                { "Sachin", "102" },
                { "Ishan", "55" },
                { "Sachin", "18" } };

        return bestAvgGrade(s1) == 84;
    }

    public static void main(String[] args)
    {
        if(pass())
        {
            System.out.println("Pass");
        }
        else
        {
            System.out.println("Some Fail");
        }
    }
}
