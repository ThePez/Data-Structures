package interfaces;

/**
 * Represents a generic queue interface that defines the basic operations
 * required for working with a queue data structure. A queue operates on
 * a FIFO (First-In-First-Out) principle.
 *
 * @param <E> the type of elements held in this queue
 */
public interface Queue<E> {
    /**
     * Adds the specified element to the end of the queue.
     *
     * @param element the element to be added to the queue; must not be null
     */
    void enqueue(E element);

    /**
     * Removes and returns the element at the front of the queue.
     * If the queue is empty, behaviour may vary depending on the implementation.
     *
     * @return the element removed from the front of the queue
     */
    E dequeue();

    /**
     * Retrieves, but does not remove, the element at the front of the queue.
     * If the queue is empty, the behaviour may vary depending on the implementation.
     *
     * @return the element at the front of the queue
     */
    E peek();

    /**
     * Checks if the collection is empty.
     *
     * @return true if the collection contains no elements, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the number of elements currently stored in the collection.
     *
     * @return the total count of elements in the collection; returns 0 if the collection is empty.
     */
    int size();

}
