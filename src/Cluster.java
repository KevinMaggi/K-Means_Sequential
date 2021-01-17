import java.util.Collection;
import java.util.InputMismatchException;

/**
 * Represents a cluster of points
 */
public class Cluster<T extends Point> extends SetOfPoints<T> {
    public Cluster(Domain domain){
        super(domain);
    }

    public Cluster(Domain domain, Collection<T> points) {
        super(domain, points);
    }

    public Cluster(SetOfPoints<T> sop) {    // Also good as copy-constructor
        super(sop);
    }

    /**
     * Get the centroid of the cluster
     * @return centroid
     */
    public Point getCenter() {
        int dimension = getDomain().getDimension();
        float[] coordinates = new float[dimension];

        for (int i = 0; i < dimension; i++) {
            for (T p : this.points) {
                try {
                    coordinates[i] += p.getCoordinate(i + 1);
                } catch (ArrayIndexOutOfBoundsException ignored) { }
            }
            coordinates[i] /= this.points.size();
        }

        return new Point(coordinates);
    }

    /**
     * Get the diameter of the cluster
     * @return diameter
     */
    public float getDiameter() {
        float max = 0;
        int n = this.points.size();
        for (int i = 0; i < n; i++) {
            Point p1 = this.points.get(i);
            for (int j = i+1; j < n; j++) {
                Point p2 = this.points.get(j);
                try {
                    float distance = Point.getEuclideanDistance(p1, p2);
                    if (distance > max) {
                        max = distance;
                    }
                } catch (InputMismatchException ignored) {}
            }
        }
        return max;
    }
}
