package structures;

import interfaces.List;
import interfaces.PriorityQueue;

/**
 * A generic implementation of a priority queue using a binary heap structure.
 * The heap can function as either a max-heap or a min-heap based on its configuration.
 * This class ensures that the highest-priority element (depending on the heap type)
 * is always at the root of the heap.
 *
 * @param <T> the type of elements stored in the heap, which must implement the {@code Comparable<T>} interface
 */
public class PQHeap<T extends Comparable<T>> implements PriorityQueue<T> {

    /**
     * An array used as the internal data storage for the heap. It dynamically grows as elements
     * are added beyond its current capacity. The elements are of a generic type, allowing the heap
     * to store objects of any type.
     */
    protected final List<T> data;

    /**
     * Indicates whether the heap is configured to operate as a max-heap.
     */
    protected boolean maxHeap = false;

    /**
     * Constructs a new instance of PQHeap with default settings.
     * By default, the heap is initialised as an empty data structure, and
     * the type of heap (max-heap or min-heap) is not specified.
     */
    public PQHeap() {
        data = new ArrayList<>();
    }

    /**
     * Constructs a new instance of PQHeap with the specified configuration.
     * The heap can be set to operate as either a max-heap or a min-heap.
     *
     * @param max true if the heap should operate as a max-heap, false if it should operate as a min-heap
     */
    public PQHeap(boolean max) {
        this();
        maxHeap = max;
    }

    /**
     * Constructs a new PQHeap with the specified configuration and initial capacity.
     * The heap can be configured to function as either a max-heap or a min-heap.
     *
     * @param max true if the heap should operate as a max-heap, false if it should operate as a min-heap
     * @param initialCapacity the initial capacity of the heap
     */
    public PQHeap(boolean max, int initialCapacity) {
        maxHeap = max;
        data = new ArrayList<>(initialCapacity);
    }

    /**
     * Constructs a new PQHeap with the specified configuration and an initial set of elements.
     * The heap can be configured to function as either a max-heap or a min-heap. The provided
     * array is used to initialise the heap, and the heap property is restored based on the
     * specified configuration through a bottom-up construction process.
     *
     * @param max true if the heap should operate as a max-heap, false if it should operate as a min-heap
     * @param array the array of elements used to initialise the heap
     */
    public PQHeap(boolean max, T[] array) {
        maxHeap = max;
        data = new ArrayList<>(array);
        bottomUpConstruction();
    }

