package org.example.experiencegold;

//1) Minimum Number of Moves to Seat Everyone
//Problem Description
//You have n available seats and n students standing in a room. Each seat has a specific position given in the array seats where seats[i] represent the position of the i-th seat. Similarly, each student has a starting position given in the array of students where students[j] represents the position of the j-th student.
//
//You can move students to assign them to seats. In each move, you can:
//
//Move any student one position to the left or right (from position x to x+1 or x-1)
//Your goal is to assign each student to a seat such that:
//
//Every student sits in exactly one seat
//No two students share the same seat
//The total number of moves is minimized
//The problem asks you to return the minimum total number of moves required to achieve this arrangement.
//
//Important Notes:
//
//Multiple seats can initially be at the same position
//Multiple students can initially be at the same position
//Since there are exactly n seats and n students, a valid assignment is always possible
//Example: If seats = [3, 1, 5] and students = [2, 7, 4]:
//Ans: 4

//⏱️ Time Complexity
//O(n log n)
//Sorting dominates
//🧠 Space Complexity
//O(1)
//Ignoring sorting space (in-place sort)
//

import java.util.*;

public class MinimumSeat {



    public static int minMovesToSeat(int[] seats, int[] students) {
        Arrays.sort(seats);
        Arrays.sort(students);

        int moves = 0;

        for (int i = 0; i < seats.length; i++) {
            moves += Math.abs(seats[i] - students[i]);
        }

        return moves;
    }

    public static void main(String[] args) {
        int[] seats = {3, 1, 5};
        int[] students = {2, 7, 4};

        System.out.println(minMovesToSeat(seats, students)); // 4
    }
}