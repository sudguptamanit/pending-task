package org.example.code;


//Implement the 'walk' method.
//Assume robot inital position is (0,0).
//walk method take path where each character corresponds to a movement of the robot.
// The robot moves up, down, left, and right represented by the characters 'U', 'D', 'L', and 'R'
// Ignore other characters.

//        Time        O(n) — single pass through the string
//        Space       O(1) — only two integers, no extra data structures

import java.util.Arrays;

    public class RobotMovement {

        public static Integer[] walk(String path) {
            if (path == null || path.isEmpty()) return new Integer[]{0, 0};

            int x = 0, y = 0;

            for (int i = 0; i < path.length(); i++) {
                switch (path.charAt(i)) {
                    case 'U': y++; break;
                    case 'D': y--; break;
                    case 'R': x++; break;
                    case 'L': x--; break;
                    // all other characters (spaces, letters, digits) are ignored
                }
            }

            return new Integer[]{x, y};
        }

        public static boolean isEqual(Integer[] a, Integer[] b) {
            return Arrays.equals(a, b);
        }

        public static boolean pass() {
            boolean res = true;

            { // moves only north
                String test = "UUU";
                res &= isEqual(walk(test), new Integer[]{0, 3});
            }
            { // returns to origin
                String test = "ULDR";
                res &= isEqual(walk(test), new Integer[]{0, 0});
            }
            { // mixed moves
                String test = "ULLLDUDUURLRLR";
                res &= isEqual(walk(test), new Integer[]{-2, 2});
            }
            { // natural language string — only U,D,L,R chars count
                String test = "UP LEFT 2xDOWN DOWN RIGHT RIGHT UP UP";
                res &= isEqual(walk(test), new Integer[]{1, 1});
            }

            return res;
        }

        public static void main(String[] args) {
            if (pass()) {
                System.out.println("Pass");
            } else {
                System.out.println("Test failures");
            }
        }
    }