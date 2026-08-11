package org.example.code;

//⏱️ Complexity
//Time: O(n) (single scan)
//Space: O(1) (no extra storage)

public class ShortestDistance {


    // Input two words returns the shortest distance between their two midpoints in number of characters
    // Words can appear multiple times in any order and should be case insensitive.

    // E.g. for the document="Example we just made up"
    //   shortestDistance( document, "we", "just" ) == 4

    public static double shortestDistance(String document, String word1, String word2) {
        if (document == null || word1 == null || word2 == null) return -1;

        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        double shortest = Double.MAX_VALUE;

        double lastMid1 = -1;
        double lastMid2 = -1;

        int i = 0;
        int n = document.length();

        while (i < n) {
            // Skip non-letter characters
            while (i < n && !Character.isLetter(document.charAt(i))) {
                i++;
            }

            if (i >= n) break;

            int start = i;

            // Read the word
            while (i < n && Character.isLetter(document.charAt(i))) {
                i++;
            }

            int end = i - 1;

            String word = document.substring(start, i).toLowerCase();
            double mid = (start + end) / 2.0;

            if (word.equals(word1)) {
                lastMid1 = mid;
                if (lastMid2 != -1) {
                    shortest = Math.min(shortest, Math.abs(lastMid1 - lastMid2));
                }
            } else if (word.equals(word2)) {
                lastMid2 = mid;
                if (lastMid1 != -1) {
                    shortest = Math.min(shortest, Math.abs(lastMid1 - lastMid2));
                }
            }
        }

        return shortest == Double.MAX_VALUE ? -1 : shortest;
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