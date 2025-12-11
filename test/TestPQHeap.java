import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import structures.PQHeap;

import java.util.*;

import static org.junit.Assert.*;

/**
 * JUnit 4 test suite for PQHeap
 */
public class TestPQHeap {

    private PQHeap<Integer> minHeap;
    private PQHeap<Integer> maxHeap;
    private PriorityQueue<Integer> javaMinHeap;
    private PriorityQueue<Integer> javaMaxHeap;
    private final Random random = new Random(System.currentTimeMillis());

    @Before
    public void setUp() {
        minHeap = new PQHeap<>();
        maxHeap = new PQHeap<>(true);
        javaMinHeap = new PriorityQueue<>(); // default min-heap
        javaMaxHeap = new PriorityQueue<>(Comparator.reverseOrder()); // max-heap
    }

    @After
    public void tearDown() {
        minHeap = null;
        maxHeap = null;
        javaMinHeap = null;
        javaMaxHeap = null;
    }

    @Test
    public void testConstructor() {
        assertTrue("New heap should be empty", minHeap.isEmpty());
        assertEquals("New heap size should be 0", 0, minHeap.size());
    }

    @Test
    public void testHeapifyConstructor() {
        for (int i = 0; i < 100; i++) {
            javaMinHeap.add(random.nextInt(100));
            javaMaxHeap.add(random.nextInt(100));
        }

        maxHeap = new PQHeap<>(true, javaMaxHeap.toArray(new Integer[0]));
        minHeap = new PQHeap<>(false, javaMinHeap.toArray(new Integer[0]));

        for (int i = 0; i < 100; i++) {
            assertEquals(javaMinHeap.poll(), minHeap.poll());
            assertEquals(javaMaxHeap.poll(), maxHeap.poll());
        }
    }

    @Test
    public void testAddAndPeekMinHeap() {
        minHeap.add(5);
        minHeap.add(3);
        minHeap.add(7);

        assertEquals("Peek should return smallest element", Integer.valueOf(3), minHeap.peek());
    }

    @Test
    public void testAddAndPeekMaxHeap() {
        maxHeap.add(5);
        maxHeap.add(3);
        maxHeap.add(7);

        assertEquals("Peek should return largest element", Integer.valueOf(7), maxHeap.peek());
    }

    @Test
    public void testPollMinHeap() {
        minHeap.add(10);
        minHeap.add(4);
        minHeap.add(8);

        assertEquals("First poll should return 4", Integer.valueOf(4), minHeap.poll());
        assertEquals("Second poll should return 8", Integer.valueOf(8), minHeap.poll());
        assertEquals("Third poll should return 10", Integer.valueOf(10), minHeap.poll());
        assertTrue("Heap should be empty after polling all elements", minHeap.isEmpty());
    }

    @Test
    public void testPollMaxHeap() {
        maxHeap.add(10);
        maxHeap.add(4);
        maxHeap.add(8);

        assertEquals("First poll should return 10", Integer.valueOf(10), maxHeap.poll());
        assertEquals("Second poll should return 8", Integer.valueOf(8), maxHeap.poll());
        assertEquals("Third poll should return 4", Integer.valueOf(4), maxHeap.poll());
        assertTrue("Heap should be empty after polling all elements", maxHeap.isEmpty());
    }

    @Test
    public void testClear() {
        maxHeap.add(1);
        maxHeap.add(2);
        maxHeap.add(3);
        assertEquals("Heap should have size 3", 3, maxHeap.size());

        maxHeap.clear();
        assertTrue("Heap should be empty after clear", maxHeap.isEmpty());
        assertEquals("Heap size should be 0 after clear", 0, maxHeap.size());
    }

    @Test
    public void testToArray() {
        maxHeap.add(1);
        maxHeap.add(2);

        Object[] arr = maxHeap.toArray();
        assertEquals("Array length should match heap size", 2, arr.length);
        assertTrue("Array should contain 1 or 2", arr[0].equals(1) || arr[0].equals(2));
    }

    @Test
    public void testRemoveElementMinHeap() {
        minHeap.add(5);
        minHeap.add(3);
        minHeap.add(7);
        minHeap.add(1);

        assertTrue("Remove should return true for existing element", minHeap.remove(3));
        assertEquals("Heap size should decrease after remove", 3, minHeap.size());
        assertFalse("Heap should not contain removed element", Arrays.asList(minHeap.toArray()).contains(3));

        assertFalse("Remove should return false for non-existing element", minHeap.remove(42));
    }

    @Test
    public void testRemoveElementMaxHeap() {
        maxHeap.add(10);
        maxHeap.add(20);
        maxHeap.add(5);

        assertTrue("Remove should return true for existing element", maxHeap.remove(20));
        assertEquals("Heap size should decrease after remove", 2, maxHeap.size());
        assertFalse("Heap should not contain removed element", Arrays.asList(maxHeap.toArray()).contains(20));
        assertFalse("Remove should return false for non-existing element", maxHeap.remove(99));
    }

    @Test
    public void testSortMinHeap() {
        minHeap.add(5);
        minHeap.add(1);
        minHeap.add(3);
        minHeap.add(7);

        Object[] sorted = minHeap.sort().toArray();
        assertArrayEquals("Sort should return ascending order", new Integer[]{7, 5, 3, 1}, sorted);
    }

    @Test
    public void testSortMaxHeap() {
        maxHeap.add(5);
        maxHeap.add(1);
        maxHeap.add(3);
        maxHeap.add(7);

        Object[] sorted = maxHeap.sort().toArray();
        assertArrayEquals("Sort should return descending order", new Integer[]{1, 3, 5, 7}, sorted);
    }


    @Test
    public void testVsPriorityQueueMinHeap() {
        for (int step = 0; step < 10_000_000; step++) {
            int op = random.nextInt(5);
            int val;

            switch (op) {
                case 0: // add
                    val = random.nextInt(10_000);
                    minHeap.add(val);
                    javaMinHeap.add(val);
                    break;
                case 1: // poll
                    if (!javaMinHeap.isEmpty()) {
                        Integer expected = javaMinHeap.poll();
                        Integer actual = minHeap.poll();
                        assertEquals("Poll mismatch at step " + step, expected, actual);
                    }
                    break;
                case 2: // peek
                    if (!javaMinHeap.isEmpty()) {
                        assertEquals("Peek mismatch at step " + step,
                                javaMinHeap.peek(), minHeap.peek());
                    } else {
                        assertNull("Peek on empty heap should be null", minHeap.peek());
                    }
                    break;
                case 3: // Clear
                    minHeap.clear();
                    javaMinHeap.clear();
                    break;
                case 4: // sort
                    Object[] sorted = minHeap.sort().toArray();
                    Object[] sortedJava = javaMinHeap.stream().sorted(Comparator.reverseOrder()).toArray();
                    assertArrayEquals("Sort mismatch at step " + step, sortedJava, sorted);
                    break;
            }

            assertEquals("Size mismatch at step " + step, javaMinHeap.size(), minHeap.size());
            if (step % 1_000_000 == 0) {
                System.out.println("Loop " + step + " ok");
            }
        }
    }

    @Test
    public void testVsPriorityQueueMaxHeap() {
        for (int step = 0; step < 10_000_000; step++) {
            int op = random.nextInt(5);
            int val;

            switch (op) {
                case 0: // add
                    val = random.nextInt(10_000);
                    maxHeap.add(val);
                    javaMaxHeap.add(val);
                    break;
                case 1: // poll
                    if (!javaMaxHeap.isEmpty()) {
                        Integer expected = javaMaxHeap.poll();
                        Integer actual = maxHeap.poll();
                        assertEquals("Poll mismatch at step " + step, expected, actual);
                    }
                    break;
                case 2: // peek
                    if (!javaMaxHeap.isEmpty()) {
                        assertEquals("Peek mismatch at step " + step,
                                javaMaxHeap.peek(), maxHeap.peek());
                    } else {
                        assertNull("Peek on empty heap should be null", maxHeap.peek());
                    }
                    break;
                case 3: // Clear
                    maxHeap.clear();
                    javaMaxHeap.clear();
                    break;
                case 4: // sort
                    Object[] sorted = maxHeap.sort().toArray();
                    Object[] sortedJava = javaMaxHeap.stream().sorted().toArray();
                    assertArrayEquals("Sort mismatch at step " + step, sortedJava, sorted);
                    break;
            }

            assertEquals("Size mismatch at step " + step, javaMaxHeap.size(), maxHeap.size());
            if (step % 1_000_000 == 0) {
                System.out.println("Loop " + step + " ok");
            }
        }
    }
}
