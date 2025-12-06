package structures;

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
    private final ArrayList<T> data;

    /**
     * Constructs a new CustomStack instance with an initial capacity of 10. The stack is backed by
     * an array of type Element, and the initial size of the stack is set to 0. The capacity of the
     * stack can be dynamically extended as needed, depending on the operations performed.
     */
    public SimpleStack() {
        data = new ArrayList<>();
    }

    @Override
    public void push(T t) {
        data.append(t);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Stack is empty");
        }

        T item = data.get(data.size() - 1);
        data.remove(data.size() - 1);
        return item;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Stack is empty");
        }

        return data.get(data.size() - 1);
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
