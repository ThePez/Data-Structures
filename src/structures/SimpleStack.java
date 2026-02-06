package structures;

import interfaces.List;
import interfaces.Stack;


/**
 * A generic implementation of a stack data structure that extends the functionality
 * to efficiently retrieve the minimum element in the stack. The stack is backed by a
 * dynamic array to store elements.
 *
 * @param <T> the type of elements stored in the stack, which must be comparable
 */
public class SimpleStack<T> implements Stack<T> {

    /**
     * A dynamic array that serves as the underlying storage for the stack elements.
     * This array stores 'Element' instances, each of which encapsulates the data and metadata
     * necessary for stack operations such as retrieving the minimum element efficiently.
     */
    protected final List<T> data;

    /**
     * Constructs a new CustomStack instance with an initial capacity of 10. The stack is backed by
     * an array of type Element, and the initial size of the stack is set to 0. The capacity of the
     * stack can be dynamically extended as needed, depending on the operations performed.
     */
    public SimpleStack() {
        data = new ArrayList<>();
    }

    @Override
    public boolean push(T t) {
        return data.append(t);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            return null;
        }

        return data.removeLast();
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            return null;
        }

        return data.getLast();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void clear() {
        data.clear();
    }
}
