package structures;

import containers.Entry;
import interfaces.List;
import interfaces.Map;

import java.util.function.Function;

/**
 * The UnorderedMap class is a hash-based data structure that provides functionality
 * to store and retrieve key-value pairs with no guaranteed order of keys.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    /**
     * List of prime numbers used to determine the size of the internal table.
     */
    private final int[] primes = {11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853, 25717,
                                  51437, 102877, 205759, 411527, 823117, 1646237, 3292489, 6584983,
                                  13169977, 26339969, 52679969, 105359939, 210719881, 421439783,
                                  842879579, 1685759167, 2147483647};

    /**
     * Index of the current prime number in the array of prime numbers.
     */
    private int primeIndex = 0;

    /**
     * Number of key-value pairs stored in the map
     */
    private int logicalSize;

    /**
     * The maximum capacity of the map.
     */
    private int capacity;

    /**
     * The internal table used to store the Key-Value pairs.
     */
    private Entry<K, V>[] table;

    /**
     * Constructs a new, empty UnorderedMap with default initial capacity.
     * The default capacity is determined by the first element in the predefined
     * array of prime numbers. The hash table is initialised to this capacity
     * to optimise performance for unordered key-value storage and operations.
     */
    @SuppressWarnings("unchecked")
    public HashMap() {
        size = 0;
        logicalSize = 0;
        capacity = primes[primeIndex];
        table = new Entry[capacity];
    }

    /**
     * Constructs an UnorderedMap with an initial capacity. The capacity
     * provided is adjusted to the nearest prime number greater than or
     * equal to the specified capacity to optimise hash table performance.
     *
     * @param capacity the initial capacity of the map. If the specified
     *                 capacity is lower than the smallest prime or higher
     *                 than the largest prime supported, the capacity is
     *                 defaulted to the nearest supported value.
     */
    @SuppressWarnings("unchecked")
    public HashMap(int capacity) {
        int low = 0;
        int high = primes.length - 1;
        primeIndex = -1;
        // Binary search to find the smallest prime number greater than or equal to the capacity
        while (low <= high) {
            int mid = (low + high) / 2;
            if (primes[mid] >= capacity) {
                primeIndex = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (primeIndex == -1) {
            System.out.printf("Requested capacity of %d is not supported."
                    + " Defaulted to capacity of %d\n", capacity, primes[primes.length - 1]);
            primeIndex = primes.length - 1;
        }

        // Set the capacity to the smallest prime number found
        this.capacity = primes[primeIndex];
        size = 0;
        logicalSize = 0;
        table = new Entry[this.capacity];
    }

    @Override
    public int size() {
        return logicalSize;
    }

    @Override
    public boolean isEmpty() {
        return logicalSize == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        size = 0;
        logicalSize = 0;
        table = new Entry[capacity];
    }

    @Override
    public V put(K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        int hash = hash(key);
        // Invalid keys are negative
        if (hash < 0) {
            return null;
        }

        V oldValue = null;
        int candidate = -1; // Hash of the first spot available to place item
        int offset = 0; // Distance from the initial hash value
        boolean added = false;
        for (int i = 0; i < table.length; i++) {
            Entry<K, V> entry = table[hash];
            if (entry == null) {
                // Empty slot found -> Stop looping the table now
                if (candidate != -1 && offset < i) {
                    // Was a sentinel slot found before the Empty slot
                    entry = table[candidate];
                    entry.setKey(key);
                    entry.setValue(value);
                    added = true;
                    // Only logical increase as an Entry object was reused
                    logicalSize++;
                    break;
                }

                // Add the new value
                table[hash] = new Entry<>(key, value);
                // Both sizes increase as a new object was made
                size++;
                logicalSize++;
                added = true;
                break;
            } else if (entry.getKey() == null) {
                // Sentinel -> possible spot to store a new node
                if (candidate == -1) {
                    candidate = hash;
                    offset = i;
                }
            } else if (entry.getKey().equals(key)) {
                // Match found -> replace
                oldValue = entry.getValue();
                entry.setValue(value);
                added = true;
                break;
            }

            hash = (hash + 1) % capacity;
        }

        // Table may not have any "null" slots left -> a sentinel should've been found
        if (!added) {
            // This should never happen... Will only trigger when size == Integer.MAX_VALUE
            if (candidate == -1) {
                throw new IllegalStateException("Hash Table is full");
            }

            // Add the new entry at the first available slot found
            Entry<K, V> entry = table[candidate];
            entry.setKey(key);
            entry.setValue(value);
            // Object was reused
            logicalSize++;
        }

        // Check if the table is at loadFactor and resize if needed
        double loadFactor = 0.66;
        if ((double) size / capacity >= loadFactor) {
            // Resize won't do anything after the 29th resize from the default size
            resize();
        }

        // As no old value in the map was overridden, return null
        return oldValue;
    }


    @Override
    public V get(K key) {
        int hash = hash(key);
        // Invalid keys are negative
        if (hash < 0) {
            return null;
        }

        for (int i = 0; i < table.length; i++) {
            Entry<K, V> entry = table[hash];
            if (entry == null) {
                return null;
            }

            if (entry.getKey() != null && entry.getKey().equals(key)) {
                return entry.getValue();
            }

            // Try the next entry
            hash = (hash + 1) % capacity;
        }

        // Key was not found -> return null
        return null;
    }

    @Override
    public V remove(K key) {
        int hash = hash(key);

        // Invalid keys are negative
        if (hash < 0) {
            return null;
        }

        for (int i = 0; i < table.length; i++) {
            Entry<K, V> entry = table[hash];

            // null entry means nothing has ever been here
            if (entry == null) {
                return null;
            }

            // Found the key, so remove it
            if (entry.getKey() != null && entry.getKey().equals(key)) {
                // Entry object remains, a key-value pair is removed
                logicalSize--;
                V oldValue = entry.getValue();
                entry.setKey(null);
                entry.setValue(null);
                return oldValue;
            }

            // Try the next entry
            hash = (hash + 1) % capacity;
        }

        // Key was not found -> return null
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        if (isEmpty()) {
            return false;
        }

        V value = get(key);
        return value != null;
    }

    @Override
    public List<K> getKeys() {
        return collectEntries(Entry::getKey, new ArrayList<>());
    }

    @Override
    public List<V> getValues() {
        return collectEntries(Entry::getValue, new ArrayList<>());
    }

    @Override
    public List<Entry<K, V>> getEntries() {
        return collectEntries(entry -> entry, new ArrayList<>());
    }

    /**
     * Collects and transforms entries of the map into a specified collection using the provided
     * function. This method iterates through all non-null entries in the underlying table and
     * applies the given transformation function to each entry, adding the resulting object to the
     * provided collection.
     *
     * @param <T>         the type of elements to be collected after transformation
     * @param function    the function to apply to each entry in the map
     * @param collection  the collection where the transformed entries will be added
     * @return the collection containing the transformed entries
     */
    private <T> List<T> collectEntries(Function<Entry<K, V>, T> function, List<T> collection) {
        if (isEmpty()) {
            return collection;
        }

        for (Entry<K, V> entry : table) {
            if (entry != null && entry.getKey() != null) {
                collection.append(function.apply(entry));
            }

            // All entries have been processed
            if (collection.size() == size) {
                break;
            }
        }

        return collection;
    }

    /**
     * Computes the hash code for a given key to determine its position in the hash map.
     * Only keys of type Long are supported. If the key is null or not of type Long,
     * the method returns -1. The hash code calculation ensures a better distribution
     * of keys using bitwise operations.
     *
     * @param key the key whose hash code needs to be computed
     * @return the computed hash code as an integer within map's capacity,
     *         or -1 if the key is null or not of type Long
     */
    private int hash(K key) {
        // Handle null keys
        if (key == null) {
            return -1;
        }

        // Use Java's HashCode contract to find the initial hash code
        int hash = key.hashCode();

        // Simple bit rotation and 'XOR'.
        // drew some inspiration from MurmurHash3 -> add in a multiplier of a large prime
        hash *= 6321751; // Large prime number
        hash = (hash << 27) | (hash >>> 5);
        hash ^= hash >>> 16;
        hash ^= (hash << 17) | (hash >>> 11);

        // Normalise the hash value to ensure it fits within the map's capacity
        return Math.floorMod(hash, capacity);
    }

    /**
     * Resizes the internal hash table to accommodate more entries by incrementing the capacity
     * to the next prime number in the predefined array of primes. The method redistributes
     * all existing entries to the new table based on their hash codes and the updated capacity.
     * If the table has already reached its maximum capacity, a message is printed, and no resizing
     * occurs.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        if (primeIndex == primes.length - 1) {
            System.out.printf("Unable to expand HashTable capacity."
                    + " Max capacity of %d is reached.\n", Integer.MAX_VALUE);
            return;
        }

        // Reset the size to 0 as .put() will fix it later
        size = 0;
        logicalSize = 0;
        // Get the next prime number in the array
        capacity = primes[++primeIndex];
        // Store all existing entries in the table
        Entry<K, V>[] oldTable = table;
        // Make a new bigger table
        table = new Entry[capacity];
        // Iterate through the old table and re-distribute the entries
        for (Entry<K, V> entry : oldTable) {
            // Only keep actual Key-Value pairs, don't transfer the Sentinels
            if (entry != null && entry.getKey() != null) {
                K key = entry.getKey();
                V value = entry.getValue();
                put(key, value);
            }
        }
    }
}
