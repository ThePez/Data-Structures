package containers;
/**
 * Represents a node in a doubly linked list data structure.
 * Each node holds a reference to its data, the next node, and the previous node.
 */
public class Node<T> {
    /**
     * Data stored in the node
     */
    protected T data;

    /**
     * Pointer to the next Node
     */
    private Node<T> next;

    /**
     * Constructs the node type with the given data payload
     *
     * @param data The payload data to store inside this node
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Retrieves the data stored within this node.
     *
     * @return The payload data stored within the node of type T.
     */
    public T getData() {
        return data;
    }

    /**
     * Updates the data stored within this node.
     *
     * @param newData The new data to be stored in the node. Must be of type T.
     */
    public void setData(T newData) {
        this.data = newData;
    }

    /**
     * Retrieves the next node referenced by this node in a doubly linked list.
     *
     * @return The next node of type Node, or null if there is no next node.
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * Updates the reference to the next node in the doubly linked list.
     *
     * @param next The node to set as the next node. It can be null if there is no next node.
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }
}
