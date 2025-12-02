package containers;

import interfaces.KVEntryInterface;

/**
 * Represents a binary node that stores a key-value pair and maintains
 * references to its left child, right child, and parent nodes. Both the key
 * and value must implement the Comparable interface, allowing for natural
 * ordering. Binary Nodes are comparable based on their keys.
 *
 * @param <K> the type of the key, which must extend Comparable
 * @param <V> the type of the value, which must extend Comparable
 */
public class BinaryEntry<K extends Comparable<K>, V> extends Entry<K, V> implements
        Comparable<BinaryEntry<K, V>>, KVEntryInterface<K, V> {
    /**
     * The left child node of this node.
     */
    private BinaryEntry<K, V> left;

    /**
     * The right child node of this node.
     */
    private BinaryEntry<K, V> right;

    /**
     * The parent node of this node.
     */
    private BinaryEntry<K, V> parent;

    /**
     * The height of this node.
     */
    private int height;

    /**
     * The size of the left subtree.
     */
    private int leftTreeSize;

    /**
     * The size of the right subtree.
     */
    private int rightTreeSize;

    /**
     * Constructs a new Node with the given key and value.
     *
     * @param key The key to be stored in this node.
     * @param value The value to be stored in this node.
     */
    public BinaryEntry(K key, V value) {
        super(key, value);
        left = null;
        right = null;
        parent = null;
        height = 1;
    }

    /**
     * Retrieves the left node of this node.
     *
     * @return The left node of type Node, or null if there is no left node.
     */
    public BinaryEntry<K, V> getLeft() {
        return left;
    }

    /**
     * Retrieves the right node of this node.
     *
     * @return The right node of type Node, or null if there is no right node.
     */
    public BinaryEntry<K, V> getRight() {
        return right;
    }

    /**
     * Sets the left node of this node.
     *
     * @param left The node to set as the left node.
     */
    public void setLeft(BinaryEntry<K, V> left) {
        this.left = left;
    }

    /**
     * Sets the right node of this node.
     *
     * @param right The node to set as the right node.
     */
    public void setRight(BinaryEntry<K, V> right) {
        this.right = right;
    }

    /**
     * Retrieves the parent node of this node.
     *
     * @return The parent node of type Node, or null if there is no parent node.
     */
    public BinaryEntry<K, V> getParent() {
        return parent;
    }

    /**
     * Sets the parent node of this node.
     *
     * @param parent The node to set as the parent node.
     */
    public void setParent(BinaryEntry<K, V> parent) {
        this.parent = parent;
    }

    /**
     * Retrieves the height of this node.
     *
     * @return The height of this node.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of this node.
     *
     * @param height The new height to be set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Retrieves the size of the left subtree.
     *
     * @return the size of the left subtree
     */
    public int getLeftTreeSize() {
        return leftTreeSize;
    }

    /**
     * Retrieves the size of the right subtree.
     *
     * @return the size of the right subtree
     */
    public int getRightTreeSize() {
        return rightTreeSize;
    }

    /**
     * Sets the size of the left subtree.
     *
     * @param leftTreeSize the size of the left subtree
     */
    public void setLeftTreeSize(int leftTreeSize) {
        this.leftTreeSize = leftTreeSize;
    }

    /**
     * Sets the size of the right subtree.
     *
     * @param rightTreeSize the size of the right subtree
     */
    public void setRightTreeSize(int rightTreeSize) {
        this.rightTreeSize = rightTreeSize;
    }

    /**
     * Compares this BinaryNode instance with another BinaryNode based on their keys.
     * The comparison is performed using the natural ordering of the keys, as defined
     * by the {@code compareTo} method of the key's type.
     *
     * @param other The BinaryNode to be compared with this BinaryNode.
     * @return A negative integer, zero, or a positive integer as this BinaryNode's key
     *         is less than, equal to, or greater than the specified BinaryNode's key.
     */
    @Override
    public int compareTo(BinaryEntry<K, V> other) {
        return key.compareTo(other.getKey());
    }
}



