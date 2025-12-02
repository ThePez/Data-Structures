package interfaces;

/**
 * Represents a generic interface for a node that stores a key-value pair.
 * The keys and values are required to implement the Comparable interface to
 * facilitate comparisons.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public interface KVEntryInterface<K, V> {

    /**
     * Retrieves the key associated with this node.
     *
     * @return the key associated with this node
     */
    K getKey();

    /**
     * Retrieves the value associated with this node.
     *
     * @return the value associated with this node
     */
    V getValue();

    /**
     * Sets the value associated with this node.
     *
     * @param value the new value to be associated with this node
     */
    void setValue(V value);

    /**
     * Sets the key associated with this node.
     *
     * @param key the new key to be associated with this node
     */
    void setKey(K key);
}
