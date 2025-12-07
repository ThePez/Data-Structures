import org.junit.Before;
import org.junit.Test;
import structures.BitVector;

import java.util.BitSet;
import java.util.Random;

import static org.junit.Assert.*;

public class TestBitVector {

    /**
     * Shifts the bits in the given BitSet by a specified amount, either left or right,
     * within the bounds of the specified size. Bits that are shifted out of range
     * are discarded, and the resulting BitSet is returned.
     *
     * @param original the original BitSet to be shifted
     * @param size the capacity of the original BitSet
     * @param shift the number of positions to shift the bits; positive for left shift,
     *              negative for right shift
     * @return a new BitSet with the shifted bits
     */
    private BitSet shiftBitSet(BitSet original, int size, int shift) {
        BitSet shifted = new BitSet(size);
        if (shift == 0) {
            return (BitSet) original.clone();
        }

        for (int i = original.nextSetBit(0); i >= 0; i = original.nextSetBit(i + 1)) {
            int newPos = i + shift;
            if (newPos < size && newPos >= 0) {
                shifted.set(newPos);
            }
        }

        return shifted;
    }

    /**
     * Rotates the bits in the given BitSet by a specified distance within the specified size.
     * A positive distance rotates the bits to the left, while a negative distance rotates them to
     * the right. Bits rotated out of bounds are wrapped around to the other end.
     *
     * @param bs the BitSet to be rotated
     * @param size the size of the BitSet to consider during rotation
     * @param dist the distance by which to rotate the bits
     * @return a new BitSet containing the rotated bits
     */
    private BitSet rotateBitSet(BitSet bs, int size, int dist) {
        BitSet result = new BitSet(size);
        dist = ((dist % size) + size) % size; // normalize
        for (int i = 0; i < size; i++) {
            if (bs.get(i)) {
                int newPos = (i + dist) % size;
                result.set(newPos);
            }
        }

        return result;
    }

    private BitVector vec;

    @Before
    public void setup() {
        vec = new BitVector(127);
    }

    @Test
    public void testDefaultConstructor() {
        BitVector v = new BitVector();
        assertEquals(64, v.size());
        assertEquals(0, v.cardinality());
        assertTrue(v.isEmpty());
    }

    @Test
    public void testSizedConstructorRoundsTo64() {
        BitVector v = new BitVector(70);
        assertEquals(128, v.size());
        assertTrue(v.isEmpty());
    }

    @Test
    public void testPutGetRemoveSingle() {
        assertFalse(vec.get(5));
        vec.put(5);
        assertTrue(vec.get(5));
        assertEquals(1, vec.cardinality());

        vec.remove(5);
        assertFalse(vec.get(5));
        assertEquals(0, vec.cardinality());
    }

    @Test
    public void testDoublePutDoesNotIncreaseCardinality() {
        vec.put(10);
        vec.put(10);
        assertTrue(vec.get(10));
        assertEquals(1, vec.cardinality());
    }

    @Test
    public void testRemoveUnsetDoesNothing() {
        assertFalse(vec.remove(50));
        assertEquals(0, vec.cardinality());
    }

