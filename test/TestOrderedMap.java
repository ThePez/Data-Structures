import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import structures.*;
import interfaces.*;

import java.util.Random;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class TestOrderedMap {

    public void checkAgainstJavaString(List<String> java, List<String> tree) {
        for (int i = 0; i < java.size(); i++) {
            assertEquals(java.get(i), tree.get(i));
        }
    }

    public void checkAgainstJavaInt(List<Integer> java, List<Integer> tree) {
        for (int i = 0; i < java.size(); i++) {
            assertEquals(java.get(i), tree.get(i));
        }
    }

    private OrderedMap<Integer, String> tree;
    private TreeMap<Integer, String> javaTree;
    private final Random random = new Random(42);

    @Before
    public void setup() {
        tree = new OrderedMap<>();
        javaTree = new TreeMap<>();
    }

    @After
    public void tearDown() {
        tree = null;
        javaTree = null;
    }

    @Test
    public void testEmptyTree() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertNull(tree.get(10));
    }

    @Test
    public void testBasicInsertion() {
        tree.put(50, "A");
        tree.put(20, "B");
        tree.put(80, "C");
        tree.put(10, "D");
        tree.put(30, "E");
        tree.put(70, "F");
        tree.put(90, "G");
        tree.put(25, "H");

        assertEquals(8, tree.size());
        assertEquals("A", tree.get(50));
        assertEquals("B", tree.get(20));
        assertEquals("C", tree.get(80));
        assertEquals("D", tree.get(10));
        assertEquals("E", tree.get(30));
        assertEquals("F", tree.get(70));
        assertEquals("G", tree.get(90));
        assertEquals("H", tree.get(25));
    }

    @Test
    public void testInsertAndGet() {
        tree.put(5, "five");
        tree.put(2, "two");
        tree.put(8, "eight");

        assertEquals(3, tree.size());
        assertEquals("five", tree.get(5));
        assertEquals("two", tree.get(2));
        assertEquals("eight", tree.get(8));
        assertNull(tree.get(100));
    }

    @Test
    public void testUpdateValue() {
        tree.put(5, "five");
        assertEquals("five", tree.get(5));

        String oldValue = tree.put(5, "FIVE");
        assertEquals("five", oldValue);
        assertEquals("FIVE", tree.get(5));
        assertEquals(1, tree.size());
    }

    @Test
    public void testGetAndPutUpdate() {
        tree.put(30, "original");
        assertEquals("original", tree.get(30));
        assertNull(tree.get(100));

        String oldValue = tree.put(30, "New E");
        assertEquals("original", oldValue);
        assertEquals("New E", tree.get(30));
        assertEquals(1, tree.size());
    }

    @Test
    public void testRemoveRootNode() {
        tree.put(5, "five");
        assertEquals(1, tree.size());
        assertEquals("five", tree.get(5));

        assertEquals("five", tree.remove(5));
        assertNull(tree.get(5));
        assertEquals(0, tree.size());
    }

    @Test
    public void testRemoveLeafNode() {
        tree.put(5, "five");
        tree.put(2, "two");
        tree.put(6, "six");
        assertEquals(3, tree.size());

        assertEquals("two", tree.remove(2));
        assertEquals(2, tree.size());
        assertNull(tree.get(2));

        assertEquals("six", tree.remove(6));
        assertEquals(1, tree.size());
        assertNull(tree.get(6));
    }

    @Test
    public void testRemoveNodeWithOneChild() {
        tree.put(5, "five");
        tree.put(4, "four");
        tree.put(6, "six");
        tree.put(7, "seven");
        tree.put(3, "three");
        assertEquals(5, tree.size());

        assertEquals("four", tree.remove(4));
        assertEquals(4, tree.size());
        assertNull(tree.get(4));

        assertEquals("six", tree.remove(6));
        assertEquals(3, tree.size());
        assertNull(tree.get(6));

        assertEquals("three", tree.get(3));
        assertEquals("seven", tree.get(7));
        assertEquals("five", tree.get(5));
    }

    @Test
    public void testRemoveNodeWithTwoChildren() {
        tree.put(5, "five");
        tree.put(2, "two");
        tree.put(8, "eight");
        tree.put(1, "one");
        tree.put(3, "three");
        assertEquals(5, tree.size());

        assertEquals("two", tree.remove(2));
        assertEquals(4, tree.size());
        assertNull(tree.get(2));
        assertEquals("five", tree.remove(5));
        assertEquals(3, tree.size());
        assertNull(tree.get(5));
    }

    @Test
    public void testRemoveNonExistentKeyReturnsNull() {
        tree.put(5, "five");
        assertNull(tree.remove(100));
        assertEquals(1, tree.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyThrows() {
        tree.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKeyThrows() {
        tree.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullKeyThrows() {
        tree.get(null);
    }

    @Test
    public void testLeftLeftRotation() {
        tree.put(30, "30");
        assertEquals(1, tree.getHeight());
        tree.put(20, "20");
        assertEquals(2, tree.getHeight());
        tree.put(10, "10"); // forces LL rotation
        assertEquals(2, tree.getHeight());
        assertEquals(3, tree.size());
    }

    @Test
    public void testRightRightRotation() {
        tree.put(10, "10");
        assertEquals(1, tree.getHeight());
        tree.put(20, "20");
        assertEquals(2, tree.getHeight());
        tree.put(30, "30"); // forces RR rotation
        assertEquals(2, tree.getHeight());
        assertEquals(3, tree.size());
    }

    @Test
    public void testLeftRightRotation() {
        tree.put(30, "30");
        assertEquals(1, tree.getHeight());
        tree.put(10, "10");
        assertEquals(2, tree.getHeight());
        tree.put(20, "20"); // forces LR rotation
        assertEquals(2, tree.getHeight());
        assertEquals(3, tree.size());
    }

    @Test
    public void testRightLeftRotation() {
        tree.put(10, "10");
        assertEquals(1, tree.getHeight());
        tree.put(30, "30");
        assertEquals(2, tree.getHeight());
        tree.put(20, "20"); // forces RL rotation
        assertEquals(2, tree.getHeight());
        assertEquals(3, tree.size());
    }

    @Test
    public void testClearFunctionality() {
        tree.put(50, "A");
        tree.put(20, "B");
        tree.put(80, "C");
        assertEquals(3, tree.size());
        assertFalse(tree.isEmpty());

        tree.clear();
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
        assertNull(tree.get(50));
        assertNull(tree.get(20));
        assertNull(tree.get(80));
    }

    @Test
    public void testRandomInsertionsAndSearches() {
        for (int i = 0; i < 1_000_000; i++) {
            int key = random.nextInt(500);
            String value = "V" + key;

            // Insert into both
            assertEquals(javaTree.put(key, value), tree.put(key, value));
            // Check both
            assertEquals(javaTree.get(key), tree.get(key));
            // Verify size
            assertEquals(javaTree.size(), tree.size());
        }

        // Check the complete tree matches the java tree
        assertEquals(javaTree.size(), tree.size());
        checkAgainstJavaInt(new ArrayList<>(javaTree.keySet().toArray(Integer[]::new)), tree.getKeys());
        checkAgainstJavaString(new ArrayList<>(javaTree.values().toArray(String[]::new)), tree.getValues());
    }

    @Test
    public void testRandomInsertionsAndDeletions() {
        // Insert some initial values
        for (int i = 0; i < 1_000_000; i++) {
            int key = random.nextInt(1000);
            assertEquals(javaTree.put(key, "V" + key), tree.put(key, "V" + key));
        }

        // Random deletions
        for (int i = 0; i < 500_00; i++) {
            int key = random.nextInt(1000);

            assertEquals(javaTree.remove(key), tree.remove(key));
            assertEquals(javaTree.size(), tree.size());

            // Spot check searches
            for (int j = 0; j < 50; j++) {
                int checkKey = random.nextInt(1000);
                assertEquals(javaTree.get(checkKey), tree.get(checkKey));
            }
        }

        // Check the complete tree matches the java tree
        assertEquals(javaTree.size(), tree.size());
        checkAgainstJavaInt(new ArrayList<>(javaTree.keySet().toArray(Integer[]::new)), tree.getKeys());
        checkAgainstJavaString(new ArrayList<>(javaTree.values().toArray(String[]::new)), tree.getValues());
    }

    @Test
    public void testConsistencyAfterMixedOperations() {
        for (int i = 0; i < 1_000_000; i++) {
            int key = random.nextInt(1_000);
            String value = "V" + key;

            // Bound [0, 9]
            if (random.nextInt(10) <= 6) {
                // Insert 70%
                assertEquals(javaTree.put(key, value), tree.put(key, value));
            } else {
                // Delete 30%
                assertEquals(javaTree.remove(key), tree.remove(key));
            }

            // Verify size matches
            assertEquals(javaTree.size(), tree.size());

            // Random spot checks
            for (int j = 0; j < 20; j++) {
                int checkKey = random.nextInt(500);
                assertEquals(javaTree.get(checkKey), tree.get(checkKey));
            }

            if (i % 100_000 == 0) {
                System.out.println("Loop " + i + " ok");
            }
        }

        // Check the complete tree matches the java tree
        assertEquals(javaTree.size(), tree.size());
        checkAgainstJavaInt(new ArrayList<>(javaTree.keySet().toArray(Integer[]::new)), tree.getKeys());
        checkAgainstJavaString(new ArrayList<>(javaTree.values().toArray(String[]::new)), tree.getValues());
    }

    @Test
    public void testKeysInRangeLarge() {
        int n = 1_000_000;

        // Insert 1..n
        for (int i = 0; i < n; i++) {
            int key = random.nextInt(n);
            tree.put(key, "V");
        }

        tree.keysInRange(random.nextInt(1000), random.nextInt(n));
    }

    @Test
    public void testOrderedInsertAndDelete() {
        int n = 1_000_000;

        // Insert 1..n
        for (int i = 1; i <= n; i++) {
            assertEquals(javaTree.put(i, "V" + i), tree.put(i, "V" + i));
        }

        // Check the complete tree matches the java tree
        System.out.println("Inserted " + n + " values");
        // Check the complete tree matches the java tree
        assertEquals(javaTree.size(), tree.size());
        checkAgainstJavaInt(new ArrayList<>(javaTree.keySet().toArray(Integer[]::new)), tree.getKeys());
        checkAgainstJavaString(new ArrayList<>(javaTree.values().toArray(String[]::new)), tree.getValues());

        // Verify searches
        for (int i = 1; i <= n; i++) {
            assertEquals(javaTree.get(i), tree.get(i));
        }

        System.out.println("Searched " + n + " values");

        // Delete 1..n
        for (int i = 1; i <= n; i++) {
            assertEquals(javaTree.remove(i), tree.remove(i));
            assertEquals(javaTree.size(), tree.size());
            assertNull(tree.get(i));
        }

        System.out.println("Deleted " + n + " values");
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertEquals(javaTree.size(), tree.size());
        assertEquals(javaTree.isEmpty(), tree.isEmpty());
    }

    @Test
    public void testNextGeqFunctionality() {
        tree.put(10, "ten");
        tree.put(20, "twenty");
        tree.put(30, "thirty");
        tree.put(40, "forty");
        javaTree.put(10, "ten");
        javaTree.put(20, "twenty");
        javaTree.put(30, "thirty");
        javaTree.put(40, "forty");

        // Test exact matches
        assertEquals("ten", tree.nextGeq(10));
        assertEquals("twenty", tree.nextGeq(20));
        assertEquals("thirty", tree.nextGeq(30));
        assertEquals("forty", tree.nextGeq(40));

        // Test greater than values
        assertEquals("twenty", tree.nextGeq(15));
        assertEquals("thirty", tree.nextGeq(25));
        assertEquals("forty", tree.nextGeq(35));

        assertEquals(javaTree.higherEntry(15).getValue(), tree.nextGeq(15));
        assertEquals(javaTree.higherEntry(25).getValue(), tree.nextGeq(25));
        assertEquals(javaTree.higherEntry(35).getValue(), tree.nextGeq(35));


        // Test values less than a minimum
        assertEquals("ten", tree.nextGeq(5));

        // Test values greater than a maximum
        assertNull(tree.nextGeq(50));

        // Test the empty tree
        tree.clear();
        assertNull(tree.nextGeq(10));
    }

    @Test
    public void testNextLeqFunctionality() {
        tree.put(10, "ten");
        tree.put(20, "twenty");
        tree.put(30, "thirty");
        tree.put(40, "forty");
        javaTree.put(10, "ten");
        javaTree.put(20, "twenty");
        javaTree.put(30, "thirty");
        javaTree.put(40, "forty");


        // Test exact matches
        assertEquals("ten", tree.nextLeq(10));
        assertEquals("twenty", tree.nextLeq(20));
        assertEquals("thirty", tree.nextLeq(30));
        assertEquals("forty", tree.nextLeq(40));

        // Test less than values
        assertEquals("ten", tree.nextLeq(15));
        assertEquals(javaTree.lowerEntry(15).getValue(), tree.nextLeq(15));
        assertEquals("twenty", tree.nextLeq(25));
        assertEquals(javaTree.lowerEntry(25).getValue(), tree.nextLeq(25));
        assertEquals("thirty", tree.nextLeq(35));
        assertEquals(javaTree.lowerEntry(35).getValue(), tree.nextLeq(35));

        // Test values greater than a maximum
        assertEquals("forty", tree.nextLeq(50));
        assertEquals(javaTree.lowerEntry(50).getValue(), tree.nextLeq(50));

        // Test values less than a minimum
        assertNull(tree.nextLeq(5));

        // Test the empty tree
        tree.clear();
        assertNull(tree.nextLeq(10));
    }

    @Test
    public void testKeysInRange() {
        tree.put(10, "ten");
        tree.put(20, "twenty");
        tree.put(30, "thirty");
        tree.put(40, "forty");
        tree.put(50, "fifty");

        // Test range that includes all keys
        List<Integer> allKeys = tree.keysInRange(10, 50);
        assertEquals(new Integer[]{10, 20, 30, 40, 50}, allKeys.toArray());

        // Test partial range
        List<Integer> partialKeys = tree.keysInRange(20, 40);
        assertEquals(new Integer[]{20, 30, 40}, partialKeys.toArray());

        // Test single element range
        List<Integer> singleKey = tree.keysInRange(30, 30);
        assertEquals(new Integer[]{30}, singleKey.toArray());

        // Test range with no matching keys
        List<Integer> noKeys = tree.keysInRange(25, 25);
        assertEquals(0, noKeys.size());

        // Test range outside bounds
        List<Integer> outsideKeys = tree.keysInRange(60, 70);
        assertEquals(0, outsideKeys.size());

        // Test the empty tree
        tree.clear();
        List<Integer> emptyKeys = tree.keysInRange(10, 50);
        assertEquals(0, emptyKeys.size());
    }

    @Test
    public void testGetKeysAndValues() {
        tree.put(30, "thirty");
        tree.put(10, "ten");
        tree.put(50, "fifty");
        tree.put(20, "twenty");
        tree.put(40, "forty");

        List<Integer> keys = tree.getKeys();
        List<String> values = tree.getValues();

        // Should be in sorted order
        assertEquals(new Integer[]{10, 20, 30, 40, 50}, keys.toArray());
        assertEquals(new String[]{"ten", "twenty", "thirty", "forty", "fifty"}, values.toArray());

        // Test empty tree
        tree.clear();
        assertEquals(0, tree.getKeys().size());
        assertEquals(0, tree.getValues().size());
    }
}