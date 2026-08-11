package org.example.April;

import java.util.*;
/*
 * Instructions to candidate.
 * Implement the "put" and "contains" methods.
 * Fix the "inOrderTraversal" method.
 *
 * ⚡ Complexity
Operation	Time Complexity
Insert	O(log n) avg
Search	O(log n) avg
Traversal	O(n)
*
*
 */

public class Solution07 {

    static class BST  {

        private Node root;

        public BST() {
            this.root = null;
        }

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

        public boolean contains(int value) {
            Node current = root;

            while (current != null) {
                if (value == current.val) {
                    return true;
                } else if (value < current.val) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }

            return false;
        }

        public List<Integer> inOrderTraversal() {
            final ArrayList<Integer> acc = new ArrayList<>();
            inOrderTraversal(root, acc);
            return acc;
        }

        private void inOrderTraversal(Node node, List<Integer> acc) {
            if (node == null) {
                return;
            }
            inOrderTraversal(node.left, acc);   // LEFT
            acc.add(node.val);                  // ROOT (FIXED POSITION)
            inOrderTraversal(node.right, acc);  // RIGHT
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

        if(Arrays.asList(1,2,3,5).equals(searchTree.inOrderTraversal())
                && !searchTree.contains(0)
                && searchTree.contains(1)
                && searchTree.contains(2)
                && searchTree.contains(3)
                && !searchTree.contains(4)
                && searchTree.contains(5)
                && !searchTree.contains(6)){
            System.out.println("Pass");
        }else {
            System.out.println("Fail");
        }

    }

}