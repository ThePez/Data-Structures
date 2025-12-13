package structures;

import containers.BinaryEntry;
import interfaces.List;
import interfaces.Map;

/**
 * OrderedMap is a data structure that implements a map using an AVL tree. It stores key-value pairs
 * in lexicographical or natural order, depending on the key type. The AVL property ensures that 
 * the tree remains balanced, guaranteeing O(log(n)) complexity for insertion, deletion, 
 * and search operations.
 *
 * @param <K> The type of the keys in this map. Keys must be Comparable, ensuring natural order.
 * @param <V> The type of values stored in this map.
 */
public class OrderedMap<K extends Comparable<K>, V> extends AbstractTree<K, V> implements Map<K, V> {

    /**
     * Defines the directions for performing a search operation, specifying the
     * type of comparison to be used in the search logic.
     */
    private enum SearchDirection {
        GREATER_OR_EQUAL, LESS_OR_EQUAL
    }

    /**
     * A record representing the result of a mutation operation.
     *
     * @param <A> The type of the new node resulting from the mutation.
     * @param <B> The type of the old value replaced during the mutation.
     * @param newNode The new node generated as a result of the mutation operation.
     * @param oldValue The old value that was replaced or affected by the mutation.
     */
    private record MutationResult<A, B>(A newNode, B oldValue) {}

    /**
     * Creates a new empty AVL tree.
     */
    public OrderedMap() {
        root = null;
        size = 0;
    }

    /**
     * Retrieves the height of the tree.
     *
     * @return the height of the tree as an integer
     */
    public int getHeight() {
        return root.getHeight();
    }

    /**
     * Removes all elements from the current data structure.
     * After calling this method, the data structure will be empty
     * and its size will be reset to zero.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return search(root, key) != null;
    }

    /**
     * Inserts the specified key-value pair into the map. If the key already exists, its
     * associated value is updated. Balances the tree after insertion to maintain the AVL property.
     *
     * @param key The key to be inserted or updated in the map.
     * @param value The value to be associated with the specified key.
     * @return The previous value associated with the specified key, or null if the key did not
     *         exist.
     */
    @Override
    public V put(K key, V value) {
        MutationResult<BinaryEntry<K, V>, V> ret = insert(root, key, value);
        root = ret.newNode;
        if (ret.oldValue == null) {
            size++;
        }

        return ret.oldValue;
    }

    /**
     * Retrieves the value associated with the specified key in the map.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key, or null if the key is not found.
     */
    @Override
    public V get(K key) {
        BinaryEntry<K, V> node = search(root, key);
        return node == null ? null : node.getValue();
    }

    /**
     * Removes the entry for the specified key from the data structure if present.
     *
     * @param key the key whose mapping is to be removed from the data structure
     * @return the previous value associated with the key, or null if there was no mapping for the
     *         key
     */
    @Override
    public V remove(K key) {
        MutationResult<BinaryEntry<K, V>, V> ret = delete(root, key);
        root = ret.newNode;
        if (ret.oldValue != null) {
            size--;
        }

        return ret.oldValue;
    }

    /**
     * Finds the next element in the collection that is greater than or equal to the specified key.
     *
     * @param key the key to compare against elements in the collection
     * @return the next element that is greater than or equal to the specified key or null if no
     *         such element exists
     */
    public V nextGeq(K key) {
        return findNext(key, SearchDirection.GREATER_OR_EQUAL);
    }

    /**
     * Finds the next element in the collection that is less than or equal to the specified key.
     *
     * @param key the key to compare against to find the next element that is less than or equal
     * @return the value associated with the next element less than or equal to the given key,
     *         or null if no such element exists
     */
    public V nextLeq(K key) {
        return findNext(key, SearchDirection.LESS_OR_EQUAL);
    }

