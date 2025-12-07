package structures;

import containers.DoubleNode;
import interfaces.List;
import java.util.Objects;

/**
 * A generic implementation of a doubly linked list. This implementation provides
 * methods to manipulate and traverse the elements in a doubly linked list, where
 * each node holds references to both its previous and next nodes.
 *
 * @param <T> the type of elements stored in the DoublyLinkedList
 */
public class DoublyLinkedList<T> extends LinkedList<T> implements List<T> {

    /**
     * Head of the list
     */
    private DoubleNode<T> head;

    /**
     * Tail of the list
     */
    private DoubleNode<T> tail;

    /**
     * Constructs an empty linked list
     */
    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean append(T t) {
        if (isEmpty()) {
            return emptyInsert(t);
        }

        return insert(t, true);
    }

    @Override
    public boolean prepend(T t) {
        if (isEmpty()) {
            return  emptyInsert(t);
        }

        return insert(t, false);
    }

    @Override
    public boolean add(int index, T data) {
        if (index == 0) { // Case 1: The index is the head
            return prepend(data);
        } else if (index == size) { // Case 2: The index is the tail
            return append(data);
        }

        DoubleNode<T> oldNode = getNode(index);
        DoubleNode<T> prev = oldNode.getPrev();
        DoubleNode<T> node = new DoubleNode<>(data);
        prev.setNext(node);
        node.setPrev(prev);
        node.setNext(oldNode);
        oldNode.setPrev(node);
        size++;
        return true;
    }

    @Override
    public T get(int index) {
        return getNode(index).getData();
    }

    @Override
    public T set(int index, T t) {
        DoubleNode<T> node = getNode(index);
        T oldData = node.getData();
        node.setData(t);
        return oldData;
    }

    @Override
    public T remove(int index) {
        return removeNode(index).getData();
    }

    @Override
    public boolean remove(Object t) {
        if (isEmpty()) {
            return false;
        }

        DoubleNode<T> currentNode = head;
        boolean remove = false;
        while (currentNode != null) {
            // "Objects.equals" handles null data
            if (Objects.equals(currentNode.getData(), t)) {
                remove = true;
                break;
            }

            // Get the next node
            currentNode = currentNode.getNext();
        }

        // Data match was found -> remove the node and return true
        if (remove) {
            DoubleNode<T> prev = currentNode.getPrev();
            DoubleNode<T> next = currentNode.getNext();
            prev.setNext(next);
            next.setPrev(prev);
            size--;
            return true;
        }

        return false;
    }

    /**
     * Retrieves the node at the specified index in the doubly linked list.
     * Traverses from the head if the index is in the first half of the list,
     * or from the tail if the index is in the second half, for optimised access.
     *
     * @param index the zero-based position of the node to retrieve
     * @return the node at the specified index
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    private DoubleNode<T> getNode(int index) {
        if (checkIndex(index)) {
            throw new IndexOutOfBoundsException();
        }

        int middle = size / 2;
        int currentIndex;
        DoubleNode<T> currentNode;
        if (index < middle) { // First half of the list
            currentIndex = 0;
            currentNode = this.head;
            while (currentNode != null && currentIndex != index) {
                currentNode = currentNode.getNext();
                currentIndex++;
            }
        } else { // Second half of the list
            currentIndex = size - 1;
            currentNode = this.tail;
            while (currentNode != null && currentIndex != index) {
                currentNode = currentNode.getPrev();
                currentIndex--;
            }
        }

        return currentNode;
    }

    /**
     * Removes the node at the specified index from the doubly linked list.
     * Updates the previous and next pointers of adjacent nodes as necessary
     * and adjusts the head or tail references if the removed node was at the
     * beginning or the end of the list. Decrements the size of the list.
     *
     * @param index the zero-based position of the node to remove
     * @return the removed node containing its original data
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    private DoubleNode<T> removeNode(int index) {
        DoubleNode<T> node = getNode(index); // handles bad index's
        DoubleNode<T> prev = node.getPrev();
        DoubleNode<T> next = node.getNext();
        if (prev != null) {
            // Next node or null if setting new tail
            prev.setNext(next);
        }

        if (next != null) {
            // Previous node or null if setting new head
            next.setPrev(prev);
        }

        if (index == 0) {
            head = next;
        }

        if (index == size - 1) {
            tail = prev;
        }

        size--;
        return node;
    }

    /**
     * Inserts the specified element into the doubly linked list. The {code}append parameter
     *  determines the position of insertion. If the list is empty, the method delegates
     * to emptyInsert. If the list is not empty, it inserts the element at the appropriate
     * end of the list (head or tail).
     *
     * @param t the element to be inserted into the linked list
     * @param append if true, the element is appended to the end of the list;
     *               if false, the element is prepended to the beginning of the list
     * @return true if the insertion was successful, false if an OutOfMemoryError occurs
     */
    private boolean insert(T t, boolean append) {
        if (isEmpty()) {
            return emptyInsert(t);
        }

        try {
            DoubleNode<T> node = new DoubleNode<>(t);
            if (append) {
                tail.setNext(node);
                node.setPrev(tail);
                tail = node;
            } else {
                node.setNext(head);
                head.setPrev(node);
                head = node;
            }

            size++;
            return true;
        } catch (OutOfMemoryError ex) {
            return false;
        }
    }

    /**
     * Inserts the given element into the doubly linked list when the list is empty.
     * Updates the head and tail references to point to the newly created node and
     * sets the size of the list to 1.
     *
     * @param t the element to be added to the linked list
     * @return true if the insertion was successful, false if an OutOfMemoryError occurs
     */
    private boolean emptyInsert(T t) {
        try {
            head = tail = new DoubleNode<>(t);
            size = 1;
            return true;
        } catch (OutOfMemoryError e) {
            return false;
        }
    }
}

