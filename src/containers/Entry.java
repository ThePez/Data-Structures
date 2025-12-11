package containers;

import interfaces.KVEntryInterface;
import java.util.Objects;

/**
 * Represents a key-value pair that can be used in various data structures or
 * collections requiring such an association. The key serves as the unique identifier
 * while the value holds the associated data.
 *
 * @param <K> The type of the key.
 * @param <V> The type of the value.
 */
public class Entry<K, V> implements KVEntryInterface<K, V> {

    /**
     * The key associated with an entry in a key-value pair structure.
     * Serves as the unique identifier within the entry.
     */
    protected K key;

    /**
     * The value associated with this entry.
     * Represents the data stored alongside the key in the entry.
     */
    protected V value;

    /**
     * Constructs a new entry that associates the specified key with the specified value.
     *
     * @param key The key associated with the entry.
     * @param value The value associated with the key in the entry.
     */
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Constructs a new entry by copying the key and value from an existing entry.
     *
     * @param entry The entry whose key and value will be copied to the new entry.
     *              This entry must not be null and should have valid key and value pairs.
     */
    public Entry(Entry<K, V> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    /**
     * Retrieves the key associated with this entry.
     *
     * @return The key associated with this entry.
     */
    public K getKey() {
        return this.key;
    }

    /**
     * Sets the key associated with this entry.
     *
     * @param key The new key to be set for this entry.
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Retrieves the value associated with this entry.
     *
     * @return The value associated with this entry.
     */
    public V getValue() {
        return this.value;
    }

    /**
     * Sets the value associated with this entry.
     *
     * @param value The new value to be set for this entry.
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Compares this entry with the specified object for equality. Two entries are considered
     * equal if their keys and values are both equal.
     *
     * @param obj The object to be compared with this entry for equality.
     * @return true if the specified object is equal to this entry, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Entry<?, ?> other = (Entry<?, ?>) obj;
        return Objects.equals(this.key, other.key) && Objects.equals(this.value, other.value);
    }

    /**
     * Computes the hash code for this entry based on its key.
     *
     * @return The hash code of the key associated with this entry.
     */
    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

}
