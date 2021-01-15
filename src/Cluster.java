/**
 * Represents a cluster of points
 */
public class Cluster<T extends Point> extends SetOfPoints<T> {
    public Cluster(Domain domain){
        super(domain);
    }

    /**
     * Get the centroid of the cluster
     * @return centroid
     */
    public Point getCenter() {
        float[] coordinates = new float[getDomain().getDimension()];

        for (T p : this.points) {
            for (int i = 0; i < getDomain().getDimension(); i++) {
                try {
                    coordinates[i] += p.getCoordinate(i + 1);
                } catch (Exception ignored) {}
            }
        }

        for (int i = 0; i < getDomain().getDimension(); i++) {
            coordinates[i] /= this.points.size();
        }

        Point centroid;
        try {
            centroid = new Point(coordinates);
            return centroid;
        } catch (Exception ignore) { return null; }
    }

    /**
     * Get the diameter of the cluster
     * @return diameter
     */
    public float getDiameter() {    // TODO is possible to improve performance by halving the distances to calculate
        float max = 0;
        for (Point p1 : this.points) {
            for (Point p2 : this.points) {
                if (p1 != p2) {
                    try {
                        float distance = Point.getEuclideanDistance(p1, p2);
                        if (distance > max) {
                            max = distance;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        return max;
    }
}
