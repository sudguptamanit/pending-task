package org.example.April;

public class Solution11 {

    static class Deque {

        private static class Node {
            String val;
            Node prev;
            Node next;

            Node(String val) {
                this.val = val;
            }
        }

        private Node head;
        private Node tail;

        // Add to front
        public void addFirst(String val) {
            Node node = new Node(val);

            if (head == null) {
                head = tail = node;
            } else {
                node.next = head;
                head.prev = node;
                head = node;
            }
        }

        // Add to back
        public void addLast(String val) {
            Node node = new Node(val);

            if (tail == null) {
                head = tail = node;
            } else {
                tail.next = node;
                node.prev = tail;
                tail = node;
            }
        }

        // Remove from front
        public String removeFirst() {
            if (head == null) {
                throw new RuntimeException("Deque is empty");
            }

            String val = head.val;
            head = head.next;

            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }

            return val;
        }

        // Remove from back
        public String removeLast() {
            if (tail == null) {
                throw new RuntimeException("Deque is empty");
            }

            String val = tail.val;
            tail = tail.prev;

            if (tail != null) {
                tail.next = null;
            } else {
                head = null;
            }

            return val;
        }

        // Peek front
        public String peekFirst() {
            if (head == null) return null;
            return head.val;
        }

        // Peek back
        public String peekLast() {
            if (tail == null) return null;
            return tail.val;
        }

        public boolean isEmpty() {
            return head == null;
        }
    }

    public static void main(String[] args) {
        Deque dq = new Deque();

        dq.addFirst("b");
        dq.addFirst("a");
        dq.addLast("c");
        dq.addLast("d");

        // Expected: a b c d
        while (!dq.isEmpty()) {
            System.out.print(dq.removeFirst() + " ");
        }
    }
}
