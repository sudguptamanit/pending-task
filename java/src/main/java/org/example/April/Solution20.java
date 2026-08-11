package org.example.April;

import java.util.*;

public class Solution20 {


        /**
         * Given a log file, return IP address(es) which accesses the site most often.
         * ⚡ Complexity
         * Time: O(n)
         * Space: O(n) (in worst case all IPs unique)
         */

        public static String findTopIpaddress(String[] lines) {
            if (lines == null || lines.length == 0) return "";
            Map<String, Integer> freqMap = new HashMap<>();
            // Count frequency of each IP
            for (String line : lines) {
                String[] parts = line.split(" ");
                String ip = parts[0];

                freqMap.put(ip, freqMap.getOrDefault(ip, 0) + 1);
            }
            // Find max frequency IP
            String topIp = "";
            int maxCount = 0;

            for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    topIp = entry.getKey();
                }
            }
            return topIp;
        }




        public static void main(String[] args) {

            String lines[] = new String[] {
                    "10.0.0.1 - log entry 1 11",
                    "10.0.0.1 - log entry 2 213",
                    "10.0.0.2 - log entry 133132" };
            String result = findTopIpaddress(lines);

            if (result.equals("10.0.0.1")) {
                System.out.println("Test passed");

            } else {
                System.out.println("Test failed");

            }

        }

    }