    /**
     * Returns a list of keys within the specified range [lo, hi], inclusive.
     *
     * @param lo the lower bound of the range (inclusive)
     * @param hi the upper bound of the range (inclusive)
     * @return a list of keys within the range [lo, hi]
     */
    public List<K> keysInRange(K lo, K hi) {
        List<K> keys = new ArrayList<>();
        if (isEmpty() || lo.compareTo(hi) > 0) {
            return keys;
        }

        searchRange(lo, hi, keys, root);
        return keys;
    }

    // Private Helper methods

    /**
     * Recursively collects all keys within the specified range [lo, hi] from the binary tree
     * rooted at the given node and appends them to the provided list.
     *
     * @param lo   the lower bound of the range (inclusive)
     * @param hi   the upper bound of the range (inclusive)
     * @param keys the list where keys within the range are added
     * @param node the current node being traversed in the binary tree
     */
    private void searchRange(K lo, K hi, List<K> keys, BinaryEntry<K, V> node) {
        if (node == null) {
            return;
        }

        K key = node.getKey();
        int lowCompare = key.compareTo(lo);
        if (lowCompare > 0) {
            // Go left if the current node's key is greater than the lower bound
            searchRange(lo, hi, keys, node.getLeft());
        }

        int highCompare = key.compareTo(hi);
        if (lowCompare >= 0 && highCompare <= 0) {
            // Add the valid key to the list
            keys.append(key);
        }

        if (highCompare < 0) {
            // Go right if the current node's key is less than the upper bound
            searchRange(lo, hi, keys, node.getRight());
        }
    }

    /**
     * Finds the next value in the binary search tree based on the given key and search direction.
     *
     * @param key the key to find in the binary search tree.
     * @param dir the direction of the search, which determines if the search is for the
     *            value greater than or equal to, or less than or equal to the key.
     * @return the value associated with the next key according to the search direction, or
     *         null if no such value exists in the tree.
     */
    private V findNext(K key, SearchDirection dir) {
        if (root == null) {
            // The bottom of the tree and key wasn't found
            return null;
        }

        BinaryEntry<K, V> node = root;
        BinaryEntry<K, V> candidate = null;
        while (node != null) {
            int compare = key.compareTo(node.getKey());
            if (compare == 0) {
                // Exact match found
                return node.getValue();
            }

            if (dir == SearchDirection.GREATER_OR_EQUAL) {
                // Want key's >= input -> when you go left, the parent was larger
                if (compare < 0) {
                    // Possible best match
                    candidate = node;
                    // Key is smaller, go to the left
                    node = node.getLeft();
                } else {
                    node = node.getRight();
                }
            } else {
                // Want key's <= input -> When you go right, the parent was smaller
                if (compare > 0) {
                    // Possible best match
                    candidate = node;
                    // Key is larger, go to the right
                    node = node.getRight();
                } else {
                    node = node.getLeft();
                }
            }
        }

        // If a candidate was found, return its value, else null
        return candidate != null ? candidate.getValue() : null;
    }

    /**
     * Searches for a node with the specified key in a binary tree rooted at the provided node.
     * The search is performed based on the natural ordering of the keys.
     *
     * @param root The root node of the binary tree/subtree where the search is performed.
     * @param key The key to search for in the binary tree.
     * @return The node containing the specified key, or null if the key is not found
     *         in the tree.
     * @throws IllegalArgumentException if the key is null.
     */
    private BinaryEntry<K, V> search(BinaryEntry<K, V> root, K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (root == null) {
            // The bottom of the tree and key wasn't found
            return null;
        }

        int compare = key.compareTo(root.getKey());
        if (compare < 0) {
            return search(root.getLeft(), key);
        } else if (compare > 0) {
            return search(root.getRight(), key);
        }

        return root;
    }

