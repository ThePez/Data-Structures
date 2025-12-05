package structures;

import interfaces.List;
import java.util.Comparator;

/**
 * An abstract base class that provides a skeletal implementation of the {@code List} interface.
 * This class simplifies the creation of custom list implementations by handling common operations
 * such as size management and conversion to an array. Concrete subclasses are required to implement
 * specific methods for adding, removing, and accessing elements.
 *
 * @param <T> The type of elements stored in the list.
 */
public abstract class AbstractList<T> implements List<T> {

    /**
     * Represents the number of elements currently stored in the list.
     * The value is updated as elements are added or removed.
     */
    protected int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = get(i);
        }

        return arr;
    }

    /**
     * Checks whether the given index is out of bounds for the current size
     * of the linked list.
     *
     * @param idx The index to check.
     * @return {@code true} if the index is invalid (less than 0 or greater than
     *         or equal to the size of the list), otherwise {@code false}.
     */
    protected boolean checkIndex(int idx) {
        return idx < 0 || idx >= size;
    }

    /**
     * Compares two objects, handling potential null values safely. If both objects
     * are null, they are considered equal. If one of the objects is null, it is
     * considered smaller than the non-null object. The method uses the provided
     * comparator to determine the order of non-null objects. If no comparator is
     * provided, the natural ordering is used.
     *
     * @param a the first object to compare may be null
     * @param b the second object to compare may be null
     * @param comparator the comparator to define the ordering of the objects,
     *          if null, natural ordering will be applied
     * @return an integer representing the comparison result:
     *         0 if the objects are equal, a negative value if the first object
     *         is less than the second, and a positive value if the first object
     *         is greater than the second
     */
    @SuppressWarnings("unchecked")
    protected int compareNullSafe(T a, T b, Comparator<? super T> comparator) {
        // Handle any null comparisons
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return -1;
        } else if (b == null) {
            return 1;
        }

        // Both "a" and "b" are non-null, use the comparator if provided
        if (comparator == null) {
            return ((Comparable<? super T>) a).compareTo(b);
        }

        return comparator.compare(a, b);
    }
}
