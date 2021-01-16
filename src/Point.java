import java.util.Arrays;
import java.util.InputMismatchException;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Represents a point in Rn
 *
 * It's an "immutable" class
 */
public class Point {
    private final float[] coordinates;

    public Point(float[] coordinates) {
        this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
    }

    public Point(Point p) {
        this.coordinates = p.getCoordinates();
    }

    /**
     * Get the dimension of the point
     * @return dimension
     */
    public int getDimension() {
        return coordinates.length;
    }

    /**
     * Get the i-th coordinate of the point (1-indexed)
     * @param i coordinate to get
     * @return value of i-th coordinate
     * @throws IndexOutOfBoundsException if the coordinate is <1 or >dimension
     */
    public float getCoordinate(int i) throws IndexOutOfBoundsException {
        if (i > coordinates.length || i < 1) {
            throw new IndexOutOfBoundsException("Invalid dimension");
        }

        return this.coordinates[i-1];
    }

    /**
     * Get all the coordinates of the point
     * @return coordinates
     */
    public float[] getCoordinates() {
        return Arrays.copyOf(coordinates, coordinates.length);
    }

    /**
     * Calculate the euclidean distance between two points
     * @param p1 first point
     * @param p2 second point
     * @return distance
     * @throws InputMismatchException if the points has incompatible dimensions
     */
    public static float getEuclideanDistance(Point p1, Point p2) throws InputMismatchException {
        if (p1.getDimension() != p2.getDimension()) {
            throw new InputMismatchException("Incompatible point (due to its dimension)");
        }

        float squareSum = 0;
        for (int i = 0; i < p1.getDimension(); i++) {
            squareSum += pow(p1.getCoordinate(i+1) - p2.getCoordinate(i+1), 2);
        }
        return (float) sqrt(squareSum);
    }
}
