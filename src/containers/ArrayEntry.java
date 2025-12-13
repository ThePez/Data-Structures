package containers;

/**
 * Represents a node that contains a key-value pair with the ability to compare nodes
 * based on their values or keys. This class is generic and supports types where both
 * the key and value implement the Comparable interface. ArrayNodes are comparable
 * based on their keys.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class ArrayEntry<K extends Comparable<K>, V> extends Entry<K, V>
        implements Comparable<ArrayEntry<K, V>> {

    /**
     * Index of the entry in the underlying array.
     */
    private int index = -1;

    /**
     * Constructs a new ArrayEntry instance with the specified key and value.
     *
     * @param key The key associated with the entry. It must implement the Comparable interface.
     * @param value The value associated with the entry.
     */
    public ArrayEntry(K key, V value) {
        super(key, value);
    }

    /**
     * Retrieves the index of this entry in the underlying array.
     *
     * @return The index of this entry, represented as an integer.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Updates the index of this entry in the underlying array.
     *
     * @param index The new index to be set for this entry. Must be a non-negative integer.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(ArrayEntry<K, V> other) {
        return key.compareTo(other.getKey());
    }
}