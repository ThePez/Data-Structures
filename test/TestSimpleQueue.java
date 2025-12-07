import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import structures.SimpleQueue;

import java.util.*;

import static org.junit.Assert.*;

/**
 * JUnit 4 test suite for SimpleQueue class
 */
public class TestSimpleQueue {

    private SimpleQueue<Integer> customQueue;
    private Queue<Integer> javaQueue;
    private long randomSeed;

    @Before
    public void setUp() {
        randomSeed = System.currentTimeMillis();
        customQueue = new SimpleQueue<>();
        javaQueue = new LinkedList<>();
    }

    @After
    public void tearDown() {
        customQueue = null;
        javaQueue = null;
    }

    @Test
    public void testConstructor() {
        assertTrue("New queue should be empty", customQueue.isEmpty());
        assertEquals("New queue size should be 0", 0, customQueue.size());
    }

    @Test
    public void testEnqueueAndPeek() {
        customQueue.enqueue(10);
        customQueue.enqueue(20);

        assertEquals("Peek should return front element", Integer.valueOf(10), customQueue.peek());
        assertEquals("Queue size should be 2", 2, customQueue.size());
    }

    @Test
    public void testDequeue() {
        customQueue.enqueue(5);
        customQueue.enqueue(15);

        assertEquals("Dequeue should return first enqueued element", Integer.valueOf(5), customQueue.dequeue());
        assertEquals("Next dequeue should return 15", Integer.valueOf(15), customQueue.dequeue());
        assertTrue("Queue should be empty after dequeuing all elements", customQueue.isEmpty());
    }

    @Test
    public void testDequeueOnEmptyQueueReturnsNull() {
        assertNull("Dequeue on empty queue should return null", customQueue.dequeue());
    }

    @Test
    public void testPeekOnEmptyQueueReturnsNull() {
        assertNull("Peek on empty queue should return null", customQueue.peek());
    }

    @Test
    public void testClear() {
        customQueue.enqueue(1);
        customQueue.enqueue(2);
        customQueue.enqueue(3);

        assertEquals("Queue should have size 3", 3, customQueue.size());

        customQueue.clear();

        assertTrue("Queue should be empty after clear", customQueue.isEmpty());
        assertEquals("Queue size should be 0 after clear", 0, customQueue.size());
    }

    @Test
    public void testFIFOOrder() {
        // Test First-In-First-Out ordering
        for (int i = 0; i < 10; i++) {
            customQueue.enqueue(i);
        }

        for (int i = 0; i < 10; i++) {
            assertEquals("Elements should dequeue in FIFO order", Integer.valueOf(i), customQueue.dequeue());
        }

        assertTrue("Queue should be empty after dequeuing all elements", customQueue.isEmpty());
    }

    @Test
    public void testLargeQueue() {
        // Test with large number of elements
        for (int i = 0; i < 1000; i++) {
            customQueue.enqueue(i);
        }
        assertEquals("Queue size should be 1000 after enqueueing 1000 elements", 1000, customQueue.size());
        assertEquals("Front element should be 0", Integer.valueOf(0), customQueue.peek());
    }

    @Test
    public void testVsJavaQueue() {
        Random random = new Random(randomSeed);
        for (int step = 0; step < 10_000_000; step++) {
            int op = random.nextInt(4);
            int val;

            switch (op) {
                case 0: // enqueue
                    val = random.nextInt(1000);
                    customQueue.enqueue(val);
                    javaQueue.offer(val);
                    break;

                case 1: // dequeue
                    if (!javaQueue.isEmpty()) {
                        Integer expected = javaQueue.poll();
                        Integer actual = customQueue.dequeue();
                        assertEquals("Dequeue mismatch at step " + step, expected, actual);
                    } else {
                        assertNull("Dequeue on empty queue should return null at step " + step,
                                customQueue.dequeue());
                    }
                    break;

                case 2: // peek
                    if (!javaQueue.isEmpty()) {
                        assertEquals("Peek mismatch at step " + step, javaQueue.peek(), customQueue.peek());
                    } else {
                        assertNull("Peek on empty queue should return null at step " + step,
                                customQueue.peek());
                    }
                    break;

                case 3: // clear
                    customQueue.clear();
                    javaQueue.clear();
                    break;
            }

            assertEquals("Size mismatch at step " + step, javaQueue.size(), customQueue.size());
            if (step % 1_000_000 == 0) {
                System.out.println("Loop " + step + " ok");
            }
        }
    }

    @Test
    public void testMixedOperations() {
        Random random = new Random(randomSeed);

        for (int step = 0; step < 10_000_000; step++) {
            if (random.nextBoolean()) {
                int val = random.nextInt(10_000);
                customQueue.enqueue(val);
                javaQueue.offer(val);
            } else {
                if (!javaQueue.isEmpty()) {
                    Integer expected = javaQueue.poll();
                    Integer actual = customQueue.dequeue();
                    assertEquals("Dequeue mismatch at step " + step, expected, actual);
                } else {
                    assertNull("Dequeue on empty queue should return null", customQueue.dequeue());
                }
            }

            assertEquals("Size mismatch at step " + step, javaQueue.size(), customQueue.size());
            if (step % 1_000_000 == 0) {
                System.out.println("Loop " + step + " ok");
                System.out.println("Queue size: " + customQueue.size());
                javaQueue.clear();
                customQueue.clear();
            }
        }
    }

    @Test
    public void testEnqueueAfterDequeue() {
        customQueue.enqueue(1);
        customQueue.enqueue(2);
        customQueue.dequeue();
        customQueue.enqueue(3);

        assertEquals("Size should be 2", 2, customQueue.size());
        assertEquals("Front should be 2", Integer.valueOf(2), customQueue.peek());
    }

    @Test
    public void testPeekDoesNotModify() {
        customQueue.enqueue(100);
        customQueue.enqueue(200);

        customQueue.peek();
        customQueue.peek();
        customQueue.peek();

        assertEquals("Multiple peeks should not change size", 2, customQueue.size());
        assertEquals("Peek should still return same element", Integer.valueOf(100), customQueue.peek());
    }
}