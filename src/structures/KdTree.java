package structures;

import interfaces.HasPoint;
import interfaces.List;
import containers.Entry;
import java.util.Comparator;

/**
 * Represents a k-d tree data structure. A k-d tree is a space-partitioning data structure
 * that organises points in a k-dimensional space. The tree alternates splitting on different
 * dimensions (axes) at consecutive levels.
 *
 * @param <T> the type of elements stored in the k-d tree, which must implement the {@code HasPoint} interface
 */
public class KdTree<T extends HasPoint> {

    /**
     * Represents a node in a k-d tree data structure.
     * A k-d tree is a binary tree used for organizing points in a k-dimensional space.
     * Each node in the tree stores a point and splits the space along one of the k dimensions.
     *
     * @param <T> the type of the value stored in the node, which must implement the {@code HasPoint} interface
     * @param left the left child of this node; may be null if no left child exists
     * @param right the right child of this node; may be null if no right child exists
     * @param value the data point stored in this node
     * @param depth the depth of this node in the k-d tree, where the root node has a depth of 0
     * @param axis the dimension (or axis) used for partitioning at this node
     */
    public record KdNode<T extends HasPoint>(KdNode<T> left, KdNode<T> right, T value, int depth,
                                             int axis){}

    /**
     * Represents metadata used in visualising the structure of a k-d tree, capturing the details
     * necessary for formatting and layout.
     *
     * @param prefix The string prefix used to format branches in the visualised tree structure.
     * @param isTail Boolean flag indicating if the current branch represents a tail.
     * @param branch The content of the current branch in the visualised tree.
     */
    private record PrintData(String prefix, boolean isTail, String branch) {}

    /**
     * The root node of the k-d tree.
     */
    private final KdNode<T> root;

    /**
     * Constructs a k-d tree from the given list of points. The tree is built by recursively
     * partitioning the points based on their x and y coordinates, alternating the splitting axis
     * at each level of the tree, starting with the x-axis.
     *
     * @param points the list of point elements used to construct the k-d tree
     */
    public KdTree(List<T> points) {
        List<T> byX = new ArrayList<>(points);
        List<T> byY = new ArrayList<>(points);

        // Pre-Sort the List of tunnels by both x and y
        byX.sort(Comparator.comparingInt(HasPoint::x));
        byY.sort(Comparator.comparingInt(HasPoint::y));

        // Build the tree
        root = build(byX, byY, 0);
    }

    /**
     * Finds all elements in the k-d tree that overlap with the reference element within the specified radius.
     * The method performs a spatial search to collect all elements whose center points lie within the given
     * radius from the reference element, without considering the reference element itself.
     *
     * @param radius the radius within which overlapping elements are to be searched
     * @param ref the reference element from which the search radius is centered
     * @return a list of all elements in the k-d tree that overlap with the reference element within the specified radius
     */
    public List<T> findAllOverlapping(double radius, T ref) {
        List<T> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        // First get bounding box for the tunnel
        radius += 0.000001;
        int x = ref.x();
        int y = ref.y();
        double radiusSquared = Math.pow(radius, 2);
        double boxMinX = x - radius;
        double boxMaxX = x + radius;
        double boxMinY = y - radius;
        double boxMaxY = y + radius;

        SimpleQueue<KdNode<T>> queue = new SimpleQueue<>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            KdNode<T> current = queue.dequeue();
            if (current == null) {
                // Reached the end of that branch
                continue;
            }

            // Get the centre point for the tunnel stored in the node
            T v = current.value;
            int queryX = v.x();
            int queryY = v.y();

            // Make sure the objects are different instances
            if (!ref.equals(v)) {
                // Check if the node's tunnel is in the bounding box
                if (queryX <= boxMaxX && queryX >= boxMinX
                        && queryY <= boxMaxY && queryY >= boxMinY) {
                    // Proper distance check
                    double distance = Math.pow(x - queryX, 2) + Math.pow(y - queryY, 2);
                    // a^2 + b^2 => c^2 < (r + epsilon) ^2 -> no need for sqrt()
                    if (distance < radiusSquared) {
                        // Tunnel's radius is greater than the distance between centres
                        result.append(v);
                    }
                }
            }

            // Check the branches now
            if (current.axis() == 0) {
                // This level splits on the X axis
                searchBranches(current, queue, queryX, boxMinX, boxMaxX);
            } else {
                // This level splits on the Y axis
                searchBranches(current, queue, queryY, boxMinY, boxMaxY);
            }
        }

