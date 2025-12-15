package interfaces;

/**
 * A generic interface that represents the basic operations of a stack data structure.
 * A stack is a collection of elements that follows the Last-In-First-Out (LIFO) principle.
 *
 * @param <T> the type of elements held in the stack
 */
public interface Stack<T> {
    /**
     * Adds an element to the top of the stack.
     *
     * @param t the element to be added to the stack
     */
    boolean push(T t);

    /**
     * Removes and returns the top element of the stack.
     * If the stack is empty, throws an IndexOutOfBoundsException.
     *
     * @return the element that was removed from the top of the stack
     */
    T pop();

    /**
     * Retrieves, but does not remove, the top element of the stack.
     * If the stack is empty, throws an IndexOutOfBoundsException.
     *
     * @return the top element of the stack
     */
    T peek();

    /**
     * Checks whether the stack is empty.
     *
     * @return true if the stack contains no elements; false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the number of elements currently stored in the stack.
     *
     * @return the number of elements in the stack
     */
    int size();

    /**
     * Removes all elements from the stack, effectively resetting it to an empty state.
     * After calling this method, the stack will contain no elements and its size will be 0.
     */
    void clear();
}
