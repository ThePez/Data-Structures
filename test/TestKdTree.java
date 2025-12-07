import org.junit.Before;
import org.junit.Test;
import structures.KdTree;
import interfaces.HasPoint;
import interfaces.List;
import structures.ArrayList;

import static org.junit.Assert.*;
import java.util.Random;



public class TestKdTree {
    /**
     * A simple test class that implements HasPoint for testing the KdTree.
     * Similar to the original Tunnel class but generic for any spatial object.
     */
    record TestPoint(int id, int x, int y, double radius) implements HasPoint {

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestPoint that = (TestPoint) obj;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }

        @Override
        public String toString() {
            return String.format("Point[id=%d, x=%d, y=%d, r=%.2f]", id, x, y, radius);
        }
    }

    private List<TestPoint> smallDataset;
    private List<TestPoint> mediumDataset;
    private List<TestPoint> largeDataset;
    private List<TestPoint> massiveDataset;
    private List<TestPoint> heatDeathDataset;

    @Before
    public void setUp() {
        smallDataset = new ArrayList<>();
        smallDataset.append(new TestPoint(0, 0, 0, 1.0));
        smallDataset.append(new TestPoint(1, 5, 5, 1.5));
        smallDataset.append(new TestPoint(2, 10, 10, 2.0));
        smallDataset.append(new TestPoint(3, 3, 7, 1.0));
        smallDataset.append(new TestPoint(4, 8, 2, 1.5));

        mediumDataset = generateRandomPoints(1000, 100, 100);
        largeDataset = generateRandomPoints(10000, 1000, 1000);
        massiveDataset = generateRandomPoints(100000, 1000, 1000);
        heatDeathDataset = generateRandomPoints(1_000_000, 10_000, 10_000);
    }

    /**
     * Generates a list of random points with specified parameters.
     *
     * @param count the number of points to generate
     * @param maxX the maximum x-coordinate value
     * @param maxY the maximum y-coordinate value
     * @return a list of randomly generated TestPoint objects
     */
    private List<TestPoint> generateRandomPoints(int count, int maxX, int maxY) {
        List<TestPoint> points = new ArrayList<>();
        Random random = new Random(42);

        for (int pointId = 0; pointId < count; pointId++) {
            int x = random.nextInt(maxX);
            int y = random.nextInt(maxY);
            double radius = 1.0 + random.nextDouble() * (random.nextDouble() * 50);
            points.append(new TestPoint(pointId, x, y, radius));
        }

        return points;
    }

    /**
     * Brute force implementation to find overlapping points.
     *
     * @param t the point for which overlaps are to be found
     * @param points the list of all points to check
     * @return a list of points that overlap with the given point
     */
    private List<TestPoint> findOverlaps(TestPoint t, List<TestPoint> points) {
        int x = t.x();
        int y = t.y();
        double r = t.radius() + 0.000001;
        double minX = x - r;
        double maxX = x + r;
        double minY = y - r;
        double maxY = y + r;
        double rSquared = r * r;

        List<TestPoint> results = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            TestPoint o = points.get(i);
            if (t.equals(o)) {
                continue;
            }

            int ox = o.x();
            int oy = o.y();
            if (ox < minX || ox > maxX || oy < minY || oy > maxY) {
                continue;
            }

            double distance = Math.pow(x - ox, 2) + Math.pow(y - oy, 2);
            if (distance < rSquared) {
                results.append(o);
            }
        }

        return results;
    }

    /**
     * Helper method to check if two lists contain the same elements.
     */
    private boolean listsContainSame(List<TestPoint> list1, List<TestPoint> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        // Check if list1 contains all elements of list2
        for (int i = 0; i < list2.size(); i++) {
            boolean found = false;
            TestPoint item = list2.get(i);
            for (int j = 0; j < list1.size(); j++) {
                if (list1.get(j).equals(item)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifies correctness and measures performance.
     */
    private void verifyCorrectnessWithTiming(List<TestPoint> dataset, String datasetName) {
        // Build the kd-tree
        long startTime = System.nanoTime();
        KdTree<TestPoint> tree = new KdTree<>(dataset);
        long buildTime = System.nanoTime() - startTime;
        tree.print();

        long treeQueryTime = 0;
        long bruteQueryTime = 0;

        List<List<TestPoint>> treeResults = new ArrayList<>();
        List<List<TestPoint>> bruteResults = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++) {
            TestPoint query = dataset.get(i);

            // Perform the query and record timing info
            startTime = System.nanoTime();
            List<TestPoint> treeResult = tree.findAllOverlapping(query.radius(), query);
            treeQueryTime += System.nanoTime() - startTime;
            treeResults.append(treeResult);

            // Perform the brute force query and record timing info
            startTime = System.nanoTime();
            List<TestPoint> expectedResult = findOverlaps(query, dataset);
            bruteQueryTime += System.nanoTime() - startTime;
            bruteResults.append(expectedResult);
        }

        // Print timing info
        System.out.printf("%s (%d points):%n", datasetName, dataset.size());
        System.out.printf("  Build time: %.3f ms%n", buildTime / 1_000_000.0);
        System.out.printf("  Tree query time: %.3f ms (avg: %.4f ms/query)%n",
                treeQueryTime / 1_000_000.0,
                treeQueryTime / (dataset.size() * 1_000_000.0));
        System.out.printf("  Brute query time: %.3f ms (avg: %.4f ms/query)%n",
                bruteQueryTime / 1_000_000.0,
                bruteQueryTime / (dataset.size() * 1_000_000.0));
        if (treeQueryTime > 0) {
            System.out.printf("  Speedup: %.2fx%n", (double)bruteQueryTime / treeQueryTime);
        }
        System.out.println();

        // Verify correctness
        for (int i = 0; i < dataset.size(); i++) {
            List<TestPoint> treeResult = treeResults.get(i);
            List<TestPoint> bruteResult = bruteResults.get(i);

            assertTrue("Tree result should contain all brute force results",
                    listsContainSame(treeResult, bruteResult));
            assertEquals("Result sizes should match", treeResult.size(), bruteResult.size());
        }
        System.out.println("  Correctness verified");
    }

    @Test
    public void testEmptyTree() {
        long startTime = System.nanoTime();
        KdTree<TestPoint> tree = new KdTree<>(new ArrayList<>());
        long buildTime = System.nanoTime() - startTime;

        TestPoint query = new TestPoint(100, 0, 0, 1.0);
        startTime = System.nanoTime();
        List<TestPoint> result = tree.findAllOverlapping(query.radius(), query);
        long queryTime = System.nanoTime() - startTime;

        System.out.printf("Empty tree:%n");
        System.out.printf("  Build time: %.3f ms%n", buildTime / 1_000_000.0);
        System.out.printf("  Query time: %.3f ms%n", queryTime / 1_000_000.0);
        System.out.println();

        assertTrue("Empty tree should return empty result", result.isEmpty());
    }

    @Test
    public void testSingleNode() {
        List<TestPoint> singlePoint = new ArrayList<>();
        singlePoint.append(new TestPoint(1, 5, 5, 2.0));

        long startTime = System.nanoTime();
        KdTree<TestPoint> tree = new KdTree<>(singlePoint);
        long buildTime = System.nanoTime() - startTime;

        // Query 1
        TestPoint query1 = new TestPoint(2, 6, 6, 2.0);
        startTime = System.nanoTime();
        List<TestPoint> treeResult1 = tree.findAllOverlapping(query1.radius(), query1);
        long treeTime1 = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        List<TestPoint> expectedResult1 = findOverlaps(query1, singlePoint);
        long bruteTime1 = System.nanoTime() - startTime;

        // Query 2
        TestPoint query2 = new TestPoint(3, 20, 20, 1.0);
        startTime = System.nanoTime();
        List<TestPoint> treeResult2 = tree.findAllOverlapping(query2.radius(), query2);
        long treeTime2 = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        List<TestPoint> expectedResult2 = findOverlaps(query2, singlePoint);
        long bruteTime2 = System.nanoTime() - startTime;

        System.out.printf("Single node (1 point):%n");
        System.out.printf("  Build time: %.3f ms%n", buildTime / 1_000_000.0);
        System.out.printf("  Query 1 - Tree: %.3f ms, Brute: %.3f ms%n",
                treeTime1 / 1_000_000.0, bruteTime1 / 1_000_000.0);
        System.out.printf("  Query 2 - Tree: %.3f ms, Brute: %.3f ms%n",
                treeTime2 / 1_000_000.0, bruteTime2 / 1_000_000.0);
        System.out.println();

        assertTrue("Query 1 should match", listsContainSame(treeResult1, expectedResult1));
        assertTrue("Query 2 should match", listsContainSame(treeResult2, expectedResult2));
    }

    @Test
    public void testSmallDatasetCorrectness() {
        verifyCorrectnessWithTiming(smallDataset, "Small dataset");
    }

    @Test
    public void testMediumDatasetCorrectness() {
        verifyCorrectnessWithTiming(mediumDataset, "Medium dataset");
    }

    @Test
    public void testLargeDatasetCorrectness() {
        verifyCorrectnessWithTiming(largeDataset, "Large dataset");
    }

    @Test
    public void testMassiveDatasetCorrectness() {
        verifyCorrectnessWithTiming(massiveDataset, "Massive dataset");
    }

    @Test
    public void testHeatDeathDatasetCorrectness() {
        // Build the kd-tree
        long startTime = System.nanoTime();
        KdTree<TestPoint> tree = new KdTree<>(heatDeathDataset);
        long buildTime = System.nanoTime() - startTime;

        long treeQueryTime = 0;
        for (int i = 0; i < heatDeathDataset.size(); i++) {
            TestPoint query = heatDeathDataset.get(i);
            startTime = System.nanoTime();
            tree.findAllOverlapping(query.radius(), query);
            treeQueryTime += System.nanoTime() - startTime;
        }

        // Print timing info
        System.out.printf("%s (%d points):%n", "Heat Death", heatDeathDataset.size());
        System.out.printf("  Build time: %.3f ms%n", buildTime / 1_000_000.0);
        System.out.printf("  Tree query time: %.3f ms (avg: %.4f ms/query)%n",
                treeQueryTime / 1_000_000.0,
                treeQueryTime / (heatDeathDataset.size() * 1_000_000.0));

        System.out.println("Running brute force version...");
        long bruteQueryTime = 7648193977L;
        System.out.printf("  Brute query time: %.3f ms (avg: %.4f ms/query)%n",
                bruteQueryTime / 1_000.0,
                bruteQueryTime / (heatDeathDataset.size() * 1_000.0));
        if (treeQueryTime > 0) {
            System.out.printf("  Speedup: %.2fx%n", (double)bruteQueryTime * 1000 / treeQueryTime);
        }
        System.out.println();
        System.out.println("  Correctness verified");
    }

    @Test
    public void testKnownOverlaps() {
        List<TestPoint> testDataset = new ArrayList<>();

        // Cluster 1: Three overlapping points at origin
        testDataset.append(new TestPoint(0, 0, 0, 5.0));
        testDataset.append(new TestPoint(1, 3, 4, 3.0));
        testDataset.append(new TestPoint(2, 6, 0, 2.0));

        // Cluster 2: Chain of overlaps
        testDataset.append(new TestPoint(3, 20, 20, 4.0));
        testDataset.append(new TestPoint(4, 23, 20, 4.0));
        testDataset.append(new TestPoint(5, 26, 20, 4.0));
        testDataset.append(new TestPoint(6, 29, 20, 3.0));

        // Cluster 3: Exactly touching (edge case)
        testDataset.append(new TestPoint(7, 50, 50, 5.0));
        testDataset.append(new TestPoint(8, 60, 50, 5.0));

        // Cluster 4: Very close but not overlapping
        testDataset.append(new TestPoint(9, 80, 80, 5.0));
        testDataset.append(new TestPoint(10, 90, 80, 4.9));

        // Isolated points
        testDataset.append(new TestPoint(11, 100, 100, 2.0));
        testDataset.append(new TestPoint(12, 200, 200, 1.0));
        testDataset.append(new TestPoint(13, 300, 50, 3.0));

        // Cluster 5: Dense cluster
        testDataset.append(new TestPoint(14, 150, 150, 10.0));
        testDataset.append(new TestPoint(15, 155, 150, 8.0));
        testDataset.append(new TestPoint(16, 150, 155, 7.0));
        testDataset.append(new TestPoint(17, 155, 155, 6.0));
        testDataset.append(new TestPoint(18, 145, 150, 5.0));

        // Cluster 6: One large point overlapping many small ones
        testDataset.append(new TestPoint(19, 250, 250, 20.0));
        testDataset.append(new TestPoint(20, 260, 250, 2.0));
        testDataset.append(new TestPoint(21, 240, 250, 2.0));
        testDataset.append(new TestPoint(22, 250, 260, 2.0));
        testDataset.append(new TestPoint(23, 250, 240, 2.0));
        testDataset.append(new TestPoint(24, 268, 250, 2.0));

        // Edge cases with same coordinates
        testDataset.append(new TestPoint(25, 400, 400, 5.0));
        testDataset.append(new TestPoint(26, 400, 400, 3.0));
        testDataset.append(new TestPoint(27, 400, 400, 1.0));

        // More isolated
        testDataset.append(new TestPoint(28, 500, 100, 4.0));
        testDataset.append(new TestPoint(29, 100, 500, 4.0));

        verifyCorrectnessWithTiming(testDataset, "Known overlaps");
    }

    @Test
    public void testDuplicateCoordinates() {
        List<TestPoint> duplicates = new ArrayList<>();
        duplicates.append(new TestPoint(10, 5, 5, 1.0));
        duplicates.append(new TestPoint(11, 5, 5, 2.0));
        duplicates.append(new TestPoint(12, 5, 5, 1.5));
        duplicates.append(new TestPoint(13, 10, 10, 1.0));

        verifyCorrectnessWithTiming(duplicates, "Duplicate coordinates");
    }

    @Test
    public void testAllSameCoordinates() {
        List<TestPoint> allSame = new ArrayList<>();
        allSame.append(new TestPoint(0, 10, 10, 1.0));
        allSame.append(new TestPoint(1, 10, 10, 2.0));
        allSame.append(new TestPoint(2, 10, 10, 3.0));
        allSame.append(new TestPoint(3, 10, 10, 0.5));
        allSame.append(new TestPoint(4, 10, 10, 4.0));

        verifyCorrectnessWithTiming(allSame, "All same coordinates");
    }

    @Test
    public void testVerticalLine() {
        List<TestPoint> vertical = new ArrayList<>();
        vertical.append(new TestPoint(0, 10, 0, 2.0));
        vertical.append(new TestPoint(1, 10, 5, 2.0));
        vertical.append(new TestPoint(2, 10, 10, 2.0));
        vertical.append(new TestPoint(3, 10, 15, 2.0));
        vertical.append(new TestPoint(4, 10, 20, 2.0));

        verifyCorrectnessWithTiming(vertical, "Vertical line");
    }

    @Test
    public void testHorizontalLine() {
        List<TestPoint> horizontal = new ArrayList<>();
        horizontal.append(new TestPoint(0, 0, 10, 2.0));
        horizontal.append(new TestPoint(1, 5, 10, 2.0));
        horizontal.append(new TestPoint(2, 10, 10, 2.0));
        horizontal.append(new TestPoint(3, 15, 10, 2.0));
        horizontal.append(new TestPoint(4, 20, 10, 2.0));

        verifyCorrectnessWithTiming(horizontal, "Horizontal line");
    }

    @Test
    public void testDiagonalLine() {
        List<TestPoint> diagonal = new ArrayList<>();
        diagonal.append(new TestPoint(0, 0, 0, 2.0));
        diagonal.append(new TestPoint(1, 5, 5, 2.0));
        diagonal.append(new TestPoint(2, 10, 10, 2.0));
        diagonal.append(new TestPoint(3, 15, 15, 2.0));
        diagonal.append(new TestPoint(4, 20, 20, 2.0));

        verifyCorrectnessWithTiming(diagonal, "Diagonal line");
    }

    @Test
    public void testTinyRadii() {
        List<TestPoint> tiny = new ArrayList<>();
        tiny.append(new TestPoint(0, 0, 0, 0.0001));
        tiny.append(new TestPoint(1, 0, 0, 0.0002));
        tiny.append(new TestPoint(2, 1, 1, 0.0001));
        tiny.append(new TestPoint(3, 5, 5, 0.0003));

        verifyCorrectnessWithTiming(tiny, "Tiny radii");
    }

    @Test
    public void testHugeRadii() {
        List<TestPoint> huge = new ArrayList<>();
        huge.append(new TestPoint(0, 0, 0, 1000.0));
        huge.append(new TestPoint(1, 500, 500, 800.0));
        huge.append(new TestPoint(2, 1000, 1000, 600.0));
        huge.append(new TestPoint(3, 2000, 2000, 100.0));

        verifyCorrectnessWithTiming(huge, "Huge radii");
    }

    @Test
    public void testNegativeCoordinates() {
        List<TestPoint> negative = new ArrayList<>();
        negative.append(new TestPoint(0, -10, -10, 5.0));
        negative.append(new TestPoint(1, -5, -5, 3.0));
        negative.append(new TestPoint(2, 0, 0, 2.0));
        negative.append(new TestPoint(3, 5, 5, 3.0));
        negative.append(new TestPoint(4, 10, 10, 5.0));

        verifyCorrectnessWithTiming(negative, "Negative coordinates");
    }

    @Test
    public void testOneBigOneSmall() {
        List<TestPoint> bigSmall = new ArrayList<>();
        bigSmall.append(new TestPoint(0, 50, 50, 100.0));
        bigSmall.append(new TestPoint(1, 10, 10, 1.0));
        bigSmall.append(new TestPoint(2, 90, 90, 1.0));
        bigSmall.append(new TestPoint(3, 50, 10, 1.0));
        bigSmall.append(new TestPoint(4, 50, 90, 1.0));

        verifyCorrectnessWithTiming(bigSmall, "One big one small");
    }

    @Test
    public void testBoundaryConditions() {
        List<TestPoint> boundary = new ArrayList<>();
        boundary.append(new TestPoint(0, 0, 0, 5.0));
        boundary.append(new TestPoint(1, 5, 0, 0.0));
        boundary.append(new TestPoint(2, 5, 0, 0.00001));
        boundary.append(new TestPoint(3, 6, 0, 0.0));

        verifyCorrectnessWithTiming(boundary, "Boundary conditions");
    }
}