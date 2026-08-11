package org.example.code;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

//Time        O(n)single pass to count + single pass over unique IPs
//Space       O(k)k = number of unique IPs in the log

public class ApacheLog {

    public static String findTopIpAddress(String[] lines) {
        if (lines == null || lines.length == 0) return "";

        HashMap<String, Integer> ipCount = new HashMap<>();

        for (String line : lines) {
            if (line == null || line.isEmpty()) continue;
            String ip = line.split(" ")[0];           // IP is always first token
            ipCount.put(ip, ipCount.getOrDefault(ip, 0) + 1);
        }

        // Find max count
        int maxCount = 0;
        for (int count : ipCount.values()) {
            maxCount = Math.max(maxCount, count);
        }

        // Collect all IPs tied for max (handles tie case)
        List<String> topIps = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : ipCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                topIps.add(entry.getKey());
            }
        }

        return topIps.size() == 1 ? topIps.get(0) : String.join(", ", topIps);
    }

    public static boolean doTestsPass() {
        boolean result = true;

        // Test 1: basic case from prompt
        {
            String[] lines = {
                    "10.0.0.1 - log entry 1",
                    "10.0.0.1 - log entry 2",
                    "10.0.0.2 - log entry 1"
            };
            result &= findTopIpAddress(lines).equals("10.0.0.1");
        }

        // Test 2: single entry
        {
            String[] lines = {"192.168.1.1 - log entry"};
            result &= findTopIpAddress(lines).equals("192.168.1.1");
        }

        // Test 3: all IPs appear once → tie, both returned
        {
            String[] lines = {
                    "10.0.0.1 - log entry",
                    "10.0.0.2 - log entry"
            };
            String res = findTopIpAddress(lines);
            result &= res.contains("10.0.0.1") && res.contains("10.0.0.2");
        }

        // Test 4: large input, clear winner
        {
            String[] lines = {
                    "10.0.0.1 - log entry",
                    "10.0.0.1 - log entry",
                    "10.0.0.1 - log entry",
                    "10.0.0.2 - log entry",
                    "10.0.0.3 - log entry"
            };
            result &= findTopIpAddress(lines).equals("10.0.0.1");
        }

        // Test 5: null/empty input
        {
            result &= findTopIpAddress(null).equals("");
            result &= findTopIpAddress(new String[]{}).equals("");
        }

        // Test 6: different subnet winner
        {
            String[] lines = {
                    "172.16.0.1 - log entry",
                    "172.16.0.2 - log entry",
                    "172.16.0.2 - log entry",
                    "192.168.1.1 - log entry"
            };
            result &= findTopIpAddress(lines).equals("172.16.0.2");
        }

        System.out.println(result ? "All tests passed." : "Some tests failed.");
        return result;
    }

    public static void main(String[] args) {
        doTestsPass();

        // Original test from prompt
        String[] lines = {
                "10.0.0.1 - log entry 1 11",
                "10.0.0.1 - log entry 2 213",
                "10.0.0.2 - log entry 133132"
        };
        String result = findTopIpAddress(lines);
        System.out.println("Top IP: " + result);
        System.out.println(result.equals("10.0.0.1") ? "Test passed." : "Test failed.");
    }
}