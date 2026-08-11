package org.example.April;

import java.util.*;


//Complexity
//Time            O(N log N)      TreeSet insertions are O(log N) each, N elements
//Space           O(N)        freqMap + freqToElements together hold all N nodes
import java.util.*;
import java.util.stream.*;

//arr
// │
//         ▼
//         Arrays.stream(arr).boxed()
// │   int[] → Stream<Integer>
// │
//         ▼
//         .collect(groupingBy(n -> n, counting()))
//        │   → Map<Integer, Long>  { 2→1, 1→2, 3→3, 4→2, 5→3, 6→1, 8→4 }
// │
//         ▼
//         .entrySet().stream()
// │
//         ▼
//         .collect(groupingBy(freq, TreeMap::new, toCollection(TreeSet::new)))
//        │   → TreeMap<Long, TreeSet<Integer>>
// │     { 1→{2,6},  2→{1,4},  3→{3,5},  4→{8} }
//         │       ↑ sorted freq      ↑ sorted elements
// │
//         ▼
//         .entrySet().stream().flatMap(entry -> ...)
//        │   freq==1 → stream all elements {2, 6}
// │   freq >1 → stream only smallest {1}, {3}, {8}
//        │
//        ▼
//        .collect(toList())
//        │
//        ▼
//        [2, 6, 1, 3, 8] ✅

public class FrequencyFilter {

    public static List<Integer> filterByFrequency(int[] arr) {
        // Step 1: Count frequencies — O(N)
        Map<Integer, Integer> freqMap = new LinkedHashMap<>();
        for (int num : arr) {
            freqMap.merge(num, 1, Integer::sum);
        }

        // Step 2: Group elements by frequency — O(N)
        // TreeMap: keys (frequencies) auto-sorted ascending
        // TreeSet: values (elements) auto-sorted ascending
        Map<Integer, TreeSet<Integer>> freqToElements = new TreeMap<>();
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            freqToElements
                    .computeIfAbsent(entry.getValue(), k -> new TreeSet<>())
                    .add(entry.getKey());
        }

        // Step 3: Build result — O(N log N) due to TreeSet insertions
        // Rule: if a frequency bucket has multiple elements, keep only the smallest
        // Exception: frequency == 1 bucket keeps ALL elements (sorted)
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, TreeSet<Integer>> entry : freqToElements.entrySet()) {
            int freq = entry.getKey();
            TreeSet<Integer> elements = entry.getValue();

            if (freq == 1) {
                result.addAll(elements);        // add all freq-1 elements sorted
            } else {
                result.add(elements.first());   // only smallest among same-freq
            }
        }

        return result;
    }

    public static List<Integer> filterByFrequency1(int[] arr) {
        // Step 1: Count frequencies — O(N)
        Map<Integer, Long> freqMap = Arrays.stream(arr)
                .boxed()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        // Step 2: Group by frequency, then apply rules and flatten — O(N log N)
        return freqMap.entrySet().stream()
                // Group entries by frequency value
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,                          // key = frequency
                        TreeMap::new,                                 // sorted by frequency ascending
                        Collectors.mapping(Map.Entry::getKey, Collectors.toCollection(TreeSet::new)) // sorted elements
                ))
                .entrySet().stream()
                .flatMap(entry -> {
                    long freq = entry.getKey();
                    TreeSet<Integer> elements = entry.getValue();
                    // freq==1: keep all sorted; otherwise keep only smallest
                    return freq == 1
                            ? elements.stream()
                            : Stream.of(elements.first());
                })
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
        int[] arr1 = {2, 1, 3, 4, 1, 5, 5, 5, 3, 4, 6, 3, 8, 8, 8, 8};
        System.out.println(filterByFrequency(arr1)); // [2, 6, 1, 3, 8]

        int[] arr2 = {2, 1, 6, 1, 2};
        System.out.println(filterByFrequency(arr2)); // [6, 1]

        int[] arr = {2, 1, 3, 4, 1, 5, 5, 5, 3, 4, 6, 3, 8, 8, 8, 8};
        System.out.println(filterByFrequency1(arr)); // [2, 6, 1, 3, 8]

        int[] arrr = {2, 1, 6, 1, 2};
        System.out.println(filterByFrequency1(arrr)); // [6, 1]
    }
}