package interfaces;

import containers.Entry;

/**
 * Represents a generic map interface that allows key-value pair storage, retrieval, and
 * manipulation. This interface provides basic methods for common operations on a map, such as
 * adding, removing, retrieving elements, and checking the presence of keys or values.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface Map<K, V> {

    /**
     * Returns the number of elements currently stored in the collection.
     *
     * @return the total count of elements in the collection; returns 0 if the collection is empty.
     */
    int size();

    /**
     * Returns true if the collection is empty, false otherwise.
     *
     * @return true if the collection is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Removes all entries from the collection. After the invocation of this method,
     * the collection will be empty and its size will be reset to zero.
     */
    void clear();

    /**
     * Checks if the map contains a mapping for the specified key.
     *
     * @param key the key whose presence in the map is to be tested
     * @return true if the map contains a mapping for the key, false otherwise
     */
    boolean containsKey(K key);

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the specified key, or null if the map contains no mapping
     *         for the key
     */
    V get(K key);

    /**
     * Associates the specified value with the specified key in the map.
     * If the map previously contained a mapping for the key, the old value is replaced.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with the key, or null if there was no mapping for the key
     */
    V put(K key, V value);

    /**
     * Removes the mapping for a specific key from the map if it is present.
     *
     * @param key the key whose associated mapping is to be removed
     * @return the value associated with the key that was removed, or null if the map contained no
     *         mapping for the key
     */
    V remove(K key);

    /**
     * Retrieves a list of all the keys currently stored in the binary tree.
     *
     * @return a list containing all keys in the binary tree, ordered according to the
     *         specific traversal or insertion logic of the implementation
     */
    List<K> getKeys();

    /**
     * Retrieves a list of all the values currently stored in the binary tree.
     *
     * @return a list containing all values in the binary tree, ordered according to the
     *         specific traversal or insertion logic of the implementation
     */
    List<V> getValues();

    /**
     * Retrieves a list of all entries (key-value pairs) currently stored in the binary tree.
     *
     * @return a list of entries, where each entry contains a key and its associated value,
     *         ordered according to the specific traversal or insertion logic of the implementation
     */
    List<Entry<K, V>> getEntries();
}
