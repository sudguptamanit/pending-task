package org.example.code1;

import java.util.*;

//⏱️ Time Complexity
//Operation	Average	Worst Case
//put	O(log n)	O(n)
//contains	O(log n)	O(n)
//inorder	O(n)	O(n)
//
//        👉 Worst case happens when tree becomes skewed
//
//🧠 Space Complexity
//O(h) recursion stack
//Balanced: O(log n)
//Skewed: O(n)
//

public class SearchTree {

    static class BST {

        private Node root;

        public BST() {
            this.root = null; // FIX: start with empty tree
        }

        // Insert into BST
        public void put(int value) {
            root = insert(root, value);
        }

        private Node insert(Node node, int value) {
            if (node == null) {
                Node newNode = new Node();
                newNode.val = value;
                return newNode;
            }

            if (value < node.val) {
                node.left = insert(node.left, value);
            } else if (value > node.val) {
                node.right = insert(node.right, value);
            }
            // ignore duplicates

            return node;
        }

        // Search in BST
        public boolean contains(int value) {
            Node curr = root;

            while (curr != null) {
                if (value == curr.val) return true;
                else if (value < curr.val) curr = curr.left;
                else curr = curr.right;
            }

            return false;
        }

        public List<Integer> inOrderTraversal() {
            final ArrayList<Integer> acc = new ArrayList<>();
            inOrderTraversal(root, acc);
            return acc;
        }

        // FIXED: Left -> Root -> Right
        private void inOrderTraversal(Node node, List<Integer> acc) {
            if (node == null) return;

            inOrderTraversal(node.left, acc);
            acc.add(node.val); // FIX: add before right
            inOrderTraversal(node.right, acc);
        }

        private static class Node {
            Integer val;
            Node left;
            Node right;
        }
    }

    public static void main(String[] args) {

        final BST searchTree = new BST();

        searchTree.put(3);
        searchTree.put(1);
        searchTree.put(2);
        searchTree.put(5);

        if (Arrays.asList(1, 2, 3, 5).equals(searchTree.inOrderTraversal())
                && !searchTree.contains(0)
                && searchTree.contains(1)
                && searchTree.contains(2)
                && searchTree.contains(3)
                && !searchTree.contains(4)
                && searchTree.contains(5)
                && !searchTree.contains(6)) {
            System.out.println("Pass");
        } else {
            System.out.println("Fail");
        }
    }
}