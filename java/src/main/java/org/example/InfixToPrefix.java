package org.example;

import java.util.Stack;

//| Metric | Value    |
//        | ------ | -------- |
//        | Time   | **O(n)** |
//        | Space  | **O(n)** |
//

public class InfixToPrefix {

    // Function to check precedence
    static int precedence(char ch) {
        switch (ch) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            case '^': return 3;
        }
        return -1;
    }

    // Convert Infix to Prefix
    public static String infixToPrefix(String infix) {
        // Step 1: Reverse string
        StringBuilder input = new StringBuilder(infix);
        input.reverse();

        // Step 2: Swap brackets
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                input.setCharAt(i, ')');
            } else if (input.charAt(i) == ')') {
                input.setCharAt(i, '(');
            }
        }

        // Step 3: Convert to Postfix
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            // Operand
            if (Character.isLetterOrDigit(ch)) {
                postfix.append(ch);
            }
            // Opening bracket
            else if (ch == '(') {
                stack.push(ch);
            }
            // Closing bracket
            else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop(); // remove '('
            }
            // Operator
            else {
                while (!stack.isEmpty() &&
                        precedence(ch) < precedence(stack.peek())) {
                    postfix.append(stack.pop());
                }
                stack.push(ch);
            }
        }

        // Pop remaining operators
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }

        // Step 4: Reverse postfix to get prefix
        return postfix.reverse().toString();
    }

    public static void main(String[] args) {
        String infix = "(A-B/C)*(A/K-L)";
        System.out.println("Prefix: " + infixToPrefix(infix));
    }
}