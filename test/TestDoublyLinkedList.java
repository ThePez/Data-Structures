import org.junit.*;
import static org.junit.Assert.*;
import structures.DoublyLinkedList;

import java.util.LinkedList;
import java.util.Random;

public class TestDoublyLinkedList {

    private DoublyLinkedList<Integer> intList;
    private DoublyLinkedList<String> stringList;
    private long randomSeed;

    @Before
    public void setUp() {
        randomSeed = System.currentTimeMillis();
        intList = new DoublyLinkedList<>();
        stringList = new DoublyLinkedList<>();
    }

    @After
    public void tearDown() {
        intList = null;
        stringList = null;
    }

    // Test constructor and basic properties
    @Test
    public void testConstructor() {
        assertEquals("New list should have size 0", 0, intList.size());
        assertTrue("New list should be empty", intList.isEmpty());
        assertNull("getFirst() on empty list should return null", intList.getFirst());
        assertNull("getLast() on empty list should return null", intList.getLast());
    }

    // Test isEmpty method
    @Test
    public void testIsEmpty() {
        assertTrue("Empty list should return true for isEmpty()", intList.isEmpty());
        intList.append(1);
        assertFalse("Non-empty list should return false for isEmpty()", intList.isEmpty());
        intList.clear();
        assertTrue("Cleared list should return true for isEmpty()", intList.isEmpty());
    }

    // Test append method
    @Test
    public void testAppend() {
        assertTrue("append should return true", intList.append(1));
        assertEquals("Size should be 1 after first append", 1, intList.size());
        assertFalse("List should not be empty after append", intList.isEmpty());

        intList.append(2);
        intList.append(3);
        assertEquals("Size should be 3 after three appends", 3, intList.size());

        // Check order
        assertEquals("First element should be 1", (Integer) 1, intList.get(0));
        assertEquals("Second element should be 2", (Integer) 2, intList.get(1));
        assertEquals("Third element should be 3", (Integer) 3, intList.get(2));
    }

    // Test prepend method
    @Test
    public void testPrepend() {
        assertTrue("prepend should return true", intList.prepend(1));
        assertEquals("Size should be 1 after first prepend", 1, intList.size());

        intList.prepend(2);
        intList.prepend(3);
        assertEquals("Size should be 3 after three prepends", 3, intList.size());

        // Check order (prepend adds to front)
        assertEquals("First element should be 3", (Integer) 3, intList.get(0));
        assertEquals("Second element should be 2", (Integer) 2, intList.get(1));
        assertEquals("Third element should be 1", (Integer) 1, intList.get(2));
    }

    // Test mixed append and prepend
    @Test
    public void testMixedAppendPrepend() {
        intList.append(2);
        intList.prepend(1);
        intList.append(3);
        intList.prepend(0);

        assertEquals("Size should be 4", 4, intList.size());
        assertEquals("Order should be [0, 1, 2, 3]", (Integer) 0, intList.get(0));
        assertEquals("Order should be [0, 1, 2, 3]", (Integer) 1, intList.get(1));
        assertEquals("Order should be [0, 1, 2, 3]", (Integer) 2, intList.get(2));
        assertEquals("Order should be [0, 1, 2, 3]", (Integer) 3, intList.get(3));
    }

    // Test add method
    @Test
    public void testAdd() {
        // Test adding to empty list
        assertTrue("add to index 0 of empty list should succeed", intList.add(0, 10));
        assertEquals("Size should be 1", 1, intList.size());
        assertEquals("Element at index 0 should be 10", (Integer) 10, intList.get(0));

        // Test adding to beginning
        assertTrue("add to index 0 should succeed", intList.add(0, 5));
        assertEquals("Size should be 2", 2, intList.size());
        assertEquals("Element at index 0 should be 5", (Integer) 5, intList.get(0));
        assertEquals("Element at index 1 should be 10", (Integer) 10, intList.get(1));

        // Test adding to end
        assertTrue("add to end should succeed", intList.add(2, 15));
        assertEquals("Size should be 3", 3, intList.size());
        assertEquals("Element at index 2 should be 15", (Integer) 15, intList.get(2));

        // Test adding to middle
        assertTrue("add to middle should succeed", intList.add(1, 7));
        assertEquals("Size should be 4", 4, intList.size());
        assertEquals("Order should be [5, 7, 10, 15]", (Integer) 5, intList.get(0));
        assertEquals("Order should be [5, 7, 10, 15]", (Integer) 7, intList.get(1));
        assertEquals("Order should be [5, 7, 10, 15]", (Integer) 10, intList.get(2));
        assertEquals("Order should be [5, 7, 10, 15]", (Integer) 15, intList.get(3));
    }