    /**
     * Constructs a new PQHeap with the specified heap type and initial elements. The heap can be
     * configured to function as either a max-heap or a min-heap. The provided list is used to
     * initialise the heap, and the heap property is restored based on the specified configuration.
     *
     * @param max true if the heap should operate as a max-heap, false if it should operate as a min-heap
     * @param list the list of elements used to initialise the heap
     */
    public PQHeap(boolean max, List<T> list) {
        this.maxHeap = max;
        data = new ArrayList<>(list);
        bottomUpConstruction();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean add(T element) {
        if (element == null) {
            // No null allowed in the heap
            throw new NullPointerException("Element cannot be null");
        }

        if (!data.append(element)) {
            return false;
        }

        // Fix any violations in the Heap caused by the new element
        upHeap(data, data.size() - 1);
        return true;
    }

    @Override
    public boolean remove(T element) {
        if (element == null) {
            return false;
        }

        // Find the first matching element
        int currentSize = data.size();
        for (int i = 0; i < currentSize; i++) {
            if (data.get(i).equals(element)) {
                // Found the element, so remove it by overriding it with the last element
                swapElements(data, i, data.size() - 1);
                data.removeLast();
                // Now fix any violations in the Heap caused by the swapped last element
                if (i < currentSize - 1) {
                    downHeap(data, i, data.size()); // Check down first
                    upHeap(data, i); // Then check up
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            return null;
        }

        return data.getFirst();
    }

    @Override
    public T poll() {
        T value;
        int size = data.size();
        switch (size) {
            case 0 -> value = null;
            case 1 -> value = data.removeFirst();
            default -> {
                value = data.get(0);
                swapElements(data, 0, size - 1);
                data.removeLast();
                downHeap(data, 0, size - 1);
            }
        }

        return value;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public List<T> sort() {
        if (isEmpty()) {
            return new ArrayList<>();
        }

        List<T> sorted = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            sorted.append(data.get(i));
        }

        for (int i = sorted.size() - 1; i >= 0; i--) {
            swapElements(sorted, 0, i);
            downHeap(sorted, 0, i);
        }

        return sorted;
    }

    /**
     * Performs a bottom-up construction of the heap to establish the heap property
     * for the entire data structure. Starting from the parent node of the last element
     * and working backwards, the method invokes the `downHeap` operation to ensure
     * that each subtree satisfies the heap property.
     */
    protected void bottomUpConstruction() {
        for (int i = parent(data.size() - 1); i >= 0; i--) {
            downHeap(data, i, data.size());
        }
    }

    /**
     * Computes the index of the parent node for the given index in a heap.
     *
     * @param index the index of the current node
     * @return the index of the parent node in the heap
     */
    protected int parent(int index) {
        return (index - 1) / 2;
    }

    /**
     * Computes the index of the left child node for the given index in a heap.
     *
     * @param index the index of the current node
     * @return the index of the left child node in the heap
     */
    protected int leftChild(int index) {
        return 2 * index + 1;
    }

    /**
     * Computes the index of the right child node for the given index in a heap.
     *
     * @param index the index of the current node
     * @return the index of the right child node in the heap
     */
    protected int rightChild(int index) {
        return 2 * index + 2;
    }

    /**
     * Swaps the positions of two elements in the internal data array of the heap.
     *
     * @param i the index of the first element to be swapped
     * @param j the index of the second element to be swapped
     */
    protected void swapElements(List<T> array, int i, int j) {
        T temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }

    /**
     * Restores the heap property by moving an element up in the heap if it is out of order.
     * The method ensures that the element at the specified index is compared with its parent
     * and swapped if necessary. It recursively continues the process until the heap property
     * is restored or the element reaches the root of the heap.
     *
     * @param array the array representing the heap where the adjustment takes place
     * @param index the index of the element to be moved up in the heap
     */
    protected void upHeap(List<T> array, int index) {
        if (index == 0) {
            // Root can't go up
            return;
        }

        int parentIndex = parent(index);
        if (maxHeap) {
            if (array.get(index).compareTo(array.get(parentIndex)) > 0) {
                swapElements(array, index, parentIndex);
                upHeap(array, parentIndex);
            }
        } else {
            // Min Heap
            if (array.get(index).compareTo(array.get(parentIndex)) < 0) {
                swapElements(array, index, parentIndex);
                upHeap(array, parentIndex);
            }
        }
    }

    /**
     * Restores the heap property by moving an element down in the heap if it is out of order.
     * Depending on the configuration of the heap (max-heap or min-heap), the method ensures
     * the element compares correctly with its children and swaps positions if necessary.
     * The process continues recursively until the heap property is restored or the element
     * becomes a leaf node.
     *
     * @param array  the array representing the heap where the adjustment takes place
     * @param index  the index of the element to be moved down in the heap
     * @param length the effective size of the heap during the operation
     */
    protected void downHeap(List<T> array, int index, int length) {
        int leftIdx = leftChild(index);
        if (leftIdx >= length) {
            // No children, so the node is a leaf
            return;
        }

        // Decide which child to swap with
        int swapIdx = getSwapIdx(array, index, leftIdx, length);

        if (maxHeap) {
            if (array.get(index).compareTo(array.get(swapIdx)) < 0) {
                swapElements(array, index, swapIdx);
                downHeap(array, swapIdx, length);
            }
        } else {
            // Min Heap
            if (array.get(index).compareTo(array.get(swapIdx)) > 0) {
                swapElements(array, index, swapIdx);
                downHeap(array, swapIdx, length);
            }
        }
    }

    /**
     * Determines the index of the child node to swap with, based on the heap's configuration
     * max-heap or min-heap. If both children are equal, the right child is chosen.
     *
     * @param array the array representing the heap
     * @param index the index of the current node in the heap
     * @param leftIdx the current node's left child index
     * @param length the effective size of the heap during the operation
     * @return the index of the child node to swap with, either the left or right child
     */
    protected int getSwapIdx(List<T> array, int index, int leftIdx, int length) {
        int rightIdx = rightChild(index);
        int swapIdx = leftIdx;
        if (rightIdx < length) {
            if (maxHeap) {
                if (array.get(rightIdx).compareTo(array.get(leftIdx)) >= 0) {
                    // The right child is larger or equal to the left child
                    swapIdx = rightIdx;
                }
            } else {
                if (array.get(rightIdx).compareTo(array.get(leftIdx)) <= 0) {
                    // The right child is smaller or equal to the left child
                    swapIdx = rightIdx;
                }
            }
        }

        return swapIdx;
    }

}
