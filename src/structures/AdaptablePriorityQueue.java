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
     * Maintains the mapping of each element in the priority queue to
     * its current index within the underlying heap structure.
     */
    private final HashMap<ArrayEntry<Integer, T>, Integer> positions = new HashMap<>();

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
            positions.put(new ArrayEntry<>(priorities[i], array[i]), i);
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
            positions.put(new ArrayEntry<>(priorities.get(i), list.get(i)), i);
        }

        bottomUpConstruction();
    }

    @Override
    public boolean add(ArrayEntry<Integer, T> element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }



        if (!data.append(element)) {
            return false;
        }

        positions.put(element, data.size() - 1);
        upHeap(data, data.size() - 1);
        return true;
    }

    @Override
    public ArrayEntry<Integer, T> poll() {
        ArrayEntry<Integer, T> value = super.poll();

        if (value != null) {
            positions.remove(value);
        }

        return value;
    }

    @Override
    public boolean remove(ArrayEntry<Integer, T> element) {
        Integer index = positions.get(element);
        if (index == null) {
            return false;
        }

        swapElements(data, index, data.size() - 1);
        positions.remove(element);
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
        positions.clear();
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
        Integer index = positions.get(oldElement);
        if (index == null) {
            return false;
        }

        positions.remove(oldElement);
        positions.put(newElement, index);
        data.set(index, newElement);
        upHeap(data, index);
        downHeap(data, index, data.size());
        return true;
    }

    @Override
    protected void swapElements(List<ArrayEntry<Integer, T>> list, int i, int j) {
        super.swapElements(list, i, j);
        positions.put(list.get(i), i);
        positions.put(list.get(j), j);
    }
}

