package org.example.experiencegold;

//
//1.	Count Good Nodes in Binary Tree
//
//You are given the root of a binary tree.
//You need to count the number of Good Nodes in the tree.
//
//Definition of a Good Node
//A node X in the tree is called Good if:
//There is no node with a value greater than X on the path from the root to that node.
//In other words:
//While moving from the root to the current node,
//        if the current node's value is greater than or equal to every node visited before it,
//then it is considered a Good Node.
//
//
//        Input
//The input is given as a binary tree, usually in array (level-order) representation.
//Example:
//root = [3,1,4,3,null,1,5]
//
//        3
//        / \
//        1   4
//        /   / \
//        3   1   5
//
//Output :- 4
//

public class GoodNode {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

//    ⏱️ Time Complexity
//    O(N) → visit each node once
//🧠 Space Complexity
//    O(H) → recursion stack (H = tree height)
//    Worst case (skewed tree): O(N)
//    Balanced tree: O(log N)
//
    public static int countGoodNodes(TreeNode root) {
        return dfs(root, Integer.MIN_VALUE);
    }

    private static int dfs(TreeNode node, int maxSoFar) {
        if (node == null) return 0;

        int count = 0;

        // Check if current node is a good node
        if (node.val >= maxSoFar) {
            count = 1;
            maxSoFar = node.val;
        }

        // Recurse left and right
        count += dfs(node.left, maxSoFar);
        count += dfs(node.right, maxSoFar);

        return count;
    }

    // Helper to build the example tree
    public static void main(String[] args) {
    /*
            3
           / \
          1   4
         /   / \
        3   1   5
    */

        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(1);
        root.right = new TreeNode(4);
        root.left.left = new TreeNode(3);
        root.right.left = new TreeNode(1);
        root.right.right = new TreeNode(5);

        System.out.println(countGoodNodes(root)); // Output: 4
    }
}