package structures;

import interfaces.List;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * Dynamic Array Class. Able to store any number of T objects.
 */
public class ArrayList<T> extends AbstractList<T> implements List<T> {
    /**
     * Capacity tracks the total number of slots (used or unused) in the data array
     */
    private int capacity;

    /**
     * Data stores the raw objects
     */
    private T[] data;

    /**
     * Constructs an empty Dynamic Array
     */
    @SuppressWarnings("unchecked")
    public ArrayList() {
        capacity = 10;
        data = (T[]) new Object[capacity];
        size = 0;
    }

    /**
     * Retrieves the current capacity of the array.
     *
     * @return the number of elements the array can currently hold without resizing.
     */
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean append(T element) {
        if (size >= capacity) {
            boolean outOfMemory = extendCapacity(0);
            if (outOfMemory) {
                return false;
            }
        }

        data[size++] = element;
        return true;
    }

    @Override
    public boolean prepend(T element) {
        if (size >= capacity && extendCapacity(1)) {
            return false;
        } else {
            System.arraycopy(data, 0, data, 1, size);
        }

        data[0] = element;
        size++;
        return true;
    }

    @Override
    public boolean add(int ix, T element) {
        // Special cases
        if (ix == 0) {
            return prepend(element);
        } else if (ix == size) {
            return append(element);
        } else if (checkIndex(ix)) {
            throw new IndexOutOfBoundsException();
        }

        // Ensure the static array is big enough
        if (size >= capacity && extendCapacity(0)) {
            return false;
        }

        System.arraycopy(data, ix, data, ix + 1, size - ix);
        // Insert the new element into the array
        data[ix] = element;
        size++;
        return true;
    }

    @Override
    public T get(int ix) {
        if (checkIndex(ix)) {
            throw new IndexOutOfBoundsException();
        }

        return data[ix];
    }

    @Override
    public T getFirst() {
        if (isEmpty()) {
            return null;
        }

        return data[0];
    }

    @Override
    public T getLast() {
        if (isEmpty()) {
            return null;
        }
         return data[size - 1];
    }

    @Override
    public T set(int ix, T element) {
        if (checkIndex(ix)) {
            throw new IndexOutOfBoundsException();
        }

        T old = data[ix];
        data[ix] = element;
        return old;
    }

    @Override
    public T remove(int ix) {
        if (checkIndex(ix)) {
            throw new IndexOutOfBoundsException();
        }

        T item =  data[ix];
        // Shuffle elements up
        System.arraycopy(data, ix + 1, data, ix, size - ix - 1);
        data[--size] = null; // Remove final element duplicate
        return item;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        return remove(0);
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        T last = data[size - 1];
        data[--size] = null;
        return last;
    }

    @Override
    public boolean remove(Object t) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], t)) {
                System.arraycopy(data, i + 1, data, i, size - i - 1);
                data[--size] = null;
                return true;
            }
        }

        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        size = 0;
        data = (T[]) new Object[capacity];
    }

    @Override
    public void sort(Comparator<? super T> c) {
        mergeSort(data, 0, size - 1, c);
    }

    /**
     * Recursively sorts the specified portion of the array using the merge sort algorithm.
     * Divides the array into smaller subarrays, sorts these subarrays, and merges them
     * back in sorted order.
     *
     * @param array The array to be sorted. This array will be modified in place.
     * @param left The starting index of the portion of the array to sort.
     * @param right The ending index of the portion of the array to sort.
     * @param c A comparator to compare elements within the array. If {@code c} is null,
     *          natural ordering will be used.
     */
    private void mergeSort(T[] array, int left, int right, Comparator<? super T> c) {
        // Find diff between left and right, then add this to left - helps prevent overflow
        int middle = left + (right - left) / 2;

        if (left < right) {

            mergeSort(array, left, middle, c);
            mergeSort(array, middle + 1, right, c);
            arrayMerge(array, left, middle, right, c);
        }
    }

    /**
     * Merges two sorted subarrays of the given array into a single sorted subarray.
     * The method assumes that the subarray from index {@code left} to {@code middle}
     * and the subarray from {@code middle + 1} to {@code right} are already sorted.
     * This method modifies the original array to contain the merged and sorted result.
     *
     * @param array The original array containing the subarrays to be merged.
     * @param left The starting index of the first sorted subarray.
     * @param middle The ending index of the first sorted subarray.
     * @param right The ending index of the second sorted subarray.
     * @param c A comparator that defines the ordering of the elements.
     *          If {@code c} is null, natural ordering is used.
     */
    private void arrayMerge(T[] array, int left, int middle, int right, Comparator<? super T> c) {
        // Find the size of the two arrays to merge
        int size1 = middle - left + 1;
        int size2 = right - middle;
        T[] temp1 = Arrays.copyOfRange(array, left, middle + 1);
        T[] temp2 = Arrays.copyOfRange(array, middle + 1, right + 1);

        int i = 0;
        int j = 0;
        int k = left;

        // 2 finger sorting
        while (i < size1 && j < size2) {
            T a = temp1[i];
            T b = temp2[j];
            if (compareNullSafe(a, b, c) <= 0) {
                array[k++] = temp1[i++];
            } else {
                array[k++] = temp2[j++];
            }
        }

        // Array 2 was shorter than array 1 -> finish copying array 1
        while (i < size1) {
            array[k++] = temp1[i++];
        }

        // Array 1 was shorter than array 2 -> finish copying array 2
        while (j < size2) {
            array[k++] = temp2[j++];
        }
    }

    /**
     * Doubles the current capacity of the array and adjusts the internal storage.
     * The method optionally shifts the existing elements by an offset defined
     * by the parameter {@code pos}.
     *
     * @param pos The position offset to start copying the existing elements.
     *            Must be either 0 or 1. Throws an exception if the value is invalid.
     * @return {@code true} if the capacity cannot be extended due to an {@code OutOfMemoryError},
     *         otherwise {@code false}.
     * @throws IndexOutOfBoundsException if {@code pos} is not 0 or 1.
     */
    @SuppressWarnings("unchecked")
    private boolean extendCapacity(int pos) {
        int oldCapacity = capacity;
        if (pos != 0 && pos != 1) {
            throw new IndexOutOfBoundsException("Copy offset must be either 0 or 1.");
        }

        try {
            capacity *= 2;
            T[] temp = data;
            data = (T[]) new Object[capacity];
            System.arraycopy(temp, 0, data, pos, size);
            return false;
        } catch (OutOfMemoryError e) {
            capacity =  oldCapacity;
            return true;
        }
    }
}
