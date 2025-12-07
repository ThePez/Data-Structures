package structures;

import containers.Node;
import interfaces.List;

import java.util.Comparator;

/**
 * A generic implementation of a singly linked list, which provides
 * sequential access, addition, and removal capabilities for elements.
 * This class implements the List interface.
 *
 * @param <T> the type of elements held in this linked list
 */
public class LinkedList<T> extends AbstractList<T> implements List<T> {
    private Node<T> head;
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
        return insert(e, true);
    }

    @Override
    public boolean prepend(T e) {
       return insert(e, false);
    }

    @Override
    public boolean add(int idx, T e) {
        if (checkIndex(idx)) {
            return false;
        }

        if (idx == 0) {
            return prepend(e);
        } else if (idx == size) {
            return append(e);
        }

        Node<T> current = head;
        for (int i = 0; i < idx; i++) {
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

        Node<T> prev = null;
        Node<T> current = head;
        while (current != null && !current.getData().equals(o)) {
            prev = current;
            current = current.getNext();
        }

        if (current == null) {
            return false;
        }

        if (prev == null) {
            head = current.getNext();
            if (size == 1) {
                head = tail = null;
            }
        } else {
            prev.setNext(current.getNext());
            if (current == tail) {
                tail = prev;
            }
        }

        size--;
        return true;
    }

    @Override
    public T remove(int idx) {
        if (checkIndex(idx)) {
            return null;
        }

        if (idx == 0) {
            T data = head.getData();
            head = head.getNext();
            size--;
            return data;
        }

        Node<T> current = head;
        for (int i = 0; i < idx; i++) {
            current = current.getNext();
        }

        Node<T> next = current.getNext();
        current.setNext(next.getNext());
        size--;
        return next.getData();
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
     * Inserts a new element into the linked list. Depending on the value of the
     * {@code append} parameter, the element is either added to the head or the tail
     * of the list.
     *
     * @param e The element to be inserted into the linked list.
     * @param append A boolean flag indicating whether to append the element to the tail
     *               ({@code true}) or prepend it to the head ({@code false}) of the list.
     */
    private boolean insert(T e, boolean append) {
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
    private boolean emptyInsert(T e) {
        try {
            head = tail = new Node<>(e);
            size = 1;
            return true;
        } catch (OutOfMemoryError ignored) {
            return false;
        }
    }
}
