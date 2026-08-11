package org.example;

import java.util.Stack;

//| Operation | Time               |
//        | --------- | ------------------ |
//        | Enqueue   | **O(1)**           |
//        | Dequeue   | **O(1)** amortized |
//        | Peek      | **O(1)** amortized |
//

public class QueueUsingStacks {

    private Stack<Integer> inStack;
    private Stack<Integer> outStack;

    public QueueUsingStacks() {
        inStack = new Stack<>();
        outStack = new Stack<>();
    }

    // Enqueue
    public void enqueue(int x) {
        inStack.push(x);
    }

    // Dequeue
    public int dequeue() {
        shiftStacks();
        if (outStack.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return outStack.pop();
    }

    // Peek
    public int peek() {
        shiftStacks();
        if (outStack.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return outStack.peek();
    }

    // Check empty
    public boolean isEmpty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    // Helper: move elements only when needed
    private void shiftStacks() {
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    public static void main(String[] args) {
        QueueUsingStacks q = new QueueUsingStacks();

        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        System.out.println(q.dequeue()); // 1
        System.out.println(q.peek());    // 2
        System.out.println(q.dequeue()); // 2
    }
}