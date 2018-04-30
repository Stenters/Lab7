/*
 * CS2852 – 031
 * Spring 2017
 * Lab 7 - Morse Code Decoder
 * Name: Stuart Enters
 * Created: 4/30/2018
 */

package enterss;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.function.BiConsumer;

/**
 * Class for a binary tree that stores type E objects.
 *
 * @param <E> The element type
 * @author Koffman and Wolfgang
 *
 */
public class MorseTree<E> implements Serializable {
    /**
     * Class to encapsulate a tree node.
     *
     * @param <E> The element type
     */
    protected static class Node<E> implements Serializable {
        // Data Fields

        /**
         * The information stored in this node.
         */
        public E data;
        /**
         * Reference to the left child.
         */
        public Node<E> left;
        /**
         * Reference to the right child.
         */
        public Node<E> right;

        // Constructors
        /**
         * Construct a node with given data and no children.
         *
         * @param data The data to store in this node
         */
        public Node(E data) {
            this.data = data;
            left = null;
            right = null;
        }

        // Methods
        /**
         * Returns a string representation of the node.
         *
         * @return A string representation of the data fields
         */
        @Override
        public String toString() {
            if (data == null){
                return null;
            } else {
                return data.toString();
            }
        }

    }
    // Data Field
    /**
     * The root of the binary tree
     */
    protected Node<E> root;

    /**
     * Construct an empty MorseTree
     */
    public MorseTree() {
        this.root = null;
    }

    /**
     * Method for adding a symbol to a tree
     * @param symbol the symbol to add
     * @param code the code for where to put the symbol
     * @return if the addition was successful
     * @throws IOException
     */
    public boolean add(E symbol, String code) throws IOException{
        char[] path = code.toCharArray();
        if (root == null){
            root = new Node<>(null);
        }
        Node<E> current = root;
        for (char p : path) {
            if (p == '.') {
                if (current.left == null) {
                    current.left = new Node<>(null);
                }
                current = current.left;
            } else if (p == '-') {
                if (current.right == null) {
                    current.right = new Node<>(null);
                }
                current = current.right;
            } else if (p != ' ') {
                System.out.println("Warning: skipping: " + p);
            }
        }
        if (current.data == null){
            current.data = symbol;
        } else if (!current.data.equals(symbol)){
            System.out.println("Exception in Dictionary file, terminating");
            throw new IOException();
        }

        if (symbol != null){
            return decode(code).equals(symbol);
        } else {
            return decode(code) == null;
        }
    }

    /**
     * Decode a character in morse code
     * @param code the morse code of the character
     * @return the character
     */
    public E decode(String code) {
        char[] path = code.toCharArray();
        Node<E> current = root;
        if (current == null){
            System.out.println("Warning: Illegal code found, aborting");
            return null;
        }
        for (char p: path) {
            if(p == '.'){
                if (current.left != null){
                    current = current.left;
                } else {
                    System.out.println("Warning: Illegal code found, aborting");
                    return null;
                }
            } else if (p == '-'){
                if (current.right != null){
                    current = current.right;
                } else {
                    System.out.println("Warning: Illegal code found, aborting");
                    return null;
                }
            } else if (p != ' '){
                System.out.println("Warning: Illegal char found: Skipping " + p);
            }
        }
        return current.data;
    }

    /**
     * Construct a MorseTree with a specified root. Should only be used by
     * subclasses.
     *
     * @param root The node that is the root of the tree.
     */
    protected MorseTree(Node<E> root) {
        this.root = root;
    }

    /**
     * Constructs a new binary tree with data in its root,leftTree as its left
     * subtree and rightTree as its right subtree.
     *
     * @param data The data item to store in the root
     * @param leftTree the left child
     * @param rightTree the right child
     */
    public MorseTree(E data, MorseTree<E> leftTree,
                     MorseTree<E> rightTree) {
        root = new Node<E>(data);
        if(leftTree != null) {
            this.root.left = leftTree.root;
        } else {
            this.root.left = null;
        }
        if(rightTree != null) {
            this.root.right = rightTree.root;
        } else {
            this.root.right = null;
        }
    }

    /**
     * Return the left subtree.
     *
     * @return The left subtree or null if either the root or the left subtree
     * is null
     */
    public MorseTree<E> getLeftSubtree() {
        if(this.root != null && this.root.left != null) {
            return new MorseTree<E>(this.root.left);
        } else {
            return null;
        }
    }

    /**
     * Return the right sub-tree
     *
     * @return the right sub-tree or null if either the root or the right
     * subtree is null.
     */
    public MorseTree<E> getRightSubtree() {
        if(this.root != null && this.root.right != null) {
            return new MorseTree<>(this.root.right);
        } else {
            return null;
        }
    }

    /**
     * Return the data field of the root
     *
     * @return the data field of the root or null if the root is null
     */
    public E getData() {
        if(this.root != null) {
            return root.data;
        } else {
            return null;
        }
    }

    /**
     * Determine whether this tree is a leaf.
     *
     * @return true if the root has no children
     */
    public boolean isLeaf() {
        return this.root == null || this.root.left == null
                && this.root.right == null;
    }

    /**
     * Method for turning the tree into a string
     * @return the string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        preOrderTraverse((e, d) -> {
            for (int i = 1; i < d; i++) {
                sb.append("  ");
            }
            sb.append(e);
            sb.append("\n");
        });
        return sb.toString();
    }

    /*<listing chapter="6" section="4">
    /**
     * Starter method for preorder traversal
     * @param consumer an object that instantiates the BiConsumer interface.
     *        Its method implements the abstract method apply.
     */
    public void preOrderTraverse(BiConsumer<E, Integer> consumer) {
        preOrderTraverse(root, 1, consumer);
    }

    /**
     * Performs a recursive pre-order traversal of the tree,
     * applying the action specified in the consumer object.
     * @param node The local root
     * @param depth The depth
     * @param consumer object whose accept method specifies the action
     * to be performed on each node.
     */
    private void preOrderTraverse(Node<E> node, int depth,
                                  BiConsumer<E, Integer> consumer) {
        if (node != null){
            consumer.accept(node.data, depth);
            preOrderTraverse(node.left, ++depth, consumer);
            preOrderTraverse(node.right, ++depth, consumer);
        }
    }

    /**
     * Method to read a binary tree.
     *
     * @pre The input consists of a pre-order traversal of the binary tree. The
     * line "null" indicates a null tree.
     * @param scan the Scanner attached to the input file
     * @return The binary tree
     */
    public static MorseTree<String> readBinaryTree(Scanner scan) {
        // Read a line and trim leading and trailing spaces.
        throw new UnsupportedOperationException();
    }
}
/*</listing>*/