    // Test add with invalid indices
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddNegativeIndex() {
        intList.add(-1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexTooLarge() {
        intList.append(1);
        intList.add(2, 10); // Should fail - only valid indices are 0 and 1
    }

    // Test get method
    @Test
    public void testGet() {
        intList.append(10);
        intList.append(20);
        intList.append(30);

        assertEquals("get(0) should return 10", (Integer) 10, intList.get(0));
        assertEquals("get(1) should return 20", (Integer) 20, intList.get(1));
        assertEquals("get(2) should return 30", (Integer) 30, intList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        intList.append(1);
        intList.get(1); // Should fail - only valid index is 0
    }

    // Test set method
    @Test
    public void testSet() {
        intList.append(10);
        intList.append(20);
        intList.append(30);

        Integer oldValue = intList.set(1, 25);
        assertEquals("set should return old value", (Integer) 20, oldValue);
        assertEquals("new value should be set", (Integer) 25, intList.get(1));
        assertEquals("size should remain the same", 3, intList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidIndex() {
        intList.append(1);
        intList.set(1, 10); // Should fail
    }

    // Test remove method
    @Test
    public void testRemove() {
        intList.append(10);
        intList.append(20);
        intList.append(30);
        intList.append(40);

        // Remove middle element
        Integer removed = intList.remove(1);
        assertEquals("remove should return removed element", (Integer) 20, removed);
        assertEquals("size should decrease", 3, intList.size());
        assertEquals("elements should shift", (Integer) 10, intList.get(0));
        assertEquals("elements should shift", (Integer) 30, intList.get(1));
        assertEquals("elements should shift", (Integer) 40, intList.get(2));

        // Remove first element
        removed = intList.remove(0);
        assertEquals("remove should return removed element", (Integer) 10, removed);
        assertEquals("size should decrease", 2, intList.size());

        // Remove last element
        removed = intList.remove(1);
        assertEquals("remove should return removed element", (Integer) 40, removed);
        assertEquals("size should decrease", 1, intList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveInvalidIndex() {
        intList.append(1);
        intList.remove(1); // Should fail
    }

    // Test removeFirst method
    @Test
    public void testRemoveFirst() {
        assertFalse("removeFirst on empty list should return false", intList.remove((Object) 10));

        intList.append(10);
        intList.append(20);
        intList.append(10);
        intList.append(30);

        assertTrue("removeFirst should return true when element exists", intList.remove((Object) 10));
        assertEquals("size should decrease", 3, intList.size());
        assertEquals("first occurrence should be removed", (Integer) 20, intList.get(0));
        assertEquals("other elements should remain", (Integer) 10, intList.get(1));
        assertEquals("other elements should remain", (Integer) 30, intList.get(2));

        assertFalse("removeFirst should return false when element doesn't exist", intList.remove((Object) 99));
        assertEquals("size should remain the same", 3, intList.size());
    }

    // Test getFirst and getLast methods
    @Test
    public void testGetFirstLast() {
        assertNull("getFirst on empty list should return null", intList.getFirst());
        assertNull("getLast on empty list should return null", intList.getLast());

        intList.append(10);
        assertEquals("getFirst should return first element", (Integer) 10, intList.getFirst());
        assertEquals("getLast should return last element", (Integer) 10, intList.getLast());

        intList.append(20);
        intList.prepend(5);
        assertEquals("getFirst should return first element", (Integer) 5, intList.getFirst());
        assertEquals("getLast should return last element", (Integer) 20, intList.getLast());
    }

    // Test clear method
    @Test
    public void testClear() {
        intList.append(1);
        intList.append(2);
        intList.append(3);

        intList.clear();
        assertEquals("size should be 0 after clear", 0, intList.size());
        assertTrue("list should be empty after clear", intList.isEmpty());
        assertNull("getFirst should return null after clear", intList.getFirst());
        assertNull("getLast should return null after clear", intList.getLast());
    }

    // Test with null values
    @Test
    public void testNullValues() {
        assertTrue("should be able to append null", intList.append(null));
        assertEquals("size should be 1", 1, intList.size());
        assertNull("get(0) should return null", intList.get(0));

        Integer oldValue = intList.set(0, 10);
        assertNull("set should return old null value", oldValue);
        assertEquals("new value should be set", (Integer) 10, intList.get(0));
    }

    // Test with String type
    @Test
    public void testStringType() {
        stringList.append("Hello");
        stringList.prepend("World");
        stringList.add(1, "Beautiful");

        assertEquals("size should be 3", 3, stringList.size());
        assertEquals("order should be correct", "World", stringList.get(0));
        assertEquals("order should be correct", "Beautiful", stringList.get(1));
        assertEquals("order should be correct", "Hello", stringList.get(2));

        assertTrue("removeFirst should work with strings", stringList.remove("Beautiful"));
        assertEquals("size should be 2", 2, stringList.size());
    }

    // Test large scale operations
    @Test
    public void testLargeScale() {
        // Add 1000 elements
        for (int i = 0; i < 1000; i++) {
            intList.append(i);
        }

        assertEquals("size should be 1000", 1000, intList.size());
        assertEquals("first element should be 0", (Integer) 0, intList.get(0));
        assertEquals("last element should be 999", (Integer) 999, intList.get(999));
        assertEquals("middle element should be correct", (Integer) 500, intList.get(500));

        // Remove every other element
        for (int i = 999; i >= 0; i -= 2) {
            intList.remove(i);
        }

        assertEquals("size should be 500", 500, intList.size());
    }

    // Test edge cases
    @Test
    public void testEdgeCases() {
        // Single element operations
        intList.append(42);
        assertEquals("single element - get", (Integer) 42, intList.get(0));
        assertEquals("single element - getFirst", (Integer) 42, intList.getFirst());
        assertEquals("single element - getLast", (Integer) 42, intList.getLast()); // Will fail due to bug

        Integer removed = intList.remove(0);
        assertEquals("single element - remove", (Integer) 42, removed);
        assertTrue("should be empty after removing single element", intList.isEmpty());
    }

    // Test bounds checking on empty list
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOnEmptyList() {
        intList.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOnEmptyList() {
        intList.set(0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOnEmptyList() {
        intList.remove(0);
    }

    @Test
    public void testVsLinkedListStress() {
        Random random = new Random(randomSeed);
        DoublyLinkedList<Integer> myList = new DoublyLinkedList<>();
        LinkedList<Integer> javaList = new LinkedList<>();
        for (int step = 0; step < 1_000_000; step++) {
            int op = random.nextInt(7); // choose operation
            int idx, val;

            switch (op) {
                case 0: // append
                    val = random.nextInt(10_000);
                    myList.append(val);
                    javaList.add(val);
                    break;
                case 1: // prepend
                    val = random.nextInt(10_000);
                    myList.prepend(val);
                    javaList.addFirst(val);
                    break;
                case 2: // add at index
                    if (!javaList.isEmpty()) {
                        idx = random.nextInt(javaList.size());
                        val = random.nextInt(10_000);
                        myList.add(idx, val);
                        javaList.add(idx, val);
                    }
                    break;
                case 3: // remove at index
                    if (!javaList.isEmpty()) {
                        idx = random.nextInt(javaList.size());
                        Integer expected = javaList.remove(idx);
                        Integer actual = myList.remove(idx);
                        assertEquals(expected, actual);
                    }
                    break;
                case 4: // set
                    if (!javaList.isEmpty()) {
                        idx = random.nextInt(javaList.size());
                        val = random.nextInt(10_000);
                        Integer expected = javaList.set(idx, val);
                        Integer actual = myList.set(idx, val);
                        assertEquals(expected, actual);
                    }
                    break;
                case 5: // clear occasionally
                    if (random.nextDouble() < 0.001) {
                        myList.clear();
                        javaList.clear();
                    }
                    break;
                case 6: // removeFirst
                    val = random.nextInt(10_000);
                    boolean expected = javaList.remove((Integer) val);
                    boolean actual = myList.remove((Object) val);
                    assertEquals(expected, actual);
                    break;
            }

            assertEquals("Size mismatch at step " + step, javaList.size(), myList.size());
            if (step % 1_000 == 0) {
                for (int i = 0; i < javaList.size(); i++) {
                    assertEquals("Mismatch at step " + step + " index " + i,
                            javaList.get(i), myList.get(i));
                }
            }

            if (step % 100_000 == 0) {
                System.out.println("Stress test progress: " + step);
            }
        }

        for (int i = 0; i < javaList.size(); i++) {
            assertEquals("Mismatch at end, index " + i,
                    javaList.get(i), myList.get(i));
        }

        System.out.println("Stress test OK, size=" + javaList.size());
    }
}