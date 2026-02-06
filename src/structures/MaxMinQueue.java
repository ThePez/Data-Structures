package structures;

import interfaces.Queue;

/**
 * A specialised queue data structure that extends {@code SimpleQueue} and implements {@code Queue}.
 * The {@code MaxMinQueue} is capable of efficiently accessing either the maximum or minimum
 * element present in the queue, depending on the configuration set during instantiation.
 *
 * @param <T> The type of elements stored in this queue. The type must implement {@link Comparable}.
 */
public class MaxMinQueue<T extends Comparable<T>> extends SimpleQueue<T> implements Queue<T> {

    /**
     * A {@link LinkedList} used to maintain a subset of the queue in monotonic order.
     * The list is updated during enqueue and dequeue operations to allow constant-time
     * access to the maximum or minimum element, depending on the configuration of the queue.
     */
    private final LinkedList<T> monotonicList;

    /**
     * Indicates whether the queue is configured to operate as a max-queue or a min-queue ({@code true})
     */
    private final boolean max;

    /**
     * Constructs a {@code MaxMinQueue} instance that can be configured to operate as a max-queue
     * or a min-queue based on the input parameter. The configuration determines whether the
     * queue provides constant-time access to the maximum or minimum element in the queue, respectively.
     *
     * @param max {@code true} if the queue should operate as a max-queue (providing maximum element
     *            access). {@code false} if it should operate as a min-queue (providing minimum
     *            element access).
     */
    public MaxMinQueue(boolean max) {
        super();
        this.max = max;
        monotonicList = new LinkedList<>();
    }

    @Override
    public void enqueue(T item) {
        if (isEmpty()) {
            super.enqueue(item);
            monotonicList.append(item);
            return;
        }

        super.enqueue(item);
        while (!monotonicList.isEmpty()) {
            T last = monotonicList.getLast();
            if (max) {
                if (item.compareTo(last) < 0) {
                    break;
                }
            } else {
                if (item.compareTo(last) > 0) {
                    break;
                }
            }

            monotonicList.removeLast();
        }

        monotonicList.append(item);
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        if (monotonicList.getFirst().equals(super.peek())) {
            monotonicList.removeFirst();
        }

        return super.dequeue();
    }

    /**
     * Retrieves the top element of the monotonic list, which represents the maximum or minimum
     * element in the queue based on the queue's configuration.
     *
     * @return The top element of the monotonic list, or {@code null} if the queue is empty.
     */
    public T peekTop() {
        return monotonicList.getFirst();
    }
}
