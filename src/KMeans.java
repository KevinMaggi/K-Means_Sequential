import java.util.*;

import static java.lang.Math.abs;

public final class KMeans<T extends Point> {

    public ArrayList<Cluster<T>> clusterize(int k, SetOfPoints<T> data) throws InputMismatchException{
        T[] points = data.toArray();

        Point[] centroids = getInitialCentroids(k, data.getPoints());
        Integer[] clusterization = new Integer[data.size()];

        boolean stop = false;

        while (!stop) {
            updateClusters(centroids, points, clusterization);
            Point[] newCentroids = newCentroids(points, clusterization, k);

            if(checkStop(centroids, newCentroids, 5)) {
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

    private boolean checkStop(Point[] oldCentroids, Point[] newCentroids, float toll) {
        for (int k = 0; k < oldCentroids.length; k++) {
            float[] oldCentroid = oldCentroids[k].getCoordinates();
            float[] newCentroid = newCentroids[k].getCoordinates();
            for (int i = 0; i < oldCentroid.length; i++) {
                if (abs(oldCentroid[i] - newCentroid[i]) > toll) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateClusters(Point[] centroids, Point[] points, Integer[] clusters) {
        for (int p = 0; p < points.length; p++) {
            float minDistance = Float.POSITIVE_INFINITY;
            Integer nearestCentroid = null;
            for (int c = 0; c < clusters.length; c++) {
                try {
                    float distance = Point.getEuclideanDistance(centroids[c], points[p]);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestCentroid = c;
                    }
                } catch (Exception ignore) { }
            }
            clusters[p] = nearestCentroid;
        }
    }

    private Point[] newCentroids(Point[] points, Integer[] clusters, int numberOfClusters) {
        int numberOfPoints = points.length;
        int dimensionOfSpace = points[0].getDimension();
        float[][] sum = new float[numberOfClusters][dimensionOfSpace];
        try {
            for (int i = 0; i < numberOfPoints; i++) {
                for (int j = 0; j < dimensionOfSpace; j++) {
                    sum[clusters[i]][j] += points[i].getCoordinate(j+1);
                }
            }
        } catch (Exception ignore) {System.out.println(ignore.getMessage());}


        Point[] centroids = new Point[numberOfClusters];
        for (int k = 0; k < numberOfClusters; k++) {
            float[] coordinate = new float[dimensionOfSpace];
            for (int j = 0; j < dimensionOfSpace; j++) {
                coordinate[j] = sum[k][j]/numberOfPoints;
            }
            centroids[k] = new Point(coordinate);
        }

        return centroids;
    }

    private Point[] getInitialCentroids(int k, ArrayList<T> points) throws InputMismatchException {
        int numPoints = points.size();

        if (numPoints < k) {
            throw new InputMismatchException("Points are not enough for this k.");
        } else if (numPoints == k) {
            return points.toArray(new Point[k]);
        } else {
            // Distances table
            float[][] distances = new float[numPoints][numPoints];
            for (int i = 0; i < numPoints; i++) {
                for (int j = 0; j < numPoints; j++) {
                    try {
                        float distance = Point.getEuclideanDistance(points.get(i), points.get(j));
                        distances[i][j] = distance;
                    } catch (Exception ignored) {}
                }
            }

            HashSet<Point> myCentroids = new HashSet<>(k);
            HashSet<Integer> pointIndexes = new HashSet<>(k);
            //int firstIndex = (int) Math.floor(Math.random() * (numPoints-1));
            int firstIndex = 0;
            myCentroids.add(points.get(firstIndex));
            pointIndexes.add(firstIndex);

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
                myCentroids.add(points.get(newCentroidIndex));
                pointIndexes.add(newCentroidIndex);
            }

            Point[] centroids = new Point[myCentroids.size()];
            centroids = new ArrayList<>(myCentroids).toArray(centroids);
            return centroids;
        }
    }
}
