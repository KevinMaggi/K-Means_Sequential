import java.util.*;

import static java.lang.Math.abs;

/**
 * For k-Means clusterization
 * @param <T> subclass of Point
 */
public final class KMeans<T extends Point> {
    /**
     * Maximum change of a centroid (in every direction) to be considered unchanged
     */
    public static final float tolerance = 0.005F;

    /**
     * Performs the k-means clusterization
     * @param k number of clusters
     * @param data points to be clusterized
     * @return clusters
     * @throws IllegalArgumentException if there aren't enough points (<k)
     * @throws NullPointerException if input data is null
     */
    public ArrayList<Cluster<T>> clusterize(int k, final SetOfPoints<T> data) throws IllegalArgumentException, NullPointerException {
        if(data == null) {
            throw new NullPointerException("Input data can't be null");
        }

        int numPoints = data.size();
        if (numPoints < k) {
            throw new IllegalArgumentException("Not enough points for this k (k=" + k + ")");
        }
        if (k == 1) {
            ArrayList<Cluster<T>> clusters = new ArrayList<>(k);
            clusters.add(new Cluster<T>(data));
            return clusters;
        }
        T[] points = data.toArray();

        Point[] centroids = initialCentroids(k, data.getPoints());
        Integer[] clusterization = new Integer[numPoints];
        boolean stop = false;

        while (!stop) {
            updateClusters(centroids, points, clusterization);
            Point[] newCentroids = newCentroids(points, clusterization, k);

            if(checkStop(centroids, newCentroids)) {
                stop = true;
            } else {
                centroids = newCentroids;
            }
        }

        ArrayList<Cluster<T>> clusters = new ArrayList<>();
        for (int j = 0; j < k; j++) {
            clusters.add(j, new Cluster<>(data.getDomain()));
        }
        for (int i = 0; i < points.length; i++) {
            clusters.get(clusterization[i]).add(points[i]);
                // cannot throws exception because the domain of all clusters is the same from which the data belongs to
        }

        return clusters;
    }

    /**
     * Checks the stop condition based on the unchange (under a certain tolerance) of centroids position
     * @param oldCentroids old centroids
     * @param newCentroids new centroids
     * @return true if have to stop
     */
    private boolean checkStop(final Point[] oldCentroids, final Point[] newCentroids) {
        for (int k = 0; k < oldCentroids.length; k++) {
            float[] oldCentroid = oldCentroids[k].getCoordinates();
            float[] newCentroid = newCentroids[k].getCoordinates();
            for (int i = 0; i < oldCentroid.length; i++) {
                if (abs(oldCentroid[i] - newCentroid[i]) > KMeans.tolerance) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Assigns every points to a cluster. It returns the clusterization in the third parameter
     * @param centroids centroids of clusters
     * @param points points to be assigned
     * @param clusterization clusterization
     */
    private void updateClusters(final Point[] centroids, final Point[] points, Integer[] clusterization) {
        for (int p = 0; p < points.length; p++) {
            float minDistance = Float.POSITIVE_INFINITY;
            Integer nearestCentroid = null;

            for (int c = 0; c < centroids.length; c++) {
                float distance = Point.getEuclideanDistance(centroids[c], points[p]);
                    // cannot throws exception because we ensure that centroids and points wer all not null and of the same dimension
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCentroid = c;
                }
            }
            clusterization[p] = nearestCentroid;
        }
    }

    /**
     * Calculates the new centroids based on the updated clusterization
     * @param points points
     * @param clusterization clusterization
     * @param k number of clusters
     * @return new centroids
     */
    private Point[] newCentroids(final Point[] points, final Integer[] clusterization, int k) {
        int dimension = points[0].getDimension();
        float[][] sum = new float[k][dimension];
        int[] clustersSize = new int[k];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < dimension; j++) {
                sum[clusterization[i]][j] += points[i].getCoordinate(j+1);
                    // cannot throws exception because all point has the same dimension and we respect it
            }
            clustersSize[clusterization[i]]++;
        }

        Point[] centroids = new Point[k];
        for (int w = 0; w < k; w++) {
            float[] coordinate = new float[dimension];
            for (int j = 0; j < dimension; j++) {
                coordinate[j] = sum[w][j]/clustersSize[w];
            }
            centroids[w] = new Point(coordinate);
        }

        return centroids;
    }

    /**
     * Determines the initial centroids by picking them randomly
     * @param k number of centroids
     * @param points points
     * @return centroids
     */
    private Point[] randomInitialCentroids(int k, final ArrayList<T> points) {
        Point[] centroids = new Point[k];
        int numPoints = points.size();
        Random r = new Random();
        for (int i = 0; i < k; i++) {
            int index = r.nextInt(numPoints);
            centroids[i] = new Point(points.get(index));
        }
        return centroids;
    }

    /**
     * Determines the initial centroids by picking the first point in the list and then picking iteratively
     * the point that maximize the minimum distance from previous centroids
     * @param k number of centroids
     * @param points points
     * @return centroids
     */
    private Point[] initialCentroids(int k, final ArrayList<T> points) {
        int numPoints = points.size();
        if (numPoints == k) {
            Point[] centroids = new Point[k];
            for (int i = 0; i < k; i++) {
                centroids[i] = new Point(points.get(i));
            }

            return centroids;
        }

        Point[] centroids = new Point[k];
        // Random r = new Random();
        // int firstCentroidIndex = r.nextInt(numPoints);
        int firstCentroidIndex = 0;
        centroids[0] = new Point(points.get(firstCentroidIndex));

        for (int i = 1; i < k; i++) {
            float maxMinDistance = 0;
            int newCentroidIndex = 0;

            for (int p = 0; p < numPoints; p++) {
                float minDistance = Float.POSITIVE_INFINITY;
                for (int c = 0; c < i; c++) {
                    float distance = Point.getEuclideanDistance(points.get(p), centroids[c]);
                        // cannot throws exception because SetOfPoint ensure that all the points are not null and of the same dimension
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }
                if (minDistance > maxMinDistance) {
                    maxMinDistance = minDistance;
                    newCentroidIndex = p;
                }
            }
            centroids[i] = new Point(points.get(newCentroidIndex));
        }

        return centroids;
    }
}
