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
     * Finds the nearest node in the k-d tree to the specified query point.
     * This method performs a nearest-neighbour search starting from the root of the tree
     * and recursively traverses the structure to locate the closest node.
     *
     * @param query the query point for which the nearest node is to be found
     * @return the nearest node to the query point, or {@code null} if the tree is empty
     */
    public T nearestNode(T query) {
        if (root == null) {
            return null;
        }

        return nearest(root, query, root.value, Double.POSITIVE_INFINITY);

    }

    /**
     * Recursively finds the nearest neighbour to a query point within a k-d tree, starting from a given node.
     * This method performs a depth-first search, comparing distances to the query point and updating the
     * best candidate if a closer point is found. It also checks the opposite child node if necessary,
     * based on the splitting plane's distance to the query point.
     *
     * @param node the current k-d tree node being examined; may be null if there are no more nodes to process
     * @param query the query point for which the nearest neighbour is being searched
     * @param candidate the current best candidate for the nearest neighbour, initially assumed to be the closest point
     * @param bestDist the squared distance from the query point to the current best candidate
     * @return the data point that is the nearest neighbour to the query point in the k-d tree
     */
    private T nearest(KdNode<T> node, T query, T candidate, double bestDist) {
        if (node == null) {
            return candidate;
        }

        double distance = distance(node.value, query);
        if (distance < bestDist) {
            bestDist = distance;
            candidate = node.value;
        }

        // Which side to check?
        int coOrd = node.axis == 0 ? query.x() :  query.y();
        int splitCoOrd = node.axis == 0 ? node.value.x() : node.value.y();

        KdNode<T> nearSide = coOrd <= splitCoOrd ? node.left : node.right;
        KdNode<T> farSide = coOrd <= splitCoOrd ? node.right : node.left;

        candidate = nearest(nearSide, query, candidate, bestDist);
        bestDist = distance(query, candidate);

        // Check if the split is closer than the best candidate
        double planeDist = Math.abs(coOrd - splitCoOrd);
        if (planeDist < bestDist) {
            candidate = nearest(farSide, query, candidate, bestDist);
        }

        return candidate;
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
                    double distance = distance(current.value, ref);
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
     * Recursively builds a k-d tree from the given lists of elements, partitioning the space
     * along alternating axes (x or y) at each level of the tree. The method creates a balanced
     * binary tree structure by choosing the median element at every recursion level as the root
     * for that subtree.
     *
     * @param x the list of elements sorted by their x-coordinate
     * @param y the list of elements sorted by their y-coordinate
     * @param depth the current depth of the tree, used to determine the splitting axis
     * @return the root node of the k-d tree or subtree created from the given elements, or null if
     *         the input lists are empty
     * @throws IllegalArgumentException if the sizes of the x and y lists are not equal
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
        List<T> sorted = (axis == 0) ? x : y;
        int mid = sorted.size() / 2;
        T median = sorted.get(mid);

        // Partition lists for each axis
        List<T> lx = new ArrayList<>();
        List<T> rx = new ArrayList<>();
        List<T> ly = new ArrayList<>();
        List<T> ry = new ArrayList<>();
        partitionLists(x, y, lx, rx, ly, ry, axis, median);

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
     * Partitions the given lists of elements into left and right subsets along a specified axis
     * relative to a median element. This method recursively prepares sublists for a balanced k-d tree
     * structure by splitting elements based on their coordinates.
     *
     * @param x the list of elements sorted by the x-coordinate
     * @param y the list of elements sorted by the y-coordinate
     * @param lx the list to store elements from x that fall into the left subset
     * @param rx the list to store elements from x that fall into the right subset
     * @param ly the list to store elements from y that fall into the left subset
     * @param ry the list to store elements from y that fall into the right subset
     * @param axis the axis (0 for x-axis, 1 for y-axis) used for the partitioning
     * @param median the element representing the median point used to partition the lists
     */
    private void partitionLists(List<T> x, List<T> y, List<T> lx, List<T> rx,
                             List<T> ly, List<T> ry, int axis, T median) {

        int medianCoOrd = (axis == 0) ? median.x() : median.y();
        partition(x, lx, rx, axis, median, medianCoOrd);
        partition(y, ly, ry, axis, median, medianCoOrd);
    }

    /**
     * Partitions the input list into two subsets, left and right, based on the specified axis
     * and the coordinate of the given median element. Elements are compared against the
     * median's coordinate value and are appended to the left or right list accordingly.
     *
     * @param list the list of elements to be partitioned
     * @param left the list to store elements that are less than or equal to the median's coordinate
     * @param right the list to store elements that are greater than the median's coordinate
     * @param axis the axis (0 for x-axis, 1 for y-axis) used for determining the coordinate
     * @param median the median element used as the reference for partitioning
     * @param medianCoOrd the coordinate value of the median element along the specified axis
     */
    private void partition(List<T> list, List<T> left, List<T> right, int axis, T median, int medianCoOrd) {
        for (int i = 0; i < list.size(); i++) {
            T item = list.get(i);
            if (item.equals(median)) {
                continue;
            }

            int coOrd = (axis == 0) ? item.x() : item.y();
            if (coOrd <= medianCoOrd) {
                left.append(item);
            } else {
                right.append(item);
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

    /**
     * Calculates the squared Euclidean distance between two points in a 2D space.
     *
     * @param a the first point, containing x and y coordinates
     * @param b the second point, containing x and y coordinates
     * @return the squared distance between the two points
     */
    private double distance(T a, T b) {
        int dx = a.x() - b.x();
        int dy = a.y() - b.y();
        return Math.pow(dx, 2) + Math.pow(dy, 2);
    }

}
