import interfaces.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import structures.HashMap;
import containers.Entry;


import java.util.*;

import static org.junit.Assert.*;

public class TestHashMap {

    private HashMap<Integer, String> map;
    private java.util.HashMap<Integer, String> javaMap;
    private final Random random = new Random(System.currentTimeMillis());

    @Before
    public void setup() {
        map = new HashMap<>();
        javaMap = new java.util.HashMap<>();
    }

    @After
    public void tearDown() {
        map = null;
        javaMap = null;
    }

    @Test
    public void testEmptyMap() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get(10));
    }

    @Test
    public void testBasicInsertion() {
        map.put(50, "A");
        map.put(20, "B");
        map.put(80, "C");
        map.put(10, "D");
        map.put(30, "E");
        map.put(70, "F");
        map.put(90, "G");
        map.put(25, "H");

        assertEquals(8, map.size());
        assertEquals("A", map.get(50));
        assertEquals("B", map.get(20));
        assertEquals("C", map.get(80));
        assertEquals("D", map.get(10));
        assertEquals("E", map.get(30));
        assertEquals("F", map.get(70));
        assertEquals("G", map.get(90));
        assertEquals("H", map.get(25));
    }

    @Test
    public void testInsertAndGet() {
        map.put(5, "five");
        map.put(2, "two");
        map.put(8, "eight");

        assertEquals(3, map.size());
        assertEquals("five", map.get(5));
        assertEquals("two", map.get(2));
        assertEquals("eight", map.get(8));
        assertNull(map.get(100));
    }

    @Test
    public void testUpdateValueOnDuplicateKey() {
        map.put(5, "five");
        assertEquals("five", map.get(5));

        String oldValue = map.put(5, "FIVE");
        assertEquals("five", oldValue);
        assertEquals("FIVE", map.get(5));
        assertEquals(1, map.size());
    }

    @Test
    public void testRemoveSingleElement() {
        map.put(5, "five");
        assertEquals(1, map.size());
        assertEquals("five", map.get(5));

        assertEquals("five", map.remove(5));
        assertNull(map.get(5));
        assertEquals(0, map.size());
    }

    @Test
    public void testRemoveMultipleElements() {
        map.put(5, "five");
        map.put(2, "two");
        map.put(6, "six");
        assertEquals(3, map.size());

        assertEquals("two", map.remove(2));
        assertEquals(2, map.size());
        assertNull(map.get(2));

        assertEquals("six", map.remove(6));
        assertEquals(1, map.size());
        assertNull(map.get(6));

        assertEquals("five", map.remove(5));
        assertEquals(0, map.size());
        assertNull(map.get(5));
    }

    @Test
    public void testRemoveNonExistentKeyReturnsNull() {
        map.put(5, "five");
        assertNull(map.remove(100));
        assertEquals(1, map.size());
    }

    @Test
    public void testPutNullKey() {
        String result = map.put(null, "value");
        assertNull(result);
        assertEquals(0, map.size());
        assertNull(map.get(null));
    }

    @Test
    public void testRemoveNullKey() {
        String result = map.remove(null);
        assertNull(result);
    }

    @Test
    public void testGetNullKey() {
        String result = map.get(null);
        assertNull(result);
    }

    @Test
    public void testClearFunctionality() {
        map.put(50, "A");
        map.put(20, "B");
        map.put(80, "C");
        assertEquals(3, map.size());
        assertFalse(map.isEmpty());

        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get(50));
        assertNull(map.get(20));
        assertNull(map.get(80));
    }

    @Test
    public void testContainsKey() {
        map.put(10, "ten");
        map.put(20, "twenty");
        map.put(30, "thirty");

        assertTrue(map.containsKey(10));
        assertTrue(map.containsKey(20));
        assertTrue(map.containsKey(30));
        assertFalse(map.containsKey(40));
        assertFalse(map.containsKey(100));
    }

    @Test
    public void testContainsKeyEmptyMap() {
        assertFalse(map.containsKey(10));
    }

    @Test
    public void testContainsKeyAfterRemoval() {
        map.put(10, "ten");
        assertTrue(map.containsKey(10));

        map.remove(10);
        assertFalse(map.containsKey(10));
    }

    @Test
    public void testContainsKeyNullKey() {
        assertFalse(map.containsKey(null));
    }

    @Test
    public void testGetKeys() {
        map.put(30, "thirty");
        map.put(10, "ten");
        map.put(50, "fifty");
        map.put(20, "twenty");
        map.put(40, "forty");

        List<Integer> keys = map.getKeys();
        assertEquals(5, keys.size());

        assertTrue(keys.contains(10));
        assertTrue(keys.contains(20));
        assertTrue(keys.contains(30));
        assertTrue(keys.contains(40));
        assertTrue(keys.contains(50));
    }

    @Test
    public void testGetKeysEmptyMap() {
        List<Integer> keys = map.getKeys();
        assertEquals(0, keys.size());
    }

    @Test
    public void testGetValues() {
        map.put(30, "thirty");
        map.put(10, "ten");
        map.put(50, "fifty");
        map.put(20, "twenty");
        map.put(40, "forty");

        List<String> values = map.getValues();
        assertEquals(5, values.size());

        // Values should be present (order doesn't matter in unordered map)
        assertTrue(values.contains("ten"));
        assertTrue(values.contains("twenty"));
        assertTrue(values.contains("thirty"));
        assertTrue(values.contains("forty"));
        assertTrue(values.contains("fifty"));
    }

    @Test
    public void testGetValuesEmptyMap() {
        List<String> values = map.getValues();
        assertEquals(0, values.size());
    }

    @Test
    public void testGetPairs() {
        map.put(10, "ten");
        map.put(20, "twenty");
        map.put(30, "thirty");

        List<Entry<Integer, String>> pairs = map.getEntries();
        assertEquals(3, pairs.size());

        // Verify pairs contain correct key-value combinations
        Set<Integer> keys = new HashSet<>();
        Set<String> values = new HashSet<>();
        for (int i = 0; i < pairs.size(); i++) {
            Entry<Integer, String> pair = pairs.get(i);
            keys.add(pair.getKey());
            values.add(pair.getValue());
        }

        assertTrue(keys.contains(10));
        assertTrue(keys.contains(20));
        assertTrue(keys.contains(30));
        assertTrue(values.contains("ten"));
        assertTrue(values.contains("twenty"));
        assertTrue(values.contains("thirty"));
    }

    @Test
    public void testGetPairsEmptyMap() {
        List<Entry<Integer, String>> pairs = map.getEntries();
        assertEquals(0, pairs.size());
    }

    @Test
    public void testConstructorWithCapacity() {
        HashMap<Integer, String> customMap = new HashMap<>(100);
        assertEquals(0, customMap.size());
        assertTrue(customMap.isEmpty());

        customMap.put(1, "one");
        assertEquals(1, customMap.size());
        assertEquals("one", customMap.get(1));
    }

    @Test
    public void testResizeTrigger() {
        // Start with small capacity
        HashMap<Integer, String> smallMap = new HashMap<>(11);

        // Insert enough elements to trigger resize (load factor 0.66)
        // 11 * 0.66 = 7.26, so inserting 8 should trigger resize
        for (int i = 0; i < 20; i++) {
            smallMap.put(i, "V" + i);
        }

        assertEquals(20, smallMap.size());
        // Verify all elements are still accessible after resize
        for (int i = 0; i < 20; i++) {
            assertEquals("V" + i, smallMap.get(i));
        }
    }

    @Test
    public void testHashCollisions() {
        // Even with collisions, map should work correctly
        for (int i = 0; i < 1000; i++) {
            map.put(i, "V" + i);
        }

        assertEquals(1000, map.size());

        // Verify all elements are retrievable
        for (int i = 0; i < 1000; i++) {
            assertEquals("V" + i, map.get(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValues() {
        map.put(10, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullValue() {
        map.put(10, "ten");
        map.put(10, null);
    }

    @Test
    public void testRandomInserts() {
        int n = 1_000_000;
        String expected;
        String actual;

        for (int i = 0; i < n; i++) {
            int key = random.nextInt(10000);
            String value = "V" + random.nextInt(10000);
            expected = javaMap.put(key, value);
            actual = map.put(key, value);
            assertEquals(expected, actual);
        }

        assertEquals(javaMap.size(), map.size());

        // Verify all keys
        for (Integer key : javaMap.keySet()) {
            expected = javaMap.get(key);
            actual = map.get(key);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testRandomInsertsWithDuplicates() {
        int n = 1_000_000;
        int keyRange = 100; // Small range to force many duplicates
        String expected;
        String actual;

        for (int i = 0; i < n; i++) {
            int key = random.nextInt(keyRange);
            String value = "V" + random.nextInt(10000);
            expected = javaMap.put(key, value);
            actual = map.put(key, value);
            assertEquals(expected, actual);

            expected = javaMap.get(key);
            actual = map.get(key);
            assertEquals(expected, actual);

            assertEquals(javaMap.size(), map.size());
        }

        assertEquals(javaMap.size(), map.size());

        // Verify all final values match
        for (Integer key : javaMap.keySet()) {
            expected = javaMap.get(key);
            actual = map.get(key);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testSequentialInserts() {
        int n = 1_000_000;
        String expected;
        String actual;

        // Insert in ascending order
        for (int i = 0; i < n; i++) {
            expected = javaMap.put(i, "V" + i);
            actual = map.put(i, "V" + i);
            assertEquals(expected, actual);
        }

        assertEquals(javaMap.size(), map.size());

        for (int i = 0; i < n; i++) {
            expected = javaMap.get(i);
            actual = map.get(i);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testAlternatingOperations() {
        int operations = 1_000_000;
        String expected;
        String actual;

        for (int i = 0; i < operations; i++) {
            boolean insert = random.nextBoolean() || javaMap.isEmpty();

            if (insert) {
                int key = random.nextInt(10000);
                String value = "V" + random.nextInt(10000);
                expected = javaMap.put(key, value);
                actual = map.put(key, value);
                assertEquals(expected, actual);
            } else {
                // Pick a random key to remove
                java.util.List<Integer> keys = new java.util.ArrayList<>(javaMap.keySet());
                int key = keys.get(random.nextInt(keys.size()));

                expected = javaMap.remove(key);
                actual = map.remove(key);
                assertEquals(expected, actual);
            }

            assertEquals(javaMap.size(), map.size());
        }

        // Verify all remaining keys
        for (Integer key : javaMap.keySet()) {
            expected = javaMap.get(key);
            actual = map.get(key);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testRandomContainsKey() {
        int n = 1_000_000;
        String expected;
        String actual;

        for (int i = 0; i < n; i++) {
            int key = random.nextInt(50000);
            String value = "V" + random.nextInt(50000);
            expected = javaMap.put(key, value);
            actual = map.put(key, value);
            assertEquals(expected, actual);
        }

        // Verification
        for (Integer key : javaMap.keySet()) {
            if (random.nextBoolean()) {
                key *= -1;
            }

            // Verify containsKey() matches (some will be true and some false based on the randomness)
            boolean expect = javaMap.containsKey(key);
            boolean act =  map.containsKey(key);
            assertEquals(expect, act);
        }
    }

    @Test
    public void testNegativeKeys() {
        int n = 1_000_000;
        String expected;
        String actual;

        for (int i = 0; i < n; i++) {
            int key = random.nextInt(20000) - 10000; // Range: -10000 to 9999
            String value = "V" + random.nextInt(20000);

            expected = javaMap.put(key, value);
            actual = map.put(key, value);
            assertEquals(expected, actual);
        }

        assertEquals(javaMap.size(), map.size());

        for (Integer key : javaMap.keySet()) {
            expected = javaMap.get(key);
            actual = map.get(key);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testStressful() {
        int n = 5_000_000;
        String expected;
        String actual;

        // Insert all elements
        for (int i = 0; i < n; i++) {
            int key = random.nextInt(n);
            int op =  random.nextInt(10);
            if (op < 4) {
                String value = "V" + random.nextInt(n);
                expected = javaMap.put(key, value);
                actual = map.put(key, value);
                assertEquals(expected, actual);
            } else if (op < 6){
                expected = javaMap.get(key);
                actual = map.get(key);
                assertEquals(expected, actual);
            } else if (op < 8){
                expected = javaMap.remove(key);
                actual = map.remove(key);
                assertEquals(expected, actual);
            } else {
                javaMap.clear();
                map.clear();
                assertEquals(javaMap.size(), map.size());
            }
        }

        assertEquals(javaMap.size(), map.size());

        // Verify all elements
        for (Integer key : javaMap.keySet()) {
            expected = javaMap.get(key);
            actual = map.get(key);
            assertEquals(expected, actual);
            assertTrue(map.containsKey(key));
        }
    }

    @Test
    public void testGetKeysAndValuesLists() {
        int n = 1_000_000;
        String expected;
        String actual;
        for (int i = 0; i < n; i++) {
            int key = random.nextInt(5000);
            String value = "V" + random.nextInt(5000);
            expected = javaMap.put(key, value);
            actual = map.put(key, value);
            assertEquals(expected, actual);
        }

        // Verify all keys are present
        List<Integer> keysList = map.getKeys();
        Set<Integer> mapKeys = new HashSet<>();
        for (int i = 0; i < keysList.size(); i++) {
            mapKeys.add(keysList.get(i));
        }
        Set<Integer> javaKeys = javaMap.keySet();
        assertEquals(javaKeys, mapKeys);

        // Verify all values are present
        List<String> mapValues = map.getValues();
        Collection<String> javaValues = javaMap.values();
        for (String value : javaValues) {
            boolean found = false;
            for (int i = 0; i < mapValues.size(); i++) {
                if (mapValues.get(i).equals(value)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testConsistencyAfterMixedOperations() {
        for (int i = 0; i < 1_000_000; i++) {
            int key = random.nextInt(1_000);
            String value = "V" + key;
            String expected;
            String actual;
            if (random.nextInt(10) <= 6) {
                // Insert 70%
                expected = javaMap.put(key, value);
                actual = map.put(key, value);
                assertEquals(expected, actual);
            } else {
                // Delete 30%
                expected = javaMap.remove(key);
                actual = map.remove(key);
                assertEquals(expected, actual);
            }

            // Verify size matches
            int expectedSize = javaMap.size();
            int actualSize = map.size();
            assertEquals(expectedSize, actualSize);

            // Random spot checks
            for (int j = 0; j < 20; j++) {
                int checkKey = random.nextInt(500);
                expected = javaMap.get(checkKey);
                actual = map.get(checkKey);
                assertEquals(expected, actual);
            }

            if (i % 100_000 == 0) {
                System.out.println("Loop " + i + " ok");
            }
        }

        // Check the complete map matches the java map
        assertEquals(javaMap.size(), map.size());

        Set<Integer> javaKeys = javaMap.keySet();
        List<Integer> keysList = map.getKeys();
        Set<Integer> mapKeys = new HashSet<>();
        for (int i = 0; i < keysList.size(); i++) {
            mapKeys.add(keysList.get(i));
        }
        assertEquals(javaKeys, mapKeys);
    }

    @Test
    public void testSequentialInsertAndDelete() {
        int n = 1_000_000;
        String expected;
        String actual;

        // Insert 1..n
        for (int i = 1; i <= n; i++) {
            expected = javaMap.put(i, "V" + i);
            actual = map.put(i, "V" + i);
            assertEquals(expected, actual);
        }

        // Verify all insertions
        System.out.println("Inserted " + n + " values");
        assertEquals(javaMap.size(), map.size());

        // Verify searches
        for (int i = 1; i <= n; i++) {
            expected = javaMap.get(i);
            actual = map.get(i);
            assertEquals(expected, actual);
        }

        System.out.println("Searched " + n + " values");

        // Delete 1..n
        for (int i = 1; i <= n; i++) {
            expected = javaMap.remove(i);
            actual = map.remove(i);
            assertEquals(expected, actual);

            assertEquals(javaMap.size(), map.size());
            assertNull(map.get(i));
        }

        System.out.println("Deleted " + n + " values");
        assertTrue(map.isEmpty());
        assertEquals(javaMap.size(), map.size());
    }

    @Test
    public void testStringKeys() {
        HashMap<String, Integer> stringMap = new HashMap<>();
        java.util.HashMap<String, Integer> javaStringMap = new java.util.HashMap<>();

        String[] keys = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape"};

        for (int i = 0; i < keys.length; i++) {
            assertEquals(javaStringMap.put(keys[i], i), stringMap.put(keys[i], i));
        }

        assertEquals(javaStringMap.size(), stringMap.size());

        for (String key : keys) {
            assertEquals(javaStringMap.get(key), stringMap.get(key));
        }

        // Test removal
        for (String key : keys) {
            assertEquals(javaStringMap.remove(key), stringMap.remove(key));
        }

        assertTrue(stringMap.isEmpty());
    }
}