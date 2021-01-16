import java.util.Arrays;

/**
 * Represents a domain for points. It's an iper-rectangular subset of Rn, eventually Rn itself.
 * A point X in a domain must satisfy LB_i <= X_i <= UB_i for i = 1...n
 *
 * It's an immutable class
 */
public final class Domain {
    /**
     * Dimension of the Rn space
     */
    private final int dimension;

    /**
     * Array of lower-bounds
     */
    private final float[] lowerBounds;

    /**
     * Array of upper-bounds
     */
    private final float[] upperBounds;

    private Domain(int dimension, float[] lowerBounds, float[] upperBounds) {
        this.dimension = dimension;
        this.lowerBounds = Arrays.copyOf(lowerBounds, lowerBounds.length);
        this.upperBounds = Arrays.copyOf(upperBounds, upperBounds.length);
    }

    /**
     * Checks if represents the same domain of another
     * @param d domain to compare with
     * @return true if represents the same domain
     */
    public boolean equals(Domain d) {
        if (dimension == d.getDimension()) {
            return Arrays.equals(lowerBounds, d.getLowerBounds()) && Arrays.equals(upperBounds, d.getUpperBounds());
        }
        return false;
    }

    /**
     * Checks if a given point belongs to this domain
     * @param p point to check
     * @return true if the point belongs to this domain
     */
    public boolean contains(Point p) {
        if (p.getDimension() == dimension) {
            for (int i = 0; i < dimension; i++) {
                try {
                    float p_i = p.getCoordinate(i+1);
                    if (p_i < lowerBounds[i] || p_i > upperBounds[i])
                        return false;
                } catch (IndexOutOfBoundsException ignore) { }
            }
            return true;
        }
        return false;
    }

    /**
     * Creates the Euclidean space Rn
     * @param dimension dimensions of Rn
     * @return Rn
     */
    public static Domain Rn(int dimension) {
        float[] lowerUnbound = new float[dimension];
        float[] upperUnbound = new float[dimension];
        for (int i = 0; i < dimension; i++) {
            lowerUnbound[i] = Float.NEGATIVE_INFINITY;
            upperUnbound[i] = Float.POSITIVE_INFINITY;
        }
        return new Domain(dimension, lowerUnbound, upperUnbound);
    }

    /**
     * Creates the RGB space
     * @return RGB space
     */
    public static Domain RGB() {
        float[] lowerBounds = new float[3];
        float[] upperBounds = new float[3];
        for (int i = 0; i < 3; i++) {
            lowerBounds[i] = 0;
            upperBounds[i] = 255;
        }
        return new Domain(3, lowerBounds, upperBounds);
    }

    public int getDimension() {
        return dimension;
    }

    public float[] getLowerBounds() {
        return Arrays.copyOf(lowerBounds, lowerBounds.length);
    }

    public float[] getUpperBounds() {
        return Arrays.copyOf(upperBounds, upperBounds.length);
    }
}
