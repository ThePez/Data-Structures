package structures;

import interfaces.Set;

/**
 * The BitVector class provides a dynamic structure for efficiently
 * managing a vector of bits. It can store a specified number of bits,
 * referred to as the "size" of the BitVector, and provides various
 * methods for bit manipulation such as getting, setting, unsetting,
 * complementing, shifting, and rotating bits.
 */
public class BitVector implements Set<Integer> {

    /**
     * The number of bits available for use in the BitVector's internal storage.
     */
    private int size;

    /**
     * The number of Long's used to store the bits in the BitVector.
     */
    private int capacity;

    /**
     * The total number of bits that are currently set to 1
     */
    private int cardinality;

    /**
     * An array of longs used as the internal storage for the bits in the BitVector.
     */
    private long[] data;

    /**
     * Constructs a BitVector with the specified size.
     *
     * @param size the desired size of the bit vector. The actual size will be the nearest
     *             multiple of the size of a long (64 bits).
     */
    public BitVector(int size) {
        capacity = Math.ceilDiv(size, Long.SIZE);
        this.size = capacity * Long.SIZE;
        data = new long[capacity];
        cardinality = 0;
    }

    /**
     * Default constructor for the BitVector class.
     * Initialises a BitVector instance with a default initial capacity.
     * The initial capacity determines the number of bits the BitVector can store.
     */
    public BitVector() {
        this(64);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return cardinality == 0;
    }

    @Override
    public void clear() {
        data = new long[capacity];
        cardinality = 0;
    }

    @Override
    public boolean get(Integer ix) {
        if (isOutOfBounds(ix)) {
            return false;
        }

        int index = ix / Long.SIZE;
        long offset = ix % Long.SIZE;
        return (data[index] & (1L << offset)) != 0;
    }

    @Override
    public boolean put(Integer ix) {
        if (isOutOfBounds(ix)) {
            grow(ix);
        }

        int index = ix / Long.SIZE;
        long offset = ix % Long.SIZE;
        if ((data[index] & (1L << offset)) != 0) {
            // Bit is already set
            return true;
        }

        // Bit not yet set
        data[index] |= (1L << offset);
        cardinality++;
        return true;
    }

