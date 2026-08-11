package org.example.code1;

//⏱️ Time Complexity
//Operation	Complexity
//addFirst	O(1)
//addLast	O(1)
//removeFirst	O(1)
//removeLast	O(1)
//peek operations	O(1)
//🧠 Space Complexity
//O(n) → storing elements
//
public class Deque {

    // Node class
    static class Node {
        String data;
        Node prev, next;

        Node(String data) {
            this.data = data;
        }
    }

    static class Dequ {
        private Node head; // front
        private Node tail; // rear

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

        // Add to rear
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
            if (head == null) throw new RuntimeException("Deque is empty");

            String val = head.data;
            head = head.next;

            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }

            return val;
        }

        // Remove from rear
        public String removeLast() {
            if (tail == null) throw new RuntimeException("Deque is empty");

            String val = tail.data;
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
            if (head == null) throw new RuntimeException("Deque is empty");
            return head.data;
        }

        // Peek rear
        public String peekLast() {
            if (tail == null) throw new RuntimeException("Deque is empty");
            return tail.data;
        }

        // Check empty
        public boolean isEmpty() {
            return head == null;
        }
    }

    public static void main(String[] args) {
        Dequ dq = new Dequ();

        dq.addFirst("b");
        dq.addFirst("a");   // a b
        dq.addLast("c");    // a b c

        System.out.println(dq.removeFirst()); // a
        System.out.println(dq.removeLast());  // c
        System.out.println(dq.peekFirst());   // b
    }
}