package org.example;

//| Metric | Value                      |
//        | ------ | -------------------------- |
//        | Time   | **O(n)**                   |
//        | Space  | **O(n)** (recursion stack) |

class Node {
    int data;
    Node prev, next;

    Node(int data) {
        this.data = data;
        prev = next = null;
    }
}

public class ReverseDLL {

    public static Node reverse(Node head) {
        if (head == null) return null;

        // Swap prev and next
        Node temp = head.prev;
        head.prev = head.next;
        head.next = temp;

        // If prev is now null, this is new head
        if (head.prev == null) {
            return head;
        }

        // Recurse for next node (originally next, now prev)
        return reverse(head.prev);
    }

    public static void printList(Node head) {
        Node curr = head;
        while (curr != null) {
            System.out.print(curr.data + " ");
            curr = curr.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.prev = head;

        head.next.next = new Node(3);
        head.next.next.prev = head.next;

        head.next.next.next = new Node(4);
        head.next.next.next.prev = head.next.next;

        System.out.print("Original: ");
        printList(head);

        head = reverse(head);

        System.out.print("Reversed: ");
        printList(head);
    }
}