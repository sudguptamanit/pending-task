package org.example;

public class MinDistanceBetweenWords {

    public static int minDistance(String sentence, String word1, String word2) {
        if (sentence == null || word1 == null || word2 == null)
            throw new IllegalArgumentException("Inputs cannot be null");

        String[] words = sentence.split(" ");

        int lastPosMid1 = -1;  // last seen middle-char position of word1
        int lastPosMid2 = -1;  // last seen middle-char position of word2
        int minDist     = Integer.MAX_VALUE;
        int charPos     = 0;   // tracks start position of current word in sentence

        for (String word : words) {
            int midPos = charPos + word.length() / 2;  // middle char position in sentence

            if (word.equalsIgnoreCase(word1)) {
                lastPosMid1 = midPos;
                if (lastPosMid2 != -1)
                    minDist = Math.min(minDist, Math.abs(lastPosMid1 - lastPosMid2));
            }

            if (word.equalsIgnoreCase(word2)) {
                lastPosMid2 = midPos;
                if (lastPosMid1 != -1)
                    minDist = Math.min(minDist, Math.abs(lastPosMid1 - lastPosMid2));
            }

            charPos += word.length() + 1;  // +1 for the space
        }

        if (minDist == Integer.MAX_VALUE)
            throw new IllegalArgumentException("One or both words not found in sentence");

        return minDist;
    }

    public static void main(String[] args) {
        String s1 = "ABC is XYZ and ABC and XYZ are two distinct words";
        System.out.println(minDistance(s1, "ABC", "XYZ"));  // 4

        String s2 = "hello world hello";
        System.out.println(minDistance(s2, "hello", "world"));  // 5

        String s3 = "cat and dog and cat dog";
        System.out.println(minDistance(s3, "cat", "dog"));  // 4
    }
}