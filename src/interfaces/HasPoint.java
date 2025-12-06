package interfaces;

/**
 * Represents an object that has a point in a 2-dimensional coordinate system.
 * This interface provides methods to retrieve the x and y coordinates of the point.
 */
public interface HasPoint {
    /**
     * Returns the x-coordinate of a point.
     *
     * @return the x-coordinate as an integer
     */
    int getX();

    /**
     * Returns the y-coordinate of a point.
     *
     * @return the y-coordinate as an integer
     */
    int getY();
}
