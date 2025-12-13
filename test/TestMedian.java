
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import structures.Median;

import static org.junit.Assert.*;

public class TestMedian {

    private Median<Integer> tracker;

    @Before
    public void setUp() {
        tracker = new Median<>();
    }

    @After
    public void tearDown() {
        tracker = null;
    }

    @Test
    public void testIsEmptyInitially() {
        assertTrue(tracker.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetMedianOnEmptyThrows() {
        tracker.getMedian();
    }

    @Test
    public void testAddSingleElement() {
        tracker.add(10);
        assertFalse(tracker.isEmpty());
        assertEquals(10.0, tracker.getMedian(), 0.0001);
    }

    @Test
    public void testMedianOddNumberOfElements() {
        tracker.add(10);
        tracker.add(5);
        tracker.add(15);
        assertEquals(10.0, tracker.getMedian(), 0.0001);
    }

    @Test
    public void testMedianEvenNumberOfElements() {
        tracker.add(10);
        tracker.add(20);
        assertEquals(15.0, tracker.getMedian(), 0.0001);

        tracker.add(30);
        tracker.add(40);
        assertEquals(25.0, tracker.getMedian(), 0.0001);
    }

    @Test
    public void testOrderDoesNotMatter() {
        tracker.add(40);
        tracker.add(10);
        tracker.add(30);
        tracker.add(20);
        assertEquals(25.0, tracker.getMedian(), 0.0001);
    }

    @Test
    public void testNegativeNumbers() {
        tracker.add(-5);
        tracker.add(-15);
        tracker.add(-10);
        assertEquals(-10.0, tracker.getMedian(), 0.0001);
    }

    @Test
    public void testLargeInputSequence() {
        for (int i = 1; i <= 1000; i++) {
            tracker.add(i);
        }
        // 1000 elements -> median = (500 + 501) / 2 = 500.5
        assertEquals(500.5, tracker.getMedian(), 0.0001);
    }
}

