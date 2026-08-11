package org.example.April;

public class Solution25 {


    // Input two words returns the shortest distance between their two midpoints in number of characters
    // Words can appear multiple times in any order and should be case insensitive.
//⚡ Complexity
//    Time: O(n)
//    Space: O(1)
    // E.g. for the document="Example we just made up"
    //   shortestDistance( document, "we", "just" ) == 4
//
//    This problem is about finding the minimum distance between two words in a document, but instead of word index distance, it uses distance between their midpoints (character positions).
//
//    return : minimum distance between midpoints of word1 and word2
//
//
//            🔥 Example Walkthrough
//    Input:
//    document = "Example we just made up"
//    word1 = "we"
//    word2 = "just"
//    Step 1: Convert to lowercase
//    example we just made up
//    Step 2: Parse word by word
//
//    We scan character by character.
//
//            Word 1: "example"
//    start = 0, end = 7
//    mid = 0 + (7-0)/2 = 3
//
//            👉 Not match → ignore
//
//    Word 2: "we"
//    start = 8, end = 10
//    mid = 8 + (10-8)/2 = 9
//
//            👉 Match word1 ("we")
//
//    lastPos1 = 9
//    Word 3: "just"
//    start = 11, end = 15
//    mid = 11 + (15-11)/2 = 13
//
//            👉 Match word2 ("just")
//
//    lastPos2 = 13
//    Step 3: Compute distance
//Math.abs(lastPos1 - lastPos2)
//            |9 - 13| = 4
//            ✅ Final Answer:
//            4


    public static double shortestDistance(String document, String word1, String word2) {
        if (document == null || word1 == null || word2 == null) return -1;
        String doc = document.toLowerCase();
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();
        int lastPos1 = -1;
        int lastPos2 = -1;
        double minDist = Double.MAX_VALUE;
        int i = 0;
        while (i < doc.length()) {
            // Skip spaces
            while (i < doc.length() && doc.charAt(i) == ' ') i++;
            if (i >= doc.length()) break;

            int start = i;

            // Find word
            while (i < doc.length() && doc.charAt(i) != ' ') i++;
            int end = i;

            String word = doc.substring(start, end);

            // Check matches
            if (word.equals(word1)) {
                lastPos1 = start + (end - start) / 2;
            } else if (word.equals(word2)) {
                lastPos2 = start + (end - start) / 2;
            }

            // If both seen → compute distance
            if (lastPos1 != -1 && lastPos2 != -1) {
                minDist = Math.min(minDist, Math.abs(lastPos1 - lastPos2));
            }
        }

        return minDist == Double.MAX_VALUE ? -1 : minDist;
    }

    public static boolean pass() {
        return  shortestDistance(document, "and", "graphic") == 6d &&
                shortestDistance(document, "transfer", "it") == 14d &&
                shortestDistance(document, "Design", "filler" ) == 25d ;
    }

    public static void main(String[] args) {
        if (pass()) {
            System.out.println("Pass");
        } else {
            System.out.println("Some Fail");
        }
    }

    private static final String document;
    static{
        StringBuffer sb = new StringBuffer();
        sb.append("In publishing and graphic design, lorem ipsum is a filler text commonly used to demonstrate the graphic elements");
        sb.append(" lorem ipsum text has been used in typesetting since the 1960s or earlier, when it was popularized by advertisements");
        sb.append(" for Letraset transfer sheets. It was introduced to the Information Age in the mid-1980s by Aldus Corporation, which");

        document = sb.toString();
    }
}