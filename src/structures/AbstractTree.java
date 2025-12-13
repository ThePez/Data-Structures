package structures;

import containers.BinaryEntry;
import containers.Entry;
import interfaces.List;
import interfaces.Map;

import java.util.function.Function;

/**
 * AbstractTree is an abstract implementation of a binary tree structure that extends
 * AbstractMap and implements the Map interface. It is designed to store key-value pairs,
 * where keys must be comparable.
 * <br>
 * The class provides various traversal methods (in-order, pre-order, post-order) and utility
 * functions for manipulating nodes in the binary tree.
 *
 * @param <K> the type of keys maintained by this tree (must be comparable)
 * @param <V> the type of mapped values
 */
public abstract class AbstractTree<K extends Comparable<K>, V> extends AbstractMap<K, V>
        implements Map<K, V> {

    /**
     * The root node of the binary tree.
     */
    protected BinaryEntry<K, V> root;

    @Override
    public List<K> getKeys() {
        return inOrder(BinaryEntry::getKey);
    }

    @Override
    public List<V> getValues() {
        return inOrder(BinaryEntry::getValue);
    }

    @Override
    public List<Entry<K, V>> getEntries() {
        return inOrder(node -> node);
    }

    /**
     * Finds the in-order predecessor of the given node in the binary tree.
     * The in-order predecessor is the node that comes immediately before the given node
     * in an in-order traversal of the binary tree.
     *
     * @param node The node for which the in-order predecessor is to be determined.
     *             Must not be null.
     * @return The in-order predecessor of the given node, or null if no predecessor exists.
     */
    protected BinaryEntry<K, V> inOrderPredecessor(BinaryEntry<K, V> node) {
        BinaryEntry<K, V> predecessor = null;

        if (node.getLeft() != null) {
            predecessor = node.getLeft();
            while (predecessor.getRight() != null) {
                predecessor = predecessor.getRight();
            }

            return predecessor;
        }

        K key = node.getKey();
        while (root != node) {
            if (key.compareTo(root.getKey()) > 0) {
                predecessor = root;
                root = root.getRight();
            } else {
                root = root.getLeft();
            }
        }

        return predecessor;
    }

    /**
     * Finds the in-order successor of the given node in the binary tree.
     * The in-order successor is the node that comes immediately after the given node
     * in an in-order traversal of the binary tree.
     *
     * @param node The node for which the in-order successor is to be determined.
     *             Must not be null.
     * @return The in-order successor of the given node, or null if no successor exists.
     */
    protected BinaryEntry<K, V> inOrderSuccessor(BinaryEntry<K, V> node) {
        if (node.getRight() != null) {
            return deleteHelper(node.getRight());
        }

        BinaryEntry<K, V> successor = null;
        K key = node.getKey();
        while (root != node) {
            if (key.compareTo(root.getKey()) < 0) {
                successor = root;
                root = root.getLeft();
            } else {
                root = root.getRight();
            }
        }

        return successor;
    }

    /**
     * Finds the in-order successor for the parent of the given binary node during the process of
     * deletion. The input to this function is the right child of the node to find the successor of.
     *
     * @param node The right child of the binary node for which the in-order successor is to be
     *             found.
     * @return The in-order successor for the parent of the given binary node.
     */
    protected BinaryEntry<K, V> deleteHelper(BinaryEntry<K, V> node) {
        BinaryEntry<K, V> successor = node;
        while (successor.getLeft() != null) {
            successor = successor.getLeft();
        }

        return successor;
    }

    /**
     * Swaps the key and value of the given node and its designated successor node.
     * This is typically used during operations such as node replacement in binary
     * trees, where the key and value of a pair of one node need to be exchanged with another.
     *
     * @param node      The node whose key and value will be swapped with the successor node.
     * @param successor The node that is intended to exchange its key and value with the given node.
     */
    protected void swapNodes(BinaryEntry<K, V> node, BinaryEntry<K, V> successor) {
        // Swap keys
        K successorKey = successor.getKey();
        successor.setKey(node.getKey());
        node.setKey(successorKey);
        // Swap values
        V successorValue = successor.getValue();
        successor.setValue(node.getValue());
        node.setValue(successorValue);
    }

    /**
     * Traverses the binary tree in in-order and applies the provided mapping function
     * to each node, collecting the results into a list.
     *
     * @param <T> the type of the elements in the resulting list
     * @param map the function used to map each binary tree node to an element of type T
     * @return a list containing the mapped elements from the in-order traversal of the tree
     */
    protected <T> List<T> inOrder(Function<BinaryEntry<K, V>, T> map) {
        SimpleStack<BinaryEntry<K, V>> stack = new SimpleStack<>();
        List<T> items = new ArrayList<>();
        BinaryEntry<K, V> currentNode = root;
        while (currentNode != null || !stack.isEmpty()) {
            while (currentNode != null) {
                // Go as far left as possible
                stack.push(currentNode);
                currentNode = currentNode.getLeft();
            }

            // Add mapped value to the array
            currentNode = stack.pop();
            items.append(map.apply(currentNode));
            // Move to the right subtree
            currentNode = currentNode.getRight();
        }

        return items;
    }

    /**
     * Traverses the binary tree in pre-order and applies the provided mapping function
     * to each node, collecting the results into a dynamic array.
     *
     * @param <T> the type of the elements in the resulting dynamic array
     * @param map the function used to map each binary tree node to an element of type T
     * @return an array containing the mapped elements from the pre-order traversal of the tree
     */
    protected <T> List<T> preOrder(Function<BinaryEntry<K, V>, T> map) {
        SimpleStack<BinaryEntry<K, V>> stack = new SimpleStack<>();
        List<T> items = new ArrayList<>();
        if (root != null) {
            stack.push(root);
            while (!stack.isEmpty()) {
                BinaryEntry<K, V> currentNode = stack.pop();
                items.append(map.apply(currentNode));
                if (currentNode.getRight() != null) {
                    stack.push(currentNode.getRight());
                }

                if (currentNode.getLeft() != null) {
                    stack.push(currentNode.getLeft());
                }
            }
        }

        return items;
    }

    /**
     * Traverses the binary tree in post-order and applies the provided mapping function
     * to each node, collecting the results into a list.
     *
     * @param <T> the type of elements in the resulting list
     * @param map the function used to map each binary tree node to an element of type T
     * @return a list containing the mapped elements from the post-order traversal of the tree
     */
    protected <T> List<T> postOrder(Function<BinaryEntry<K, V>, T> map) {
        SimpleStack<BinaryEntry<K, V>> stack = new SimpleStack<>();
        List<T> items = new ArrayList<>();
        BinaryEntry<K, V> currentNode = root;
        stack.push(currentNode);
        while (!stack.isEmpty()) {
            currentNode = stack.pop();
            items.prepend(map.apply(currentNode));

            if (currentNode.getLeft() != null) {
                stack.push(currentNode.getLeft());
            }

            if (currentNode.getRight() != null) {
                stack.push(currentNode.getRight());
            }
        }

        return items;
    }

}