        return result;
    }

    /**
     * Searches and queues the child branches of a k-d tree node based on the query point
     * and the bounding box constraints. This method determines whether the left, right,
     * or both children branches of the node are valid search candidates and adds them to
     * the provided queue accordingly.
     *
     * @param node      the current k-d tree node being evaluated
     * @param queue     the queue used to store the child nodes that need to be further explored
     * @param queryPoint the query point used to evaluate which branches to search
     * @param boxMin    the minimum boundary of the current bounding box being checked
     * @param boxMax    the maximum boundary of the current bounding box being checked
     */
    private void searchBranches(KdNode<T> node, SimpleQueue<KdNode<T>> queue, int queryPoint,
                                double boxMin, double boxMax) {
        if (boxMax < queryPoint) {
            // The bounding box is in the left branch
            queue.enqueue(node.left);
        } else if (boxMin > queryPoint) {
            // The bounding box is in the right branch
            queue.enqueue(node.right);
        } else {
            // The bounding box is in both branches
            queue.enqueue(node.left);
            queue.enqueue(node.right);
        }
    }

    /**
     * Recursively builds a k-d tree from two sorted lists of Tunnel objects, one sorted by
     * x-coordinate and the other sorted by y-coordinate.
     *
     * @param x the list of Tunnel objects sorted by x-coordinate
     * @param y the list of Tunnel objects sorted by y-coordinate
     * @param depth the current depth in the tree, used to determine the splitting axis
     * @return the root node of the k-d tree or null if the input lists are empty
     */
    private KdNode<T> build(List<T> x, List<T> y, int depth) {
        // Recursive base case
        if (x.isEmpty() || y.isEmpty()) {
            return null;
        }

        if (x.size() != y.size()) {
            throw new IllegalArgumentException("x and y lists must be the same size");
        }

        // Determine the splitting axis
        int axis = depth % 2; // 0 == X, 1 == Y

        // Partition lists for each axis
        List<T> lx = new ArrayList<>();
        List<T> rx = new ArrayList<>();
        List<T> ly = new ArrayList<>();
        List<T> ry = new ArrayList<>();
        T median;
        // The partition helper ensures the correct splitting of the lists
        if (axis == 0) {
            median = partition(x, y, lx, rx, ly, ry, true);
        } else {
            median = partition(y, x, ly, ry, lx, rx, false);
        }

        // Increase depth for children
        depth++;
        // Build children before the parent
        KdNode<T> leftChild = build(lx, ly, depth);
        KdNode<T> rightChild = build(rx, ry, depth);
        // Decrease depth for parent
        depth--;
        // Build the parent of these two children
        return new KdNode<>(leftChild, rightChild, median, depth, axis);
    }

    /**
     * Partitions the given lists into left and right subsets based on a median element from the split list.
     * The partitioning is performed around the median element, which is determined from the split list.
     * Elements are divided into their respective subsets based on the specified axis (x or y).
     *
     * @param split the primary list of elements to be partitioned around its median
     * @param other the secondary list of elements to be partitioned based on the median of the split list
     * @param leftSplit the list to store elements from the split list that are less than or equal to the median
     * @param rightSplit the list to store elements from the split list that are greater than the median
     * @param leftOther the list to store elements from the other list that belong on the left based on the median
     * @param rightOther the list to store elements from the other list that belong on the right based on the median
     * @param byX flag indicating whether to partition based on the x-coordinate (true) or y-coordinate (false)
     * @return the median element from the split list
     */
    private T partition(List<T> split, List<T> other, List<T> leftSplit, List<T> rightSplit,
                        List<T> leftOther, List<T> rightOther, boolean byX) {
        int mid = split.size() / 2;
        // Partition the currently selected split axis around the median
        T midPoint = split.get(mid);
        for (int i = 0; i < split.size(); i++) {
            if (i == mid) {
                continue;
            }

            partitionHelper(split, leftSplit, rightSplit, byX, midPoint, i);

        }

        for (int i = 0; i < other.size(); i++) {
            if (other.get(i).equals(midPoint)) {
                continue;
            }

            partitionHelper(other, leftOther, rightOther, byX, midPoint, i);
        }

        return midPoint;
    }

    /**
     * Helper method to partition elements from the given list into left and right subsets
     * based on a midpoint and a specified axis (x or y). The method evaluates each element
     * in the list and appends it to the respective left or right subset based on its coordinate
     * relative to the midpoint.
     *
     * @param split the list of elements to be partitioned into left and right subsets
     * @param leftSplit the list to store elements from the split list that are less than or equal
     *                  to the midpoint on the specified axis
     * @param rightSplit the list to store elements from the split list that are greater than
     *                   the midpoint on the specified axis
     * @param byX flag indicating whether to partition based on the x-coordinate (true)
     *            or y-coordinate (false)
     * @param midPoint the element representing the midpoint used for partitioning the split list
     * @param i the index of the current element in the split list being evaluated
     */
    private void partitionHelper(List<T> split, List<T> leftSplit, List<T> rightSplit, boolean byX,
                                 T midPoint, int i) {
        if (byX) {
            if (split.get(i).x() <= midPoint.x()) {
                leftSplit.append(split.get(i));
            } else {
                rightSplit.append(split.get(i));
            }
        } else {
            if (split.get(i).y() <= midPoint.y()) {
                leftSplit.append(split.get(i));
            } else {
                rightSplit.append(split.get(i));
            }
        }
    }

    /**
     * Prints a visual representation of the k-d tree. The tree structure is displayed
     * in a hierarchical format, starting from the root node and showing all child nodes
     * recursively. Each tree branch is prefixed based on its depth and position (left or right),
     * and nodes include details such as their depth and the splitting axis.
     */
    public void print() {
        if (root == null) {
            return;
        }

        // Print root specifically
        T base = root.value;
        System.out.printf("└─ Root [depth=%d, X-split] %s\n", root.depth, base.toString());

        // Now print the rest of the tree
        SimpleStack<Entry<KdNode<T>, PrintData>> stack = new SimpleStack<>();
        stack.push(new Entry<>(root.right, new PrintData("   ", true, "R")));
        stack.push(new Entry<>(root.left, new PrintData("   ", false, "L")));

        while (!stack.isEmpty()) {
            Entry<KdNode<T>, PrintData> entry = stack.pop();
            KdNode<T> node = entry.getKey();
            PrintData pd = entry.getValue();
            String connector = pd.isTail ? "└─ " : "├─ ";
            if (node == null) {
                System.out.println(pd.prefix + connector + pd.branch + " ∅");
                continue;
            }

            T t = node.value;
            String nodeAxis = node.axis == 0 ? "X" : "Y";
            System.out.printf("%s%s%s [depth=%d, %s-split] %s\n", pd.prefix, connector,
                    pd.branch, node.depth, nodeAxis, t.toString());

            // For children, extend the prefix
            String prefix = pd.prefix + (pd.isTail ? "   " : "│  ");
            if (node.left != null || node.right != null) {
                stack.push(new Entry<>(node.right, new PrintData(prefix, true, "R")));
                stack.push(new Entry<>(node.left, new PrintData(prefix, false, "L")));
            }
        }
    }

}