    /**
     * Inserts a key-value pair into the binary tree starting from the provided root node.
     * If the key already exists, its associated value is updated. If the key does not exist,
     * a new node is created and inserted while maintaining the balance of the tree.
     *
     * @param subRoot The root node of the subtree where the key-value pair should be inserted.
     * @param key The key to be inserted into the tree.
     * @param value The value associated with the key being inserted.
     * @return The root of the modified binary tree/subtree after insertion.
     * @throws IllegalArgumentException if the key is null.
     */
    private MutationResult<BinaryEntry<K, V>, V> insert(BinaryEntry<K, V> subRoot, K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (subRoot == null) {
            BinaryEntry<K, V> newNode = new BinaryEntry<>(key, value);
            newNode.setHeight(1);
            return new MutationResult<>(newNode, null);
        }

        MutationResult<BinaryEntry<K, V>, V> result;
        int compare = key.compareTo(subRoot.getKey());
        if (compare < 0) {
            // Key is smaller, go to the left
            result = insert(subRoot.getLeft(), key, value);
            subRoot.setLeft(result.newNode());
        } else if (compare > 0) {
            // Key is larger, go to the right
            result = insert(subRoot.getRight(), key, value);
            subRoot.setRight(result.newNode());
        } else {
            // Key is equal, update the value
            V old = subRoot.getValue();
            subRoot.setValue(value);
            return new MutationResult<>(subRoot, old);
        }

        // Rebalance the tree
        return new MutationResult<>(rebalance(subRoot), result.oldValue());
    }

    /**
     * Deletes a node with the specified key from the binary tree rooted at the given node.
     * Balances the tree after deletion to maintain its properties.
     *
     * @param root The root node of the binary tree/subtree where the key is to be deleted.
     * @param key The key of the node to be deleted from the tree.
     * @return The root of the modified binary tree/subtree after deletion, or null if the tree
     *         becomes empty.
     * @throws IllegalArgumentException if the key is null.
     */
    private MutationResult<BinaryEntry<K, V>, V> delete(BinaryEntry<K, V> root, K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (root == null) {
            return new MutationResult<>(null, null);
        }

        int compare = key.compareTo(root.getKey());
        MutationResult<BinaryEntry<K, V>, V> result;
        if (compare < 0) {
            // Key is smaller, go to the left
            result = delete(root.getLeft(), key);
            root.setLeft(result.newNode());
            return new MutationResult<>(rebalance(root), result.oldValue());
        } else if (compare > 0) {
            // Key is larger, go to the right
            result = delete(root.getRight(), key);
            root.setRight(result.newNode());
            return new MutationResult<>(rebalance(root), result.oldValue());
        } else {
            // Match found
            V old = root.getValue();
            if (root.getLeft() == null || root.getRight() == null) {
                // 1 Child or less, This is where deletes actually happen
                root = (root.getLeft() != null) ? root.getLeft() : root.getRight();
                return new MutationResult<>(root, old);
            } else {
                // 2 Children -> Not deleting here, just swaps
                BinaryEntry<K, V> successor = deleteHelper(root.getRight());
                swapNodes(root, successor);
                // Delete the successor
                MutationResult<BinaryEntry<K, V>, V> delResult = delete(root.getRight(), key);
                root.setRight(delResult.newNode());
                return new MutationResult<>(rebalance(root), old);
            }
        }
    }

    /**
     * Updates the height of the specified binary node based on the heights of its
     * left and right child nodes. The height of the node is set to one more than
     * the maximum height of its children. If a child node is null, its height is
     * considered as 0.
     *
     * @param node The binary node whose height is to be updated. This node must not be null.
     * @throws IllegalArgumentException if the node is null.
     */
    private void updateNodeHeight(BinaryEntry<K, V> node) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null.");
        }

        int leftHeight = (node.getLeft() == null) ? 0 : node.getLeft().getHeight();
        int rightHeight = (node.getRight() == null) ? 0 : node.getRight().getHeight();
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
    }

    /**
     * Computes the balance factor of the specified binary tree node. The balance factor is defined
     * as the difference in height between the left and right subtrees of the node.
     * <br>
     * A positive value indicates the left subtree is taller. A negative value indicates the right
     * subtree is taller. 0 indicates both subtrees have the same height.
     * <br>
     * @param node The binary tree node for which to calculate the balance factor.
     * @return The integer value representing the balance factor of the node.
     */
    private int getBalance(BinaryEntry<K, V> node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = (node.getLeft() == null) ? 0 : node.getLeft().getHeight();
        int rightHeight = (node.getRight() == null) ? 0 : node.getRight().getHeight();
        return leftHeight - rightHeight;
    }

    /**
     * Rebalances the binary tree rooted at the given node to ensure it maintains
     * a balanced state. If the difference in height between the left and right
     * subtrees (balance factor) exceeds allowed limits, appropriate rotations
     * (left or right) are performed to restore balance.
     *
     * @param root The root node of the binary tree/subtree to be rebalanced.
     * @return The root of the modified binary tree/subtree after rebalancing is performed.
     */
    private BinaryEntry<K, V> rebalance(BinaryEntry<K, V> root) {
        if (root == null) {
            throw new IllegalArgumentException("Root node cannot be null.");
        }

        // Update the height
        updateNodeHeight(root);

        int balance = getBalance(root);
        if (balance > 1) {
            // The left subtree is taller
            if (getBalance(root.getLeft()) < 0) {
                // Left-Right case
                root.setLeft(leftRotate(root.getLeft()));
            }

            // Left-Left case, or stage 2 of the Left-Right case
            return rightRotate(root);
        } else if (balance < -1) {
            // The right subtree is taller
            if (getBalance(root.getRight()) > 0) {
                // Right-Left case
                root.setRight(rightRotate(root.getRight()));
            }

            // Right-Right case or stage 2 of the Right-Left case
            return leftRotate(root);
        }

        return root;
    }

    /**
     * Performs a left rotation on the given binary node. The rotation restructures
     * the subtree rooted at the given node to maintain balance, swapping the positions of
     * the given node and its right child and adjusting their associated subtrees accordingly.
     * The heights of the affected nodes are updated after the rotation.
     *
     * @param a The root of the subtree on which the left rotation is performed.
     *          This node must not be null and should have a non-null right child.
     * @return The node that becomes the new root of the subtree after the rotation.
     */
    private BinaryEntry<K, V> leftRotate(BinaryEntry<K, V> a) {
        // Protect the method against invalid arguments
        if (a == null || a.getRight() == null) {
            throw new IllegalArgumentException("Node or right child cannot be null.");
        }

        // Get the new root
        BinaryEntry<K, V> b = a.getRight();
        // Store the tail of the right subtree
        BinaryEntry<K, V> tail = b.getLeft();
        // Set the new root's left child to the old root
        b.setLeft(a);
        // Update the right child of the old root to the tail of the right subtree
        a.setRight(tail);
        // Update the heights of the affected nodes
        updateNodeHeight(a);
        updateNodeHeight(b);
        // Return the new root
        return b;
    }

    /**
     * Performs a right rotation on the given binary node. The rotation restructures
     * the subtree rooted at the specified node to maintain balance, swapping the positions
     * of the given node and its left child and adjusting their associated subtrees accordingly.
     * The heights of the affected nodes are updated after the rotation.
     *
     * @param a The root of the subtree on which the right rotation is performed.
     *          This node must not be null and should have a non-null left child.
     * @return The node that becomes the new root of the subtree after the rotation.
     */
    private BinaryEntry<K, V> rightRotate(BinaryEntry<K, V> a) {
        // Protect the method against invalid arguments
        if (a == null || a.getLeft() == null) {
            throw new IllegalArgumentException("Node or left child cannot be null.");
        }

        // Get the new root
        BinaryEntry<K, V> b = a.getLeft();
        // Store the tail of the left subtree
        BinaryEntry<K, V> tail = b.getRight();
        // Set the new root's right child to the old root
        b.setRight(a);
        // Update the left child of the old root to the tail of the left subtree
        a.setLeft(tail);
        // Update the heights of the affected nodes
        updateNodeHeight(a);
        updateNodeHeight(b);
        // Return the new root
        return b;
    }

}