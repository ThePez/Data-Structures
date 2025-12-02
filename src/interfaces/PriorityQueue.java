package interfaces;

/**
 * Represents a generic interface for a priority queue, a special type of data structure where
 * elements are processed based on their priority. A higher priority element is accessed and
 * processed before lower priority elements.
 *
 * @param <T> the type of elements held in this priority queue
 */
public interface PriorityQueue<T> {

    /**
     * Adds the specified element to the heap.
     *
     * @param element the element to be added to the heap
     * @return true if the element was successfully added, false otherwise
     */
    boolean add(T element);

    /**
     * Removes the specified element from the heap if it is present.
     *
     * @param element the element to be removed from the heap
     * @return true if the element was successfully removed, false otherwise
     */
    boolean remove(T element);

    /**
     * Retrieves, but does not remove, the top element of this structure.
     *
     * @return the element at the top of the structure
     * @throws IndexOutOfBoundsException if the structure is empty
     */
    T peek();

    /**
     * Retrieves and removes the top or root element of the heap, or returns null if the heap is
     * empty.
     *
     * @return the top or root element of the heap, or null if the heap is empty
     */
    T poll();

    /**
     * Removes all elements from the data structure. After this operation, the structure will be
     * empty and its size will be zero.
     */
    void clear();

    /**
     * Returns the number of elements currently stored in the structure.
     *
     * @return the total number of elements in the structure
     */
    int size();

    /**
     * Converts the elements of the structure into an array in the same order as they are stored.
     *
     * @return an array containing all the elements of the structure
     */
    Object[] toArray();

}