    @Test
    public void testPutGrowsVector() {
        int ix = 128;
        vec.put(ix);

        assertTrue(vec.get(ix));
        assertTrue(vec.size() >= 128);
        assertEquals(1, vec.cardinality());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeThrows() {
        vec.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutNegativeThrows() {
        vec.put(-3);
    }

    @Test
    public void testToggle() {
        vec.toggle(12);
        assertTrue(vec.get(12));

        vec.toggle(12);
        assertFalse(vec.get(12));
    }

    @Test
    public void testComplementEmpty() {
        vec.complement();
        assertEquals(vec.size(), vec.cardinality());

        for (int i = 0; i < vec.size(); i++) {
            assertTrue(vec.get(i));
        }
    }

    @Test
    public void testComplementPartial() {
        vec.put(0);
        vec.put(64);
        vec.complement();

        assertFalse(vec.get(0));
        assertFalse(vec.get(64));
        assertEquals(vec.size() - 2, vec.cardinality());
    }

    @Test
    public void testLeftShift() {
        vec.put(1);
        vec.put(2);
        vec.shift(2);

        assertFalse(vec.get(1));
        assertTrue(vec.get(3));
        assertTrue(vec.get(4));
    }

    @Test
    public void testLeftShiftFull() {
        vec.put(63);
        vec.shift(1);
        assertFalse(vec.get(63));
        assertTrue(vec.get(64));
        assertEquals(1, vec.cardinality());
    }

    @Test
    public void testRightShift() {
        vec.put(10);
        vec.put(20);
        vec.shift(-5);

        assertTrue(vec.get(5));
        assertTrue(vec.get(15));
        assertFalse(vec.get(10));
    }

    @Test
    public void testShiftTooFarClears() {
        vec.put(1);
        vec.put(2);
        vec.shift(1000);

        assertEquals(0, vec.cardinality());
    }

    @Test
    public void testRotateLeft() {
        vec = new BitVector(64);
        vec.put(0);
        vec.rotate(1);

        assertTrue(vec.get(1));
        assertFalse(vec.get(0));
    }

    @Test
    public void testRotateWrapAround() {
        vec = new BitVector(63);
        vec.put(63);
        vec.rotate(1);

        assertTrue(vec.get(0));
        assertFalse(vec.get(63));
    }

    @Test
    public void testRotateRight() {
        vec.put(0);
        vec.rotate(-1);

        assertTrue(vec.get(127)); // last bit
        assertFalse(vec.get(0));
    }

    @Test
    public void testHighestBitEmpty() {
        assertEquals(-1, vec.highestBit());
    }

    @Test
    public void testHighestBit() {
        vec.put(77);
        vec.put(12);
        vec.put(99);

        assertEquals(99, vec.highestBit());
    }

    @Test
    public void testRunCountSimple() {
        vec.put(1);
        vec.put(2);
        vec.put(3);
        assertEquals(3, vec.runCount());
    }

    @Test
    public void testRunCountWithBreaks() {
        vec.put(2);
        vec.put(3);
        vec.put(7);
        vec.put(8);
        vec.put(9);

        assertEquals(3, vec.runCount());
    }

    @Test
    public void testAgainstBitSet() {
        BitSet bs = new BitSet();
        Random r = new Random(123);

        for (int i = 0; i < 10_000; i++) {
            int ix = r.nextInt(500);

            if (r.nextBoolean()) {
                vec.put(ix);
                bs.set(ix);
            } else {
                vec.remove(ix);
                bs.clear(ix);
            }
        }

        for (int i = 0; i < 500; i++) {
            assertEquals("Mismatch at bit " + i, bs.get(i), vec.get(i));
        }

        assertEquals(bs.cardinality(), vec.cardinality());
    }

    @Test
    public void randomizedFuzzTest() {
        BitSet bs = new BitSet();
        Random r = new Random(42);

        for (int i = 0; i < 100_000; i++) {
            int op = r.nextInt(6);
            int ix = r.nextInt(128);

            switch (op) {
                case 0 -> {
                    vec.put(ix);
                    bs.set(ix);
                }
                case 1 -> {
                    vec.remove(ix);
                    bs.clear(ix);
                }
                case 2 -> {
                    vec.toggle(ix);
                    bs.flip(ix);
                }
                case 3 -> {
                    int shift = r.nextInt(20) - 10;
                    vec.shift(shift);
                    bs = shiftBitSet(bs, vec.size(), shift);
                }
                case 4 -> {
                    int rotate = r.nextInt(50) - 25;
                    vec.rotate(rotate);
                    bs = rotateBitSet(bs, vec.size(), rotate);
                }
                case 5 -> {
                    vec.complement();
                    bs.flip(0, vec.size());
                }
            }

            for (int j = 0; j < vec.size(); j++) {
                assertEquals("After " + i + "ops, Mismatch at index " + j, bs.get(j), vec.get(j));
            }
        }
    }
}
