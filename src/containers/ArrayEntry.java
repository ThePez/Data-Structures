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
     * Constructs an ArrayEntry object that stores a key-value pair.
     *
     * @param key The key to be associated with the entry. Must be of type K.
     * @param value The value to be stored with the key in the entry. Must be of type V.
     */
    public ArrayEntry(K key, V value) {
        super(key, value);
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