package org.example.April;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;


public class Index {
    public static List<Integer> findPrefixIndices(String text, String prefix) {
        List<Integer> indices = new ArrayList<>();

        // Handle empty prefix to avoid infinite loops
        if (prefix == null || prefix.isEmpty()) {
            return indices;
        }

        int index = text.indexOf(prefix);
        while (index >= 0) {
            indices.add(index);
            // Search for the next occurrence starting after the current one
            index = text.indexOf(prefix, index + 1);
        }

        return indices;
    }
    public static void main(String[] args) {
        String text = "apple banana apple cherry apple";
        String prefix = "apple";

        List<Integer> result = findPrefixIndices(text, prefix);
        System.out.println("Indices of '" + prefix + "': " + result);
        // Output: Indices of 'apple': [0, 13, 26]
        String name = "rohitroh";
        Map<String, Long> characterFrequency = Arrays.stream(name.split(""))
                .collect(groupingBy(Function.identity(), counting()));
        System.out.println(characterFrequency);


        Map<Character, Long> collected = name.chars()
                .mapToObj(ch -> (char) ch)
                .collect(groupingBy(Function.identity(), counting()));
        System.out.println(collected);

        Map<String, Integer> countCharacter = Arrays.stream(name.split(""))
                .collect(groupingBy(Function.identity(),
                        collectingAndThen(counting(), Long::intValue)));
        System.out.println(countCharacter);
    }
}
