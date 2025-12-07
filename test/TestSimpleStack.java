
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import structures.SimpleStack;

import java.util.*;

import static org.junit.Assert.*;

/**
 * JUnit 4 test suite for CustomStack class
 */
public class TestSimpleStack {

    private SimpleStack<Integer> customStack;
    private Deque<Integer> javaStack;
    private long randomSeed;

    @Before
    public void setUp() {
        randomSeed = System.currentTimeMillis();
        customStack = new SimpleStack<>();
        javaStack = new ArrayDeque<>();
    }

    @After
    public void tearDown() {
        customStack = null;
        javaStack = null;
    }

    @Test
    public void testConstructor() {
        assertTrue("New stack should be empty", customStack.isEmpty());
        assertEquals("New stack size should be 0", 0, customStack.size());
    }

    @Test
    public void testPushAndPeek() {
        customStack.push(10);
        customStack.push(20);

        assertEquals("Peek should return top element", Integer.valueOf(20), customStack.peek());
        assertEquals("Stack size should be 2", 2, customStack.size());
    }

    @Test
    public void testPop() {
        customStack.push(5);
        customStack.push(15);

        assertEquals("Pop should return last pushed element", Integer.valueOf(15), customStack.pop());
        assertEquals("Next pop should return 5", Integer.valueOf(5), customStack.pop());
        assertTrue("Stack should be empty after popping all elements", customStack.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPopOnEmptyStackThrows() {
        customStack.pop();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPeekOnEmptyStackThrows() {
        customStack.peek();
    }

    @Test
    public void testClear() {
        customStack.push(1);
        customStack.push(2);
        customStack.push(3);

        assertEquals("Stack should have size 3", 3, customStack.size());

        customStack.clear();

        assertTrue("Stack should be empty after clear", customStack.isEmpty());
        assertEquals("Stack size should be 0 after clear", 0, customStack.size());
    }

    @Test
    public void testExtendCapacity() {
        // Fill beyond initial capacity (10) to trigger resize
        for (int i = 0; i < 20; i++) {
            customStack.push(i);
        }
        assertEquals("Stack size should be 20 after pushing 20 elements", 20, customStack.size());
        assertEquals("Top element should be 19", Integer.valueOf(19), customStack.peek());
    }

    @Test
    public void testVsJavaDeque() {
        Random random = new Random(randomSeed);
        for (int step = 0; step < 10_000_000; step++) {
            int op = random.nextInt(4);
            int val;

            switch (op) {
                case 0: // push
                    val = random.nextInt(1000);
                    customStack.push(val);
                    javaStack.push(val);
                    break;

                case 1: // pop
                    if (!javaStack.isEmpty()) {
                        Integer expected = javaStack.pop();
                        Integer actual = customStack.pop();
                        assertEquals("Pop mismatch at step " + step, expected, actual);
                    }

                    break;

                case 2: // peek
                    if (!javaStack.isEmpty()) {
                        assertEquals("Peek mismatch at step " + step, javaStack.peek(), customStack.peek());
                    } else {
                        try {
                            customStack.peek();
                            fail("Peek on empty stack should throw");
                        } catch (IndexOutOfBoundsException expected) { /* ok */ }
                    }

                    break;

                case 3: // clear
                    customStack.clear();
                    javaStack.clear();
                    break;
            }

            assertEquals("Size mismatch at step " + step, javaStack.size(), customStack.size());
            if (step % 1_000_000 == 0) {
                System.out.println("Loop " + step + " ok");
            }
        }
    }

    @Test
    public void testMinMaxVsCollections() {
        Random random = new Random(randomSeed);

        for (int step = 0; step < 10_000_000; step++) {
            if (random.nextBoolean()) {
                int val = random.nextInt(10_000);
                customStack.push(val);
                javaStack.push(val);
            } else {
                if (!javaStack.isEmpty()) {
                    javaStack.pop();
                    customStack.pop();
                }
            }

            assertEquals("Size mismatch at step " + step, javaStack.size(), customStack.size());
            if (step % 1_000_000 == 0) {
                System.out.println("Loop " + step + " ok");
                System.out.println("Stack size: " + customStack.size());
                javaStack.clear();
                customStack.clear();
            }
        }
    }
}
