package structures;

import containers.Node;
import interfaces.List;
import java.util.Comparator;
import java.util.Objects;

/**
 * A generic implementation of a singly linked list, which provides
 * sequential access, addition, and removal capabilities for elements.
 * This class implements the List interface.
 *
 * @param <T> the type of elements held in this linked list
 */
public class LinkedList<T> extends AbstractList<T> implements List<T> {
    /**
     * The head reference of the linked list.
     */
    private Node<T> head;

    /**
     * The tail reference of the linked list.
     */
    private Node<T> tail;

    /**
     * Constructs an empty LinkedList with no elements.
     * The size is initialised to 0, and both the head and tail
     * references are set to null.
     */
    public LinkedList() {
        size = 0;
        head = tail = null;
    }

    @Override
    public boolean append(T e) {
        if (isEmpty()) {
            return emptyInsert(e);
        }

        return insert(e, true);
    }

    @Override
    public boolean prepend(T e) {
        if (isEmpty()) {
            return emptyInsert(e);
        }

        return insert(e, false);
    }

    @Override
    public boolean add(int idx, T e) {
        if (idx == 0) {
            return prepend(e);
        } else if (idx == size) {
            return append(e);
        } else if (checkIndex(idx)) {
            return false;
        }

        Node<T> current = head;
        for (int i = 0; i < idx - 1; i++) {
            current = current.getNext();
        }

        Node<T> next = current.getNext();
        try {
            Node<T> newNode = new Node<>(e);
            current.setNext(newNode);
            newNode.setNext(next);
            size++;
            return true;
        } catch (OutOfMemoryError ex) {
            return false;
        }
    }

    @Override
    public T get(int idx) {
        if (checkIndex(idx)) {
            return null;
        }

        Node<T> current = head;
        for (int i = 0; i < idx; i++) {
            current = current.getNext();
        }

        return current.getData();
    }

    @Override
    public T getFirst() {
        if (isEmpty()) {
            return null;
        }

        return head.getData();
    }

    @Override
    public T getLast() {
        if (isEmpty()) {
            return null;
        }

        return tail.getData();
    }

    @Override
    public T set(int idx, T e) {
        if (checkIndex(idx)) {
            return null;
        }

        if (idx == 0) {
            T data = head.getData();
            head.setData(e);
            return data;
        } else if (idx == size - 1) {
            T data = tail.getData();
            tail.setData(e);
            return data;
        }

        Node<T> current = head;
        for (int i = 0; i < idx; i++) {
            current = current.getNext();
        }

        T data = current.getData();
        current.setData(e);
        return data;
    }

    @Override
    public boolean remove(Object o) {
        if (isEmpty()) {
            return false;
        }

        Node<T> currentNode = head;
        Node<T> prevNode = null;
        while (currentNode != null) {
            // "Objects.equals" handles null data
            if (Objects.equals(currentNode.getData(), o)) {
                removeNode(currentNode, prevNode);
                return true;
            }

            // Get the next node
            prevNode = currentNode;
            currentNode = currentNode.getNext();
        }

        return false;
    }

    @Override
    public T remove(int idx) {
        if (idx < 0 || idx >= size) {
            return null;
        }

        return removeNode(idx).getData();
    }

    @Override
    public T removeLast() {
        return remove(size - 1);
    }

    @Override
    public T removeFirst() {
        return remove(0);
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        // use merge sort....
    }

    /**
     * Retrieves the node at the specified index within the linked list.
     * Iterates through the nodes starting from the head to find the node at the given index.
     *
     * @param index The position of the node to retrieve, starting from 0.
     * @return The node at the specified index if it exists, or null if the end of the list is reached.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    private Node<T> getNode(int index) {
        if (checkIndex(index)) {
            throw new IndexOutOfBoundsException();
        }

        int currentIndex;
        Node<T> currentNode;
        currentIndex = 0;
        currentNode = this.head;
        while (currentNode != null && currentIndex != index) {
            currentNode = currentNode.getNext();
            currentIndex++;
        }

        return currentNode;
    }

    /**
     * Removes a specified node from the linked list by updating references to bypass
     * the node. Adjusts the head and tail of the list if the removed node is the first
     * or last node. Decrements the size of the list.
     *
     * @param node The node to be removed from the linked list.
     * @param prev The previous node preceding the node to be removed. If the node to
     *             be removed is the head, this parameter should be null.
     * @return The node that was removed from the linked list.
     */
    private Node<T> removeNode(Node<T> node, Node<T> prev) {
        Node<T> next = node.getNext();
        if (prev != null) {
            // Next node or null if setting new tail
            prev.setNext(next);
        } else {
            // No previous node -> setting new head
            head = next;
        }

        if (next == null) {
            // No next node -> setting new tail
            tail = prev;
        }

        size--;
        return node;
    }

    /**
     * Removes the node at the specified index within the linked list.
     * If the index is zero, the head node is removed. Otherwise,
     * the method retrieves the node preceding the target node, then updates
     * pointers appropriately to exclude the node at the specified index
     * from the list.
     *
     * @param index The position of the node to remove, starting from 0.
     * @return The removed node at the specified index.
     */
    private Node<T> removeNode(int index) {
        if (index == 0) {
            return removeNode(getNode(0), null);

        }

        Node<T> prev = getNode(index - 1);
        return removeNode(prev.getNext(), prev);
    }

    /**
     * Inserts a new element into the linked list. Depending on the value of the
     * {@code append} parameter, the element is either added to the head or the tail
     * of the list.
     *
     * @param e The element to be inserted into the linked list.
     * @param append A boolean flag indicating whether to append the element to the tail
     *               ({@code true}) or prepend it to the head ({@code false}) of the list.
     */
    protected boolean insert(T e, boolean append) {
        if (isEmpty()) {
            return emptyInsert(e);
        }

        try {
            Node<T> newNode = new Node<>(e);
            if (append) {
                tail.setNext(newNode);
                tail = newNode;
            } else {
                newNode.setNext(head);
                head = newNode;
            }

            size++;
            return true;
        } catch (OutOfMemoryError ignored) {
            return false;
        }
    }

    /**
     * Inserts the first element into the LinkedList
     *
     * @param e the data to store in the new Node
     */
    protected boolean emptyInsert(T e) {
        try {
            head = tail = new Node<>(e);
            size = 1;
            return true;
        } catch (OutOfMemoryError ignored) {
            return false;
        }
    }
}
