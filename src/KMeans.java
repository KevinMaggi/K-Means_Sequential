import java.util.*;

import static java.lang.Math.abs;

public final class KMeans<T extends Point> {
    public static final int tollerance = 5;

    public ArrayList<Cluster<T>> clusterize(int k, SetOfPoints<T> data) throws InputMismatchException {
        int numPoints = data.size();
        if (numPoints < k) {
            throw new InputMismatchException("Points are not enough for this k.");
        }
        T[] points = data.toArray();

        Point[] centroids = getInitialCentroids(k, data.getPoints());
        int[] clusterization = new int[numPoints];

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
            try {
                clusters.get(clusterization[i]).add(points[i]);
            } catch (Exception ignore) { }
        }

        return clusters;
    }

    private boolean checkStop(Point[] oldCentroids, Point[] newCentroids) {
        for (int k = 0; k < oldCentroids.length; k++) {
            float[] oldCentroid = oldCentroids[k].getCoordinates();
            float[] newCentroid = newCentroids[k].getCoordinates();
            for (int i = 0; i < oldCentroid.length; i++) {
                if (abs(oldCentroid[i] - newCentroid[i]) > KMeans.tollerance) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateClusters(Point[] centroids, Point[] points, int[] clusters) {
        for (int p = 0; p < points.length; p++) {
            try {
                float minDistance = Point.getEuclideanDistance(centroids[0], points[p]);
                int nearestCentroid = 0;
                for (int c = 1; c < clusters.length; c++) {
                    float distance = Point.getEuclideanDistance(centroids[c], points[p]);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestCentroid = c;
                    }
                }
                clusters[p] = nearestCentroid;
            } catch (Exception ignore) { }
        }
    }

    private Point[] newCentroids(Point[] points, int[] clusters, int numberOfClusters) {
        int numberOfPoints = points.length;
        int dimensionOfSpace = points[0].getDimension();
        float[][] sum = new float[numberOfClusters][dimensionOfSpace];
        float[] clusterDimension = new float[numberOfClusters];
        try {
            for (int i = 0; i < numberOfPoints; i++) {
                for (int j = 0; j < dimensionOfSpace; j++) {
                    sum[clusters[i]][j] += points[i].getCoordinate(j+1);
                }
                clusterDimension[clusters[i]]++;
            }
        } catch (Exception ignore) { }

        Point[] centroids = new Point[numberOfClusters];
        for (int k = 0; k < numberOfClusters; k++) {
            float[] coordinate = new float[dimensionOfSpace];
            for (int j = 0; j < dimensionOfSpace; j++) {
                coordinate[j] = sum[k][j]/clusterDimension[k];
            }
            centroids[k] = new Point(coordinate);
        }

        return centroids;
    }

    private Point[] getInitialCentroids(int k, ArrayList<T> points) throws InputMismatchException {
        int numPoints = points.size();

        if (numPoints == k) {
            return points.toArray(new Point[k]);
        } else {
            // Distances table
            float[][] distances = new float[numPoints][numPoints];
            for (int i = 0; i < numPoints; i++) {
                for (int j = 0; j < i; j++) {
                    try {
                        float distance = Point.getEuclideanDistance(points.get(i), points.get(j));
                        distances[i][j] = distance;
                        distances[j][i] = distance;
                    } catch (Exception ignored) {}
                }
            }

            Point[] myCentroids = new Point[k];
            int[] pointIndexes = new int[k];
            //int firstIndex = (int) Math.floor(Math.random() * (numPoints-1));
            int firstIndex = 0;
            myCentroids[0] = points.get(firstIndex);
            pointIndexes[0] = firstIndex;

            for (int i = 1; i < k; i++) {
                float maxMinDistance = 0;
                int newCentroidIndex = 0;

                for (int indexPoint = 0; indexPoint < numPoints; indexPoint++) {
                    float minDistance = distances[indexPoint][firstIndex];
                    for (int indexCentroid : pointIndexes) {
                        float distance = distances[indexPoint][indexCentroid];
                        if (distance < minDistance) {
                            minDistance = distance;
                        }
                    }

                    if (minDistance > maxMinDistance) {
                        maxMinDistance = minDistance;
                        newCentroidIndex = indexPoint;
                    }
                }
                myCentroids[i] = new Point(points.get(newCentroidIndex));
                pointIndexes[i] = newCentroidIndex;
            }

            return myCentroids;
        }
    }
}