    @Override
    public boolean remove(Integer ix) {
        if (isOutOfBounds(ix)) {
            return false;
        }

        int index = ix / Long.SIZE;
        long offset = ix % Long.SIZE;
        if ((data[index] & (1L << offset)) != 0) {
            data[index] &= ~(1L << offset);
            cardinality--;
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (int word = 0; word < capacity; word++) {
            long current = data[word];
            if (current == 0) {
                continue;
            }

            int index = word * Long.SIZE;
            while(current != 0) {
                int bit = Long.numberOfTrailingZeros(current);
                if (!first) {
                    sb.append(',');
                }

                sb.append(index + bit);
                first = false;
                current &= current - 1; // This clears the least significant bit
            }
        }

        return sb.append('}').toString();
    }

    /**
     * Toggles the state of the bit at the specified index.
     * If the bit at the given index is set to true, it will be set to false.
     * If the bit is set to false, it will be set to true.
     *
     * @param ix the index of the bit to toggle
     */
    public void toggle(int ix) {
        if (get(ix)) {
            remove(ix);
        } else {
            put(ix);
        }
    }

    /**
     * Complements the bit values within the BitVector.
     * Each bit in the data array is flipped: all 0's become 1's, and all 1's become 0's.
     * This operation effectively toggles the state of all bits stored in the BitVector.
     */
    public void complement() {
        for (int i = 0; i < data.length; i++) {
            data[i] = ~data[i];
        }

        // Flip the count, all 0's are not 1's and vice versa
        cardinality = size - cardinality;
    }

    /**
     * Returns the number of set bits (population count) in the bit representation.
     *
     * @return the count of set bits in the internal bit representation
     */
    public int cardinality() {
        return cardinality;
    }

    /**
     * Shifts the bits in the BitVector by the specified distance. This operation moves all bits
     * either left or right depending on the sign of the distance. A positive distance performs
     * a left shift, and a negative distance performs a right shift. Bits shifted out of the BitVector
     * are discarded and zero-filled from the opposite side.
     *
     * @param dist the distance by which to shift the bits. A positive value shifts the bits to
     *             the left, and a negative value shifts them to the right. If the absolute value
     *             of the distance is greater than or equal to the size of the BitVector, all bits
     *             will be set to zero.
     */
    public void shift(long dist) {
        if (dist == 0) {
            return;
        } else if (Math.abs(dist) >= size) {
            data = new long[capacity];
            cardinality = 0;
            return;
        }

        int wordShift = (int) Math.abs(dist / Long.SIZE);
        int bitShift = (int) Math.abs(dist % Long.SIZE);
        updatePop(wordShift, bitShift, dist);
        if (dist > 0) {
            data = leftShift(wordShift, bitShift);
        } else {
            data = rightShift(wordShift, bitShift);
        }
    }

    /**
     * Rotates the bits in the BitVector by the specified distance.
     * This operation moves all bits in a circular manner: bits shifted out of one end
     * re-enter from the opposite end. A positive `dist` value indicates a left rotation,
     * whereas a negative `dist` value results in a right rotation.
     *
     * @param dist the distance by which to rotate the bits. A positive value rotates
     *             the bits to the left, while a negative value rotates them to the right.
     */
    public void rotate(long dist) {
        if (dist == 0 || size == 0 || dist % size == 0) {
            return;
        }

        // Normalise dist to be in the range [0, size)
        dist = (dist < 0) ? dist % size + size : dist % size;

        if (data.length == 1) {
            data[0] = (data[0] << dist) | (data[0] >>> (size - dist));
            return;
        }

        int wordShift = (int) (dist / Long.SIZE);
        int bitShift = (int) (dist % Long.SIZE);
        long[] leftShifted = leftShift(wordShift, bitShift);
        long rightDist = size - dist;
        wordShift = (int) (rightDist / Long.SIZE);
        bitShift = (int) (rightDist % Long.SIZE);
        long[] rightShifted = rightShift(wordShift, bitShift);
        // merge the shifted arrays
        for (int i = 0; i < data.length; i++) {
            data[i] = leftShifted[i] | rightShifted[i];
        }
    }

    /**
     * Calculates and returns the position of the highest set bit in the data array.
     * If no bits are set, the method returns -1.
     *
     * @return the position of the highest set bit in the data array, or -1 if no bits are set
     */
    public int highestBit() {
        for (int i = data.length - 1; i >= 0; i--) {
            if (data[i] != 0L) {
                int bitPos = 63 - Long.numberOfLeadingZeros(data[i]);
                return (i * Long.SIZE) + bitPos;
            }
        }

        return -1;
    }

    /**
     * Computes the maximum consecutive count of true values in a sequence. This method iterates
     * over a sequence of boolean values, tracking the longest consecutive sequence of true entries.
     * It evaluates the total count by comparing the current streak against the highest streak found
     * so far.
     *
     * @return The maximum number of consecutive true values in the sequence.
     */
    public long runCount() {
        long total = 0;
        long current = 0;
        for (int i = 0; i < size; i++) {
            if (get(i)) {
                current++;
            } else {
                if (current > total) {
                    total = current;
                }

                current = 0;
            }
        }

        return Math.max(current, total);
    }

    /**
     * Expands the capacity of the BitVector to accommodate the specified index.
     * This method ensures the internal data array is resized to fit the number of
     * bits necessary to store the given index, maintaining alignment with the size
     * of a long (64 bits).
     *
     * @param ix the index that must be accommodated in the BitVector. The method
     *           calculates the necessary capacity to contain this index and expands
     *           the internal data array accordingly.
     */
    private void grow(int ix) {
        capacity = Math.ceilDiv(ix + 1, Long.SIZE);
        size = capacity * Long.SIZE;
        long[] newData = new long[capacity];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    /**
     * Checks whether the specified index is out of bounds for the BitVector.
     * Throws an IndexOutOfBoundsException if the index is negative.
     *
     * @param ix the index to check for bounds validity. A valid index is non-negative and less than the size of the BitVector.
     * @return true if the index is greater than or equal to the size of the BitVector, false otherwise.
     */
    private boolean isOutOfBounds(int ix) {
        if (ix < 0) {
            throw new IndexOutOfBoundsException();
        }

        return ix >= size;
    }


    /**
     * Performs a left shift operation on the internal data array, shifting the bits
     * by the specified number of words and additional bits within each word.
     * This method operates on the internal storage structure of the BitVector
     * and returns a new array representing the shifted state.
     *
     * @param wordShift the number of words to shift the bits to the left.
     * @param bitShift  the number of bits to shift each word internally to the left.
     * @return a new array containing the result of the left shift operation.
     */
    private long[] leftShift(int wordShift, int bitShift) {
        long[] result = new long[data.length];
        if (data.length == 1) {
            result[0] = data[0] << bitShift;
            return result;
        }

        System.arraycopy(data, 0, result, wordShift, data.length - wordShift);
        if (bitShift != 0) {
            for (int i = result.length - 1; i >= 0; i--) {
                result[i] <<= bitShift;
                result[i] |= (i > 0) ? result[i - 1] >>> (Long.SIZE - bitShift) : 0;
            }
        }

        return result;
    }

    /**
     * Performs a right shift operation on the internal data array, shifting the bits
     * by the specified number of words and additional bits within each word.
     * This method operates on the internal storage structure of the BitVector
     * and returns a new array representing the shifted state.
     *
     * @param wordShift the number of words to shift the bits to the right.
     * @param bitShift  the number of bits to shift each word internally to the right.
     * @return a new array containing the result of the right shift operation.
     */
    private long[] rightShift(int wordShift, int bitShift) {
        long[] result = new long[data.length];
        if (data.length == 1) {
            result[0] = data[0] >>> bitShift;
            return result;
        }

        System.arraycopy(data, wordShift, result, 0, data.length - wordShift);
        if (bitShift != 0) {
            for (int i = 0; i < result.length; i++) {
                result[i] >>>= bitShift;
                result[i] |= (i < result.length - 1) ? result[i + 1] << (Long.SIZE - bitShift) : 0;
            }
        }

        return result;
    }

    /**
     * Updates the population count (cardinality) of the BitVector after shifting its internal data.
     * This method calculates and adjusts the population count by removing bits that are shifted
     * out of the valid range, accounting for both word and bit shifts.
     *
     * @param wordShift the number of whole words (64-bit sections) by which the bits are shifted.
     *                  A positive value indicates a left shift, and a negative value indicates a right shift.
     * @param bitShift  the number of additional bits to shift within a word after applying the word shift.
     *                  A positive value adjusts the population count based on bits shifted out of the ends.
     * @param dist      the shift distance, where a positive value represents leftward movement
     *                  and a negative value represents rightward movement.
     */
    private void updatePop(int wordShift, int bitShift, long dist) {
        if (dist == 0) {
            return;
        }

        // Remove entire words worth of bits
        if (wordShift > 0) {
            if (dist > 0) {
                for (int i = data.length - wordShift; i < data.length; i++) {
                    cardinality -= Long.bitCount(data[i]);
                }
            } else {
                for (int i = 0; i < wordShift; i++) {
                    cardinality -= Long.bitCount(data[i]);
                }
            }
        }

        // Remove the carry-over bits from the last word
        if (bitShift > 0) {
            long word;
            if (dist > 0) {
                word = data[data.length - 1 - wordShift];
                word &= -1L << (Long.SIZE - bitShift); // mask
            } else {
                word = data[wordShift];
                word &= (1L << bitShift) - 1; // mask
            }

            cardinality -= Long.bitCount(word);
        }
    }
}
