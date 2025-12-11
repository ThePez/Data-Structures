import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import structures.ArrayList;

import java.util.Comparator;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * JUnit 4 test suite for custom ArrayList
 */
public class TestArrayList {

    private ArrayList<Integer> list;
    private java.util.ArrayList<Integer> javaList;
    private final Random random = new Random(System.currentTimeMillis());

    @Before
    public void setUp() {
        list = new ArrayList<>();
        javaList = new java.util.ArrayList<>();
    }

    @After
    public void tearDown() {
        list = null;
        javaList = null;
    }

    // ------------------ Constructors ------------------

    @Test
    public void testDefaultConstructor() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertEquals(10, list.getCapacity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCapacityConstructor() {
        new ArrayList<Integer>(0);
    }

    @Test
    public void testArrayConstructor() {
        Integer[] values = {1, 2, 3, 4};
        ArrayList<Integer> l = new ArrayList<>(values);

        assertEquals(4, l.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], l.get(i));
        }
    }

    @Test
    public void testListConstructor() {
        list.append(1);
        list.append(2);
        list.append(3);

        ArrayList<Integer> copy = new ArrayList<>(list);

        assertEquals(3, copy.size());
        assertEquals((Integer) 1, copy.get(0));
        assertEquals((Integer) 2, copy.get(1));
        assertEquals((Integer) 3, copy.get(2));
    }

    // ------------------ Add Operations ------------------

    @Test
    public void testAppend() {
        list.append(10);
        list.append(20);

        assertEquals(2, list.size());
        assertEquals((Integer)10, list.get(0));
        assertEquals((Integer)20, list.get(1));
    }

    @Test
    public void testPrepend() {
        list.append(10);
        list.prepend(5);

        assertEquals(2, list.size());
        assertEquals((Integer)5, list.get(0));
        assertEquals((Integer)10, list.get(1));
    }

    @Test
    public void testAddAtIndex() {
        list.append(1);
        list.append(3);
        list.add(1, 2);

        assertEquals(3, list.size());
        assertEquals((Integer)2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddInvalidIndex() {
        list.add(5, 10);
    }

    // ------------------ Getters ------------------

    @Test
    public void testGetFirstAndLast() {
        assertNull(list.getFirst());
        assertNull(list.getLast());

        list.append(1);
        list.append(2);
        list.append(3);

        assertEquals((Integer)1, list.getFirst());
        assertEquals((Integer)3, list.getLast());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        list.get(0);
    }

    @Test
    public void testGetAtIndex() {
        list.append(1);
        list.append(2);
        list.append(3);

        assertEquals((Integer)2, list.get(1));
        assertEquals((Integer)3, list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    public void testContains() {
        list.append(1);
        list.append(2);
        list.append(3);

        assertTrue(list.contains(2));
        assertFalse(list.contains(4));
    }

    // ------------------ Set ------------------

    @Test
    public void testSet() {
        list.append(1);
        list.append(2);

        Integer old = list.set(1, 99);

        assertEquals((Integer)2, old);
        assertEquals((Integer)99, list.get(1));
    }

    // ------------------ Removal ------------------

    @Test
    public void testRemoveAtIndex() {
        list.append(1);
        list.append(2);
        list.append(3);

        Integer removed = list.remove(1);

        assertEquals((Integer)2, removed);
        assertEquals(2, list.size());
        assertEquals((Integer)3, list.get(1));
    }

    @Test
    public void testRemoveFirstAndLast() {
        list.append(1);
        list.append(2);
        list.append(3);

        assertEquals((Integer)1, list.removeFirst());
        assertEquals((Integer)3, list.removeLast());

        assertEquals(1, list.size());
        assertEquals((Integer)2, list.get(0));
    }

    @Test
    public void testRemoveObject() {
        list.append(5);
        list.append(10);
        list.append(20);

        assertTrue(list.remove((Object)10));
        assertFalse(list.remove((Object)999));

        assertEquals(2, list.size());
        assertEquals((Integer)20, list.get(1));
    }

    // ------------------ Clear ------------------

    @Test
    public void testClear() {
        list.append(1);
        list.append(2);

        list.clear();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertEquals(10, list.getCapacity());
    }

    // ------------------ Resize ------------------

    @Test
    public void testResizeBeyondDefaultCapacity() {
        for (int i = 0; i < 20; i++) {
            list.append(i);
        }

        assertEquals(20, list.size());
        assertTrue(list.getCapacity() >= 20);
    }

    // ------------------ Sorting ------------------

    @Test
    public void testSortWithComparator() {
        list.append(5);
        list.append(1);
        list.append(3);
        list.append(9);

        list.sort(Comparator.naturalOrder());

        assertArrayEquals(new Integer[]{1,3,5,9}, list.toArray());
    }

    @Test
    public void testSortReverse() {
        list.append(5);
        list.append(1);
        list.append(3);

        list.sort(Comparator.reverseOrder());

        assertArrayEquals(new Integer[]{5,3,1}, list.toArray());
    }

    // ------------------ Stress Test ------------------

    @Test
    public void testVsJavaArrayList() {
        for (int i = 0; i < 5_000_000; i++) {
            int op = random.nextInt(4);

            switch (op) {
                case 0: // add
                    int v = random.nextInt(1000);
                    list.append(v);
                    javaList.add(v);
                    break;

                case 1: // remove
                    if (!javaList.isEmpty()) {
                        int ix = random.nextInt(javaList.size());
                        assertEquals(javaList.remove(ix), list.remove(ix));
                    }
                    break;

                case 2: // set
                    if (!javaList.isEmpty()) {
                        int ix = random.nextInt(javaList.size());
                        int val = random.nextInt(5000);

                        Integer javaOld = javaList.set(ix, val);
                        Integer yourOld = list.set(ix, val);

                        assertEquals(javaOld, yourOld);
                    }
                    break;

                case 3: // clear
                    if (random.nextInt(2000) == 0) {
                        list.clear();
                        javaList.clear();
                    }
                    break;
            }

            assertEquals("Size mismatch at step " + i,
                    javaList.size(), list.size());

            if (i % 100_000 == 0) {
                System.out.println("Step " + i + " OK");
            }
        }
    }
}
