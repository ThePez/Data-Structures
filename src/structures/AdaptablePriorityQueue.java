package structures;

import containers.ArrayEntry;
import interfaces.List;
import interfaces.PriorityQueue;

/**
 * AdaptablePriorityQueue class represents a priority queue data structure
 * with the additional capability of adapting to changes in the state of
 * its elements. It allows for efficient operations such as insertion,
 * removal, and priority updates. This class extends PQHeap and implements
 * the PriorityQueue interface to adhere to standard priority queue behaviour,
 * while providing additional functionality for enhanced adaptability.
 *
 * @param <T> the type of elements stored in the priority queue, which
 *            must implement the Comparable interface to support ordering.
 */
public class AdaptablePriorityQueue<T> extends
        PQHeap<ArrayEntry<Integer, T>> implements PriorityQueue<ArrayEntry<Integer, T>> {

    /**
     * Constructs a new instance of AdaptablePriorityQueue with default settings.
     * This constructor initializes the priority queue as an empty min-heap that
     * can efficiently adapt to element updates.
     */
    public AdaptablePriorityQueue() {
        super();
    }

    /**
     * Constructs a new instance of AdaptablePriorityQueue with the specified configuration.
     * This constructor initialises the priority queue as an empty heap. The behaviour of the heap
     * can be configured to operate as either a max-heap or a min-heap based on the input parameter.
     *
     * @param max true if the priority queue should operate as a max-heap, false if it should operate as a min-heap
     */
    public AdaptablePriorityQueue(boolean max) {
        super(max);
    }

    /**
     * Constructs a new AdaptablePriorityQueue with the specified configuration and initial capacity.
     * This constructor initialises the priority queue as an empty heap with configurable behaviour
     * (either max-heap or min-heap) and allows setting the initial capacity of the heap.
     *
     * @param max true if the priority queue should operate as a max-heap, false if it should operate as a min-heap
     * @param capacity the initial capacity of the priority queue
     */
    public AdaptablePriorityQueue(boolean max, int capacity) {
        super(max, capacity);
    }

    /**
     * Constructs a new instance of AdaptablePriorityQueue initialised with
     * the given configuration and array of initial elements. This constructor
     * allows the user to specify whether the priority queue operates as a max-heap
     * or min-heap, and initialises the internal storage by restoring the heap
     * property based on the provided array.
     *
     * @param max true if the priority queue should operate as a max-heap,
     *            false if it should operate as a min-heap
     * @param array the array of elements used to initialise the priority queue
     */
    public AdaptablePriorityQueue(boolean max, T[] array, int[] priorities) {
        super(max, array.length);

        if (array.length != priorities.length) {
            throw new IllegalArgumentException("Array lengths must match");
        }

        for (int i = 0; i < array.length; i++) {
            ArrayEntry<Integer, T> entry = new ArrayEntry<>(priorities[i], array[i]);
            entry.setIndex(i);
            data.append(entry);
        }

        bottomUpConstruction();
    }

    /**
     * Constructs a new instance of AdaptablePriorityQueue with the specified configuration
     * and a list of initial elements. This constructor allows the user to specify whether
     * the priority queue operates as a max-heap or a min-heap, and initialises the internal
     * storage by restoring the heap property based on the provided list.
     *
     * @param max true if the priority queue should operate as a max-heap, false if it should operate as a min-heap
     * @param list the list of elements used to initialise the priority queue
     */
    public AdaptablePriorityQueue(boolean max, List<T> list, List<Integer> priorities) {
        super(max, list.size());
        if (list.size() != priorities.size()) {
            throw new IllegalArgumentException("List lengths must match");
        }

        for (int i = 0; i < list.size(); i++) {
            ArrayEntry<Integer, T> entry = new ArrayEntry<>(priorities.get(i), list.get(i));
            entry.setIndex(i);
            data.append(entry);
        }

        bottomUpConstruction();
    }

    @Override
    public boolean add(ArrayEntry<Integer, T> element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }

        if (element.getIndex() != -1) {
            throw new IllegalArgumentException("Element already has an assigned index");
        }

        if (!data.append(element)) {
            return false;
        }

        element.setIndex(data.size() - 1);
        upHeap(data, data.size() - 1);
        return true;
    }

    @Override
    public ArrayEntry<Integer, T> poll() {
        return super.poll();
    }

    @Override
    public boolean remove(ArrayEntry<Integer, T> element) {
        if (isInvalidEntry(element)) {
            return false;
        }

        int index = element.getIndex();
        // Element is indeed in the heap at the set position
        swapElements(data, index, data.size() - 1);
        int oldSize = data.size() - 1;
        data.removeLast();
        if (index < oldSize) {
            downHeap(data, index, data.size());
            upHeap(data, index);
        }

        return true;
    }

    @Override
    public void clear() {
        super.clear();
    }

    /**
     * Updates the specified old element in the adaptable priority queue with the new element.
     * The method efficiently maintains the heap order and updates the internal position mapping accordingly.
     * If the old element is not present in the queue, the method returns false.
     *
     * @param oldElement the element to be replaced in the priority queue
     * @param newElement the new element to replace the old element
     * @return true if the update operation was successful, false if the old element was not found
     */
    public boolean update(ArrayEntry<Integer, T> oldElement, ArrayEntry<Integer,T> newElement) {
        if (isInvalidEntry(oldElement) || newElement == null) {
            return false;
        }

        int index = oldElement.getIndex();
        newElement.setIndex(index);
        data.set(index, newElement);
        upHeap(data, index);
        downHeap(data, index, data.size());
        return true;
    }

    @Override
    protected void swapElements(List<ArrayEntry<Integer, T>> list, int i, int j) {
        super.swapElements(list, i, j);
        list.get(i).setIndex(i);
        list.get(j).setIndex(j);
    }

    /**
     * Determines if the given entry is invalid in the context of the priority queue.
     * An entry is considered invalid if it is null, has an index of -1, or does not
     * match the element at its associated index in the data structure.
     *
     * @param element The entry to be validated. It represents a key-value pair with
     *                a specific index in the priority queue.
     * @return true if the entry is null, has an invalid index, or does not match its associated
     *         element in the data structure; false otherwise.
     */
    private boolean isInvalidEntry(ArrayEntry<Integer, T> element) {
        if (element == null) {
            return true;
        }

        int index = element.getIndex();
        if (index == -1) {
            return true;
        }

        return !data.get(index).equals(element);
    }
}

