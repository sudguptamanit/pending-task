package org.example;

import java.util.*;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |


public class ElectionWinner {

    public static String findWinner(String[] votes) {
        Map<String, Integer> map = new HashMap<>();

        String winner = "";
        int maxVotes = 0;

        for (String vote : votes) {
            int count = map.getOrDefault(vote, 0) + 1;
            map.put(vote, count);

            // Update winner
            if (count > maxVotes) {
                maxVotes = count;
                winner = vote;
            } else if (count == maxVotes && vote.compareTo(winner) < 0) {
                winner = vote;
            }
        }

        return winner;
    }

    public static void main(String[] args) {
        String[] votes = {"john", "johnny", "jackie", "johnny", "john", "jackie", "jackie"};

        System.out.println(findWinner(votes)); // Output: jackie
    }
}