package containers;

/**
 * Node that contains pointer to both the next and previous Nodes.
 *
 * @param <T> the type of data stored in the Node
 */
public class DoubleNode<T> extends Node<T>{

    /**
     * Pointer to the previous Node
     */
    private DoubleNode<T> prev;

    /**
     * Pointer to the next Node
     */
    private DoubleNode<T> next;

    /**
     * Constructs the node type with the given data payload
     *
     * @param data The payload data to store inside this node
     */
    public DoubleNode(T data) {
        super(data);
        this.data = data;
        prev = null;
        next = null;
    }

    /**
     * Retrieves the previous node referenced by this node in a doubly linked list.
     *
     * @return The previous node of type Node, or null if there is no previous node.
     */
    public DoubleNode<T> getPrev() {
        return prev;
    }

    /**
     * Sets the previous node reference for this node in the doubly linked list.
     *
     * @param prev The node to be set as the previous node. It can be null if there is no previous node.
     */
    public void setPrev(DoubleNode<T> prev) {
        this.prev = prev;
    }


    /**
     * Retrieves the next node referenced by this node in a doubly linked list.
     *
     * @return The next node of type Node, or null if there is no next node.
     */
    public DoubleNode<T> getNext() {
        return next;
    }

    /**
     * Updates the reference to the next node in the doubly linked list.
     *
     * @param next The node to set as the next node. It can be null if there is no next node.
     */
    public void setNext(DoubleNode<T> next) {
        this.next = next;
    }

}
