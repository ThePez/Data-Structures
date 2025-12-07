package structures;

/**
 * The Median class provides a mechanism to maintain a dynamic collection of elements and efficiently
 * compute the median of the collection at any time. The class uses two heaps (a max-heap and a min-heap)
 * to manage the elements, ensuring that the median can be retrieved in constant time and updates (additions)
 * can be performed in logarithmic time.
 *
 * @param <T> the type of elements to be stored in the tracker, which must extend Number and
 *            implement Comparable to ensure numeric comparison functionality.
 */
public class Median<T extends Number & Comparable<T>> {

    /**
     * The lower heap is used to maintain the balance of the two heaps used to track the median.
     */
    private final PQHeap<T> lower;

    /**
     * The upper heap is used to maintain the balance of the two heaps used to track the median.
     */
    private final PQHeap<T> upper;

    /**
     * Constructs an empty MedianTracker.
     */
    public Median() {
        lower = new PQHeap<>(true); // Max heap
        upper = new PQHeap<>(); // Min heap
    }

    /**
     * Returns the total number of elements in the MedianTracker by summing
     * the sizes of the internal data structures used to track elements.
     *
     * @return the total number of elements tracked
     */
    public int size() {
        return lower.size() + upper.size();
    }

    /**
     * Returns true if the tracker is empty, false otherwise.
     *
     * @return true if the tracker is empty, false otherwise
     */
    public boolean isEmpty() {
        return lower.isEmpty() && upper.isEmpty();
    }

    /**
     * Calculates the median of the elements currently stored in the tracker. If the number of
     * elements is odd, returns the top element of the max-heap (lower). If the number of elements
     * is even, returns the average of the two peeks from both heaps.
     *
     * @return the median of the elements as a double
     * @throws IndexOutOfBoundsException if there are no elements in the tracker
     */
    public double getMedian() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("No elements in the tracker");
        }

        int size = lower.size() + upper.size();
        if (size % 2 == 0) {
            // Even
            T lowPeak = lower.peek();
            T highPeak = upper.peek();
            return (lowPeak.doubleValue() + highPeak.doubleValue()) / 2;
        } else {
            return lower.peek().doubleValue();
        }
    }

    /**
     * Adds a value to the MedianTracker and adjusts the internal data structure to maintain
     * the properties necessary for calculating the median.
     *
     * @param value the integer value to be added to the tracker
     * @return true if the value was successfully added
     */
    public boolean add(T value) {
        if (isEmpty()) {
            lower.add(value);
            return true;
        }

        T lowPeak = lower.peek();
        if (value.compareTo(lowPeak) < 0) {
            lower.add(value);
        } else {
            upper.add(value);
        }

        rebalance();
        return true;
    }

    /**
     * Maintains the balance of the two heaps used to track the median. If the size difference
     * between the two heaps exceeds 1, this method ensures the size difference remains valid by
     * moving the peak element from the larger heap to the smaller one as needed.
     */
    private void rebalance() {
        if (lower.size() > upper.size() + 1) {
            upper.add(lower.poll());
            return;
        }

        if (upper.size() > lower.size()) {
            lower.add(upper.poll());
        }
    }
}

