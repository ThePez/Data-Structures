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
public class ArrayEntry<K extends Comparable<K>, V extends Comparable<V>> extends Entry<K, V>
        implements Comparable<ArrayEntry<K, V>> {
    /**
     * The index of this node within the array.
     */
    private int index;

    /**
     * Constructs a new Node instance with the specified key and value. The index is initialised to
     * -1.
     *
     * @param key the key associated with this node
     * @param value the value associated with this node
     */
    public ArrayEntry(K key, V value) {
        super(key, value);
        index = -1;
    }

    /**
     * Sets the index of this node.
     *
     * @param idx the new index to be associated with this node
     */
    public void setIndex(int idx) {
        index = idx;
    }

    /**
     * Retrieves the index of this node.
     *
     * @return the index of this node
     */
    public int getIndex() {
        return index;
    }

    /**
     * Compares this ArrayNode to another ArrayNode based on their keys.
     * The comparison is performed using the natural ordering of the keys,
     * as defined by the {@code compareTo} method of the key's type.
     *
     * @param other the ArrayNode to be compared with this ArrayNode
     * @return a negative integer, zero, or a positive integer as this ArrayNode's key
     *         is less than, equal to, or greater than the specified ArrayNode's key
     */
    @Override
    public int compareTo(ArrayEntry<K, V> other) {
        return key.compareTo(other.getKey());
    }
}