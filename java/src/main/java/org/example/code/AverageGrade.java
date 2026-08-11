package org.example.code;


//  Find the best average grade.
//  Given a list of student test scores
//  Each student may have more than one test score in the list.
//⏱️ Complexity
//        Time: O(n)
//        Space: O(n) (for maps)

import java.util.HashMap;
import java.util.Map;

class AverageGrade
{
    public static Integer bestAvgGrade(String[][] scores)
    {
        if (scores == null || scores.length == 0) return 0;

        Map<String, Integer> sumMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        // Step 1: Aggregate scores
        for (String[] entry : scores)
        {
            String name = entry[0];
            int score = Integer.parseInt(entry[1]);

            sumMap.put(name, sumMap.getOrDefault(name, 0) + score);
            countMap.put(name, countMap.getOrDefault(name, 0) + 1);
        }

        // Step 2: Compute best average
        int maxAvg = Integer.MIN_VALUE;

        for (String name : sumMap.keySet())
        {
            int avg = sumMap.get(name) / countMap.get(name); // integer division (floor)
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