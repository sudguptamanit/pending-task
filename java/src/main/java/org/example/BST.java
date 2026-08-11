package org.example;

//| Operation | Average Case | Worst Case |
//        | --------- | ------------ | ---------- |
//        | Put       | O(log n)     | O(n)       |
//        | Contains  | O(log n)     | O(n)       |
//        | InOrder   | O(n)         | O(n)       |



class BST {

    // Node structure
    static class Node {
        int key;
        Node left, right;

        Node(int key) {
            this.key = key;
        }
    }

    private Node root;

    // ========================
    // 1. PUT (Insert)
    // ========================
    public void put(int key) {
        root = put(root, key);
    }

    private Node put(Node node, int key) {
        if (node == null) return new Node(key);

        if (key < node.key) {
            node.left = put(node.left, key);
        } else if (key > node.key) {
            node.right = put(node.right, key);
        }
        // duplicate keys ignored

        return node;
    }

    // ========================
    // 2. CONTAINS (Search)
    // ========================
    public boolean contains(int key) {
        return contains(root, key);
    }

    private boolean contains(Node node, int key) {
        if (node == null) return false;

        if (key < node.key) {
            return contains(node.left, key);
        } else if (key > node.key) {
            return contains(node.right, key);
        } else {
            return true;
        }
    }

    // ========================
    // 3. INORDER (Sorted Traversal)
    // ========================
    public void inOrder() {
        inOrder(root);
        System.out.println();
    }

    private void inOrder(Node node) {
        if (node == null) return;

        inOrder(node.left);
        System.out.print(node.key + " ");
        inOrder(node.right);
    }

    // ========================
    // MAIN METHOD
    // ========================
    public static void main(String[] args) {
        BST tree = new BST();

        tree.put(5);
        tree.put(3);
        tree.put(7);
        tree.put(2);
        tree.put(4);
        tree.put(6);
        tree.put(8);

        System.out.println("Contains 4? " + tree.contains(4)); // true
        System.out.println("Contains 10? " + tree.contains(10)); // false

        System.out.print("InOrder Traversal: ");
        tree.inOrder(); // 2 3 4 5 6 7 8
    }
}