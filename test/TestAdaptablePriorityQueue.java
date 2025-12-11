import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import containers.ArrayEntry;
import structures.AdaptablePriorityQueue;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Comprehensive JUnit 4 test suite for AdaptablePriorityQueue.
 * Includes basic functionality tests and large-scale fuzz testing
 * against Java's built-in PriorityQueue for validation.
 */
public class TestAdaptablePriorityQueue {

    private AdaptablePriorityQueue<String> minHeap;
    private AdaptablePriorityQueue<String> maxHeap;

    @Before
    public void setUp() {
        minHeap = new AdaptablePriorityQueue<>();
        maxHeap = new AdaptablePriorityQueue<>(true);
    }

    // ==================== BASIC FUNCTIONALITY TESTS ====================

    @Test
    public void testConstructorDefault() {
        AdaptablePriorityQueue<String> pq = new AdaptablePriorityQueue<>();
        assertTrue("New queue should be empty", pq.isEmpty());
        assertEquals("Size should be 0", 0, pq.size());
    }

    @Test
    public void testConstructorWithMaxFlag() {
        AdaptablePriorityQueue<String> pq = new AdaptablePriorityQueue<>(true);
        assertTrue("New queue should be empty", pq.isEmpty());
    }

    @Test
    public void testConstructorWithCapacity() {
        AdaptablePriorityQueue<String> pq = new AdaptablePriorityQueue<>(false, 100);
        assertTrue("New queue should be empty", pq.isEmpty());
    }

    @Test
    public void testAddSingleElement() {
        ArrayEntry<Integer, String> entry = new ArrayEntry<>(5, "test");
        assertTrue("Add should return true", minHeap.add(entry));
        assertEquals("Size should be 1", 1, minHeap.size());
        assertFalse("Queue should not be empty", minHeap.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullElement() {
        minHeap.add(null);
    }

    @Test
    public void testPeekSingleElement() {
        ArrayEntry<Integer, String> entry = new ArrayEntry<>(5, "test");
        minHeap.add(entry);
        assertEquals("Peek should return the added element", entry, minHeap.peek());
        assertEquals("Size should remain 1", 1, minHeap.size());
    }

    @Test
    public void testPollSingleElement() {
        ArrayEntry<Integer, String> entry = new ArrayEntry<>(5, "test");
        minHeap.add(entry);
        assertEquals("Poll should return the added element", entry, minHeap.poll());
        assertTrue("Queue should be empty after poll", minHeap.isEmpty());
    }

    @Test
    public void testPollEmptyQueue() {
        assertNull("Poll on empty queue should return null", minHeap.poll());
    }

    @Test
    public void testMinHeapOrdering() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(5, "five");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(2, "two");
        ArrayEntry<Integer, String> e3 = new ArrayEntry<>(8, "eight");
        ArrayEntry<Integer, String> e4 = new ArrayEntry<>(1, "one");

        minHeap.add(e1);
        minHeap.add(e2);
        minHeap.add(e3);
        minHeap.add(e4);

        assertEquals("Should poll minimum (1)", e4, minHeap.poll());
        assertEquals("Should poll next minimum (2)", e2, minHeap.poll());
        assertEquals("Should poll next minimum (5)", e1, minHeap.poll());
        assertEquals("Should poll next minimum (8)", e3, minHeap.poll());
    }

    @Test
    public void testMaxHeapOrdering() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(5, "five");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(2, "two");
        ArrayEntry<Integer, String> e3 = new ArrayEntry<>(8, "eight");
        ArrayEntry<Integer, String> e4 = new ArrayEntry<>(1, "one");

        maxHeap.add(e1);
        maxHeap.add(e2);
        maxHeap.add(e3);
        maxHeap.add(e4);

        assertEquals("Should poll maximum (8)", e3, maxHeap.poll());
        assertEquals("Should poll next maximum (5)", e1, maxHeap.poll());
        assertEquals("Should poll next maximum (2)", e2, maxHeap.poll());
        assertEquals("Should poll next maximum (1)", e4, maxHeap.poll());
    }

    @Test
    public void testRemoveExistingElement() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(5, "five");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(2, "two");
        ArrayEntry<Integer, String> e3 = new ArrayEntry<>(8, "eight");

        minHeap.add(e1);
        minHeap.add(e2);
        minHeap.add(e3);

        assertTrue("Remove should return true", minHeap.remove(e1));
        assertEquals("Size should be 2", 2, minHeap.size());
        assertEquals("Min should now be 2", e2, minHeap.peek());
    }

    @Test
    public void testRemoveNonExistingElement() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(5, "five");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(2, "two");

        minHeap.add(e1);

        assertFalse("Remove should return false for non-existing element", minHeap.remove(e2));
        assertEquals("Size should remain 1", 1, minHeap.size());
    }

    @Test
    public void testUpdateElement() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(5, "five");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(2, "two");
        ArrayEntry<Integer, String> e3 = new ArrayEntry<>(8, "eight");

        minHeap.add(e1);
        minHeap.add(e2);
        minHeap.add(e3);

        ArrayEntry<Integer, String> updated = new ArrayEntry<>(1, "updated");
        assertTrue("Update should return true", minHeap.update(e1, updated));
        assertEquals("Min should now be updated element", updated, minHeap.peek());
    }

    @Test
    public void testUpdateNonExistingElement() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(5, "five");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(2, "two");
        ArrayEntry<Integer, String> updated = new ArrayEntry<>(1, "updated");

        minHeap.add(e1);

        assertFalse("Update should return false for non-existing element", minHeap.update(e2, updated));
    }

    @Test
    public void testUpdateWithHigherPriority() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(5, "five");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(2, "two");
        ArrayEntry<Integer, String> e3 = new ArrayEntry<>(8, "eight");

        minHeap.add(e1);
        minHeap.add(e2);
        minHeap.add(e3);

        ArrayEntry<Integer, String> updated = new ArrayEntry<>(10, "updated");
        minHeap.update(e2, updated);

        assertEquals("Min should be 5", e1, minHeap.poll());
        assertEquals("Next should be 8", e3, minHeap.poll());
        assertEquals("Last should be updated (10)", updated, minHeap.poll());
    }

    @Test
    public void testClear() {
        minHeap.add(new ArrayEntry<>(5, "five"));
        minHeap.add(new ArrayEntry<>(2, "two"));
        minHeap.add(new ArrayEntry<>(8, "eight"));

        minHeap.clear();

        assertTrue("Queue should be empty after clear", minHeap.isEmpty());
        assertEquals("Size should be 0", 0, minHeap.size());
        assertNull("Peek should return null", minHeap.peek());
    }

    @Test
    public void testDuplicatePriorities() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(5, "first");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(5, "second");
        ArrayEntry<Integer, String> e3 = new ArrayEntry<>(5, "third");

        minHeap.add(e1);
        minHeap.add(e2);
        minHeap.add(e3);

        assertEquals("Size should be 3", 3, minHeap.size());

        // All have same priority, order doesn't matter but all should be present
        Set<String> values = new HashSet<>();
        values.add(minHeap.poll().getValue());
        values.add(minHeap.poll().getValue());
        values.add(minHeap.poll().getValue());

        assertTrue(values.contains("first"));
        assertTrue(values.contains("second"));
        assertTrue(values.contains("third"));
    }

    // ==================== FUZZ TESTING ====================

    @Test
    public void testFuzzRandomOperations_Small() {
        fuzzTest(100, 42);
    }

    @Test
    public void testFuzzRandomOperations_Medium() {
        fuzzTest(1000, 123);
    }

    @Test
    public void testFuzzRandomOperations_Large() {
        fuzzTest(5000, 456);
    }

    @Test
    public void testFuzzRandomOperations_VeryLarge() {
        fuzzTest(10000, 789);
    }

    @Test
    public void testFuzzOnlyAddsAndPolls() {
        Random rand = new Random(999);
        AdaptablePriorityQueue<Integer> myPQ = new AdaptablePriorityQueue<>();
        PriorityQueue<ArrayEntry<Integer, Integer>> javaPQ = new PriorityQueue<>(
                Comparator.comparingInt(ArrayEntry::getKey)
        );

        for (int i = 0; i < 5000; i++) {
            int priority = rand.nextInt(10000);
            ArrayEntry<Integer, Integer> entry = new ArrayEntry<>(priority, priority);

            myPQ.add(entry);
            javaPQ.add(entry);
        }

        while (!javaPQ.isEmpty()) {
            ArrayEntry<Integer, Integer> expected = javaPQ.poll();
            ArrayEntry<Integer, Integer> actual = myPQ.poll();

            assertNotNull("My PQ should not be empty", actual);
            assertEquals("Priorities should match", expected.getKey(), actual.getKey());
        }

        assertTrue("My PQ should be empty", myPQ.isEmpty());
    }

    @Test
    public void testFuzzWithUpdates() {
        Random rand = new Random(555);
        AdaptablePriorityQueue<Integer> myPQ = new AdaptablePriorityQueue<>();
        List<ArrayEntry<Integer, Integer>> entries = new ArrayList<>();

        // Add initial elements
        for (int i = 0; i < 1000; i++) {
            int priority = rand.nextInt(5000);
            ArrayEntry<Integer, Integer> entry = new ArrayEntry<>(priority, i);
            myPQ.add(entry);
            entries.add(entry);
        }

        // Perform random updates
        for (int i = 0; i < 500; i++) {
            int idx = rand.nextInt(entries.size());
            ArrayEntry<Integer, Integer> oldEntry = entries.get(idx);
            int newPriority = rand.nextInt(5000);
            ArrayEntry<Integer, Integer> newEntry = new ArrayEntry<>(newPriority, oldEntry.getValue());

            myPQ.update(oldEntry, newEntry);
            entries.set(idx, newEntry);
        }

        // Verify heap property by polling all
        Integer lastPriority = null;
        while (!myPQ.isEmpty()) {
            ArrayEntry<Integer, Integer> entry = myPQ.poll();
            if (lastPriority != null) {
                assertTrue("Heap property violated", entry.getKey() >= lastPriority);
            }
            lastPriority = entry.getKey();
        }
    }

    @Test
    public void testFuzzMaxHeap() {
        Random rand = new Random(333);
        AdaptablePriorityQueue<Integer> myMaxPQ = new AdaptablePriorityQueue<>(true);
        PriorityQueue<ArrayEntry<Integer, Integer>> javaMaxPQ = new PriorityQueue<>(
                (a, b) -> b.getKey().compareTo(a.getKey())
        );

        for (int i = 0; i < 2000; i++) {
            int priority = rand.nextInt(10000);
            ArrayEntry<Integer, Integer> entry = new ArrayEntry<>(priority, priority);

            myMaxPQ.add(entry);
            javaMaxPQ.add(entry);
        }

        while (!javaMaxPQ.isEmpty()) {
            ArrayEntry<Integer, Integer> expected = javaMaxPQ.poll();
            ArrayEntry<Integer, Integer> actual = myMaxPQ.poll();

            assertNotNull("My PQ should not be empty", actual);
            assertEquals("Priorities should match", expected.getKey(), actual.getKey());
        }

        assertTrue("My PQ should be empty", myMaxPQ.isEmpty());
    }

    /**
     * Main fuzz testing method that performs random operations and compares
     * results with Java's PriorityQueue.
     */
    private void fuzzTest(int operations, long seed) {
        Random rand = new Random(seed);
        AdaptablePriorityQueue<Integer> myPQ = new AdaptablePriorityQueue<>();
        PriorityQueue<ArrayEntry<Integer, Integer>> javaPQ = new PriorityQueue<>(
                Comparator.comparingInt(ArrayEntry::getKey)
        );

        List<ArrayEntry<Integer, Integer>> trackedEntries = new ArrayList<>();

        for (int i = 0; i < operations; i++) {
            int operation = rand.nextInt(100);

            if (operation < 60 || trackedEntries.isEmpty()) {
                // 60% add operation (or 100% if empty)
                int priority = rand.nextInt(10000);
                ArrayEntry<Integer, Integer> entry = new ArrayEntry<>(priority, i);

                myPQ.add(entry);
                javaPQ.add(entry);
                trackedEntries.add(entry);

            } else if (operation < 85) {
                // 25% poll operation
                ArrayEntry<Integer, Integer> expected = javaPQ.poll();
                ArrayEntry<Integer, Integer> actual = myPQ.poll();

                if (expected == null) {
                    assertNull("Both should be null", actual);
                } else {
                    assertNotNull("My PQ should not return null", actual);
                    assertEquals("Polled priorities should match",
                            expected.getKey(), actual.getKey());
                    trackedEntries.remove(expected);
                }

            } else {
                // 15% remove operation
                int idx = rand.nextInt(trackedEntries.size());
                ArrayEntry<Integer, Integer> toRemove = trackedEntries.get(idx);

                boolean myResult = myPQ.remove(toRemove);
                boolean javaResult = javaPQ.remove(toRemove);

                assertEquals("Remove results should match", javaResult, myResult);

                if (javaResult) {
                    trackedEntries.remove(idx);
                }
            }

            // Periodically verify sizes match
            if (i % 100 == 0) {
                assertEquals("Sizes should match at operation " + i,
                        javaPQ.size(), myPQ.size());
            }
        }

        // Final verification - poll all remaining elements
        while (!javaPQ.isEmpty()) {
            ArrayEntry<Integer, Integer> expected = javaPQ.poll();
            ArrayEntry<Integer, Integer> actual = myPQ.poll();

            assertNotNull("My PQ should have elements remaining", actual);
            assertEquals("Final poll priorities should match",
                    expected.getKey(), actual.getKey());
        }

        assertTrue("My PQ should be empty at end", myPQ.isEmpty());
        assertEquals("Final size should be 0", 0, myPQ.size());
    }

    // ==================== EDGE CASES ====================

    @Test
    public void testLargeNumberOfElements() {
        for (int i = 0; i < 10000; i++) {
            minHeap.add(new ArrayEntry<>(i, "value" + i));
        }

        assertEquals("Size should be 10000", 10000, minHeap.size());

        for (int i = 0; i < 10000; i++) {
            ArrayEntry<Integer, String> entry = minHeap.poll();
            assertEquals("Should poll in order", Integer.valueOf(i), entry.getKey());
        }
    }

    @Test
    public void testRemoveFromDifferentPositions() {
        List<ArrayEntry<Integer, String>> entries = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ArrayEntry<Integer, String> e = new ArrayEntry<>(i, "val" + i);
            entries.add(e);
            minHeap.add(e);
        }

        // Remove from beginning, middle, end
        assertTrue(minHeap.remove(entries.getFirst()));
        assertTrue(minHeap.remove(entries.get(50)));
        assertTrue(minHeap.remove(entries.get(99)));
        entries.remove(99);
        entries.remove(50);
        entries.removeFirst();

        assertEquals("Size should be 97", entries.size(), minHeap.size());
        for (ArrayEntry<Integer, String> integerStringArrayEntry : entries) {
            ArrayEntry<Integer, String> entry = minHeap.poll();
            assertEquals("Should poll in order", integerStringArrayEntry.getKey(), entry.getKey());
        }
    }

    @Test
    public void testAlternatingAddRemove() {
        ArrayEntry<Integer, String> e1 = new ArrayEntry<>(1, "one");
        ArrayEntry<Integer, String> e2 = new ArrayEntry<>(2, "two");

        minHeap.add(e1);
        assertEquals(e1, minHeap.poll());
        minHeap.add(e2);
        assertEquals(e2, minHeap.poll());
        assertTrue(minHeap.isEmpty());
    }

    @Test
    public void testStressTestWithManyUpdates() {
        Random rand = new Random(12345);
        AdaptablePriorityQueue<Integer> minHeap = new AdaptablePriorityQueue<>();
        List<ArrayEntry<Integer, Integer>> entries = new ArrayList<>();

        // Add 1000 elements
        for (int i = 0; i < 1000; i++) {
            ArrayEntry<Integer, Integer> e = new ArrayEntry<>(rand.nextInt(5000), i);
            entries.add(e);
            minHeap.add(e);
        }

        // Perform 5000 random updates
        for (int i = 0; i < 5000; i++) {
            int idx = rand.nextInt(entries.size());
            ArrayEntry<Integer, Integer> old = entries.get(idx);
            ArrayEntry<Integer, Integer> newEntry = new ArrayEntry<>(rand.nextInt(5000), old.getValue());
            minHeap.update(old, newEntry);
            entries.set(idx, newEntry);
        }

        // Verify heap property maintained
        Integer last = null;
        while (!minHeap.isEmpty()) {
            ArrayEntry<Integer, Integer> current = minHeap.poll();
            if (last != null) {
                assertTrue("Heap property violated after updates", current.getKey() >= last);
            }
            last = current.getKey();
        }
    }
}