package structures;

import interfaces.Queue;

/**
 * A generic queue implementation using a linked list as the underlying data structure.
 * This queue supports typical queue operations such as enqueue, dequeue, clear and size.
 *
 * @param <E> The type of elements this queue holds.
 */
public class SimpleQueue<E> implements Queue<E> {

    /**
     * Represents the underlying data structure for a custom queue implementation.
     */
    private final LinkedList<E> queue;

    /**
     * Constructs an empty CustomQueue. Initialises the underlying data structure as a linked list.
     */
    public SimpleQueue() {
        queue = new LinkedList<>();
    }

    /**
     * Adds an element to the end of the queue.
     *
     * @param item The element to be added to the queue.
     */
    public void enqueue(E item) {
        queue.append(item);
    }

    /**
     * Removes and returns the element at the front of the queue.
     * If the queue is empty, it returns null.
     *
     * @return The element at the front of the queue, or null if the queue is empty.
     */
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }

        return queue.removeFirst();
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }

        return queue.get(0);
    }

    /**
     * Removes all elements from the queue, leaving it empty.
     */
    public void clear() {
        queue.clear();
    }

    /**
     * Returns the number of elements currently present in the queue.
     *
     * @return the size of the queue, representing the number of elements stored.
     */
    public int size() {
        return queue.size();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no elements, false otherwise.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
