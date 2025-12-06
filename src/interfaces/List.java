package interfaces;

import java.util.Comparator;

/**
 * A generic interface representing a List data structure. This interface
 * defines the basic behaviours required for managing a collection of
 * elements.
 *
 * @param <T> the type of elements stored in the list
 */
public interface List<T> {

    /**
     * Returns the number of elements currently stored in the list.
     *
     * @return the number of elements in the list
     */
    int size();

    /**
     * Checks whether the list is empty or not.
     *
     * @return true if the list contains no elements, false otherwise
     */
    boolean isEmpty();

    /**
     * Removes all elements from the list. After invocation, the list will be empty,
     * and its size will be reset to zero.
     */
    void clear();

    /**
     * Inserts the specified element at the specified position in the list.
     * Shifts the element currently at that position (if any) and any later elements
     * to the right (increasing their indices by one).
     *
     * @param idx the index at which the specified element is to be inserted
     * @param t the element to be inserted
     * @return true if the list was modified as a result of the operation, false otherwise
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    boolean add(int idx, T t);

    /**
     * Adds all elements from the specified list to the current list.
     *
     * @param list the list containing elements to be added to this list
     * @return true if the list was modified as a result of this operation, false otherwise
     */
    boolean addAll(List<T> list);

    /**
     * Appends the specified element to the end of the list.
     *
     * @param t the element to be appended to the list
     * @return true if the list was modified as a result of this operation, false otherwise
     */
    boolean append(T t);

    /**
     * Inserts the specified element at the beginning of the list.
     * Shifts the existing elements to the right to make room for the new element.
     *
     * @param t the element to be added at the beginning of the list
     * @return true if the list was modified as a result of this operation, false otherwise
     */
    boolean prepend(T t);

    /**
     * Retrieves the element stored at the specified index in the list.
     *
     * @param idx the index of the element to retrieve
     * @return the element located at the specified index
     * @throws IndexOutOfBoundsException if idx is out of bounds
     */
    T get(int idx);

    /**
     * Retrieves the first element in the list.
     *
     * @return the first element in the list, or null if the list is empty
     */
    T getFirst();

    /**
     * Retrieves the last element in the list.
     *
     * @return the last element in the list, or null if the list is empty
     */
    T getLast();

    /**
     * Replaces the element at the specified position in the list with the specified
     * element and returns the element previously at the specified position.
     *
     * @param idx the index of the element to replace
     * @param t the element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    T set(int idx, T t);

    /**
     * Removes a single occurrence of the specified element from the list, if it is present.
     * If the list contains one or more instances of the specified element, only the first
     * occurrence will be removed.
     *
     * @param o the element to be removed from the list, if present
     * @return true if the list contained the specified element and it was successfully removed,
     *         false otherwise
     */
    boolean remove(Object o);

    /**
     * Removes the element at the specified position in the list.
     * Shifts any later elements to the left (subtracts one from their indices).
     *
     * @param idx the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    T remove(int idx);

    /**
     * Removes and returns the first element in the list.
     * If the list is empty, this method may throw an exception or return null, depending on
     * the specific implementation.
     *
     * @return the first element that was removed from the list or null if the list is empty
     */
    T removeFirst();

    /**
     * Removes and returns the last element in the list. If the list is empty, this method
     * may throw an exception or return null, depending on the specific implementation.
     *
     * @return the last element that was removed from the list or null if the list is empty
     */
    T removeLast();

    /**
     * Sorts the elements in the list according to the order induced by the specified comparator.
     * The behaviour of this method is undefined if the specified comparator is inconsistent
     * with equals.
     *
     * @param c the comparator used to determine the order of the list. A {@code null} value
     * indicates that the elements' natural ordering should be used.
     */
    void sort(Comparator<? super T> c);

    /**
     * Converts the contents of the list into an array.
     *
     * @return an array containing all the elements of the list in a proper sequence
     */
    Object[] toArray();
}
