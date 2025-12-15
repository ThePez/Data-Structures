package structures;

import containers.Entry;
import interfaces.List;
import interfaces.Stack;

/**
 * A specialized stack implementation that extends the functionality of a simple stack
 * by providing efficient retrieval of either the maximum or minimum value in the stack,
 * depending on its configuration. Each element pushed to the stack is paired with an
 * auxiliary value that tracks the current max or min value.
 *
 * @param <T> the type of elements held in the stack, which must be comparable
 */
public class MaxMinStack<T extends Comparable<T>> extends SimpleStack<T> implements Stack<T> {

    /**
     * A list that stores pairs of elements where each pair consists of a value from the stack
     * and an auxiliary value (e.g., the current maximum or minimum value in the stack at the
     * time of insertion).
     */
    private final List<Entry<T, T>> data;

    /**
     * A configuration flag indicating whether the stack operates in "maximum" mode.
     */
    private final boolean max;

    /**
     * Constructs a new MaxMinStack with the specified configuration mode (max or min).
     * The stack will track either the maximum or minimum element based on the provided mode.
     *
     * @param max a boolean flag indicating the mode of the stack:
     *            true for "maximum tracking" mode,
     *            false for "minimum tracking" mode
     */
    public MaxMinStack(boolean max) {
        data = new ArrayList<>();
        this.max = max;
    }

    @Override
    public boolean push(T t) {
        if (isEmpty()) {
            return data.append(new Entry<>(t, t));
        }

        T value = data.getLast().getValue();
        if (max) {
            if (t.compareTo(value) > 0) {
                return data.append(new Entry<>(t, t));
            } else {
                return data.append(new Entry<>(t, value));
            }
        } else {
            if (t.compareTo(value) < 0) {
                return data.append(new Entry<>(t, t));
            } else {
                return data.append(new Entry<>(t, value));
            }
        }
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            return null;
        }

        return data.removeLast().getKey();
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            return null;
        }

        return data.getLast().getKey();
    }

    /**
     * Retrieves the top auxiliary value from the stack without removing it.
     * The auxiliary value is determined based on the configuration of the stack
     * (e.g., maximum or minimum, depending on the mode it was initialised with).
     * Returns null if the stack is empty.
     *
     * @return The top auxiliary value of the stack, or null if the stack is empty.
     */
    public T peekTop() {
        if (isEmpty()) {
            return null;
        }

        return data.getLast().getValue();
    }
}
