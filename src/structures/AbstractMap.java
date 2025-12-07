package structures;

import interfaces.Map;

/**
 * An abstract implementation of the Map interface that provides a skeletal structure
 * for a map collection. This class is intended to serve as a base for implementing
 * custom map structures.

 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public abstract class AbstractMap<K, V> implements Map<K, V> {

    /**
     * The number of elements stored in the map.
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
}
