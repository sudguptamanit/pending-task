package org.example;

import java.util.*;

//| Step        | Complexity         |
//        | ----------- | ------------------ |
//        | Aggregation | O(n)               |
//        | Heap ops    | O(n log 10) ≈ O(n) |
//        | Total       | ⭐ **O(n)**         |


class Video {
    String name;
    int rate;

    Video(String name, int rate) {
        this.name = name;
        this.rate = rate;
    }
}

public class TopVideos {

    public static List<String> top10Videos(List<Video> list) {

        // Step 1: Aggregate watch rates
        Map<String, Integer> map = new HashMap<>();

        for (Video v : list) {
            map.put(v.name, map.getOrDefault(v.name, 0) + v.rate);
        }

        // Step 2: Min Heap (Top 10)
        PriorityQueue<Map.Entry<String, Integer>> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            minHeap.offer(entry);

            if (minHeap.size() > 10) {
                minHeap.poll(); // remove smallest
            }
        }

        // Step 3: Extract result
        List<String> result = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll().getKey());
        }

        // Reverse to get highest first
        Collections.reverse(result);

        return result;
    }

    public static void main(String[] args) {
        List<Video> list = Arrays.asList(
                new Video("abc", 10),
                new Video("def", 15),
                new Video("ghi", 10),
                new Video("abc", 12),
                new Video("xyz", 100)
        );

        System.out.println(top10Videos(list));//[xyz, abc, def, ghi]
    }
}