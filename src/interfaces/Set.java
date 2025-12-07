package interfaces;

/**
 * A generic interface representing a collection of key-value mappings, resembling a map structure.
 * This interface allows storage, retrieval, and removal of key-value pairs and provides basic
 * utility methods to manipulate and inspect the collection.
 */
public interface Set<K> {

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
     * Checks if the set contains the specified key.
     *
     * @param key the key whose presence in the map is to be tested
     * @return true if the map contains a mapping for the key, false otherwise
     */
    boolean get(K key);

    /**
     * Adds the specified key to the collection.
     *
     * @param key the key to be added to the collection
     * @return true if the key was successfully added, false if the key already exists
     */
    boolean put(K key);

    /**
     * Removes the specified key from the collection if it is present.
     *
     * @param key the key to be removed from the collection
     * @return true if the key was successfully removed from the collection, false otherwise
     */
    boolean remove(K key);

}
