package com.morening.java.learn;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Main {

    private static final int TYPE_MAX = 0;
    private static final int TYPE_MIN = 1;

    private static final int MAX_TREE_DEPTH = 10;
    private static final int MAX_CHILD_NUM = 5;
    private static final int MAX_RANDOM_VAL = 50;

    public static void main(String[] args) {
        int nDepth = new Random().nextInt(MAX_TREE_DEPTH) + 1;
        Node root = createTree(nDepth);
        evaluate(root, nDepth);
    }

    private static void evaluate(Node root, int nDepth) {
        int pruningCount = AlphaBetaPruning(root);
        System.out.println("=== AlphaBetaPruning ===");
        System.out.println("选择深度："+nDepth);
        System.out.println("评估结果："+root.val);
        System.out.println("评估次数："+pruningCount);
        printAnswer(root);

        int miniMaxCount = MiniMaxAlgorithm(root);
        System.out.println("=== MiniMaxAlgorithm ===");
        System.out.println("选择深度："+nDepth);
        System.out.println("评估结果："+root.val);
        System.out.println("评估次数："+miniMaxCount);
        printAnswer(root);
    }

    private static void printAnswer(Node root) {
        int depth = 0;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            Node temp = queue.poll();
            if (depth != temp.depth){
                depth++;
                System.out.println();
            }
            System.out.print(temp.val+" ");
            Node cur = temp.child;
            while (cur != null){
                queue.add(cur);
                cur = cur.brother;
            }
        }
        System.out.println();
    }

    private static int MiniMaxAlgorithm(Node root){
        if (root.child == null){
            return 0;
        }
        int count = 0;
        root.val = root.type == TYPE_MAX ? Integer.MIN_VALUE: Integer.MAX_VALUE;
        Node child = root.child;
        while (child != null){
            count += MiniMaxAlgorithm(child);
            count++;
            if (root.type == TYPE_MAX){
                root.val = root.val > child.val ? root.val: child.val;
            } else if (root.type == TYPE_MIN){
                root.val = root.val < child.val ? root.val: child.val;
            }
            child = child.brother;
        }

        return count;
    }

    private static int AlphaBetaPruning(Node root) {
        if (root.child == null){
            return 0;
        }
        int count = 0;
        root.val = root.type == TYPE_MAX ? Integer.MIN_VALUE: Integer.MAX_VALUE;
        Node parent = root.parent;
        Node child = root.child;
        while (child != null){
            count += AlphaBetaPruning(child);
            count++;
            if (root.type == TYPE_MAX){
                root.val = root.val > child.val ? root.val: child.val;

                if (parent != null && parent.type == TYPE_MIN){
                    if (parent.val <= root.val){
                        return count;
                    }
                }
            } else if (root.type == TYPE_MIN){
                root.val = root.val < child.val ? root.val: child.val;

                if (parent != null && parent.type == TYPE_MAX){
                    if (parent.val >= root.val){
                        return count;
                    }
                }
            }
            child = child.brother;
        }

        if (parent != null && parent.type == TYPE_MAX){
            parent.val = parent.val > root.val ? parent.val: root.val;
        } else if (parent != null && parent.type == TYPE_MIN){
            parent.val = parent.val < root.val ? parent.val: root.val;
        }

        return count;
    }

    private static Node createTree(int nDepth) {
        Node root = new Node(TYPE_MAX, 0);
        createNode(root, 1, nDepth);

        return root;
    }

    private static void createNode(Node root, int depth, int nDepth) {
        if (depth >= nDepth){
            return;
        }
        int nChild = new Random().nextInt(MAX_CHILD_NUM) + 1;
        for (int j = 0; j < nChild; j++) {
            if (depth == nDepth-1) {
                Node temp = new Node(depth % 2, depth);
                temp.val = new Random().nextInt(MAX_RANDOM_VAL) + 1;
                insertNode(root, temp);
            } else {
                Node temp = new Node(depth % 2, depth);
                insertNode(root, temp);
                createNode(temp, depth + 1, nDepth);
            }
        }

    }

    /* 尾插入 */
    private static void insertNode(Node root, Node temp) {
        temp.brother = root.child;
        root.child = temp;

        temp.parent = root;
    }

    static class Node {
        int depth = 0;
        int val = 0;
        int type = TYPE_MAX;

        Node child = null;
        Node brother = null;

        Node parent = null;

        public Node(int type, int depth) {
            this.type = type;
            this.depth = depth;
        }
    }
}