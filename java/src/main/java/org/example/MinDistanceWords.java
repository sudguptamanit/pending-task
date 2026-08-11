package org.example;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(1)** |

public class MinDistanceWords {

    public static int minDistance(String sentence, String w1, String w2) {
        int n = sentence.length();
        int i = 0;

        int lastW1Mid = -1;
        int lastW2Mid = -1;
        int minDist = Integer.MAX_VALUE;

        while (i < n) {

            // Skip spaces
            while (i < n && sentence.charAt(i) == ' ') i++;

            int start = i;

            // Read word
            while (i < n && sentence.charAt(i) != ' ') i++;

            int end = i - 1;

            if (start <= end) {
                String word = sentence.substring(start, i);

                int mid = start + (end - start) / 2;

                if (word.equals(w1)) {
                    lastW1Mid = mid;
                    if (lastW2Mid != -1) {
                        minDist = Math.min(minDist, Math.abs(lastW1Mid - lastW2Mid));
                    }
                }

                if (word.equals(w2)) {
                    lastW2Mid = mid;
                    if (lastW1Mid != -1) {
                        minDist = Math.min(minDist, Math.abs(lastW2Mid - lastW1Mid));
                    }
                }
            }
        }

        return minDist == Integer.MAX_VALUE ? -1 : minDist;
    }

    public static void main(String[] args) {
        String sentence = "ABC is XYZ";
        String w1 = "ABC";
        String w2 = "XYZ";

        System.out.println(minDistance(sentence, w1, w2)); // 7
    }
}
