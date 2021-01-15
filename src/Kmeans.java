import java.util.*;

public final class Kmeans {

    public static HashSet<Cluster<Point>> runKmeans(int k, SetOfPoints<Point> points) {
        try {
            HashSet<Point> centroids = getInitialCentroids(k, points.getPoints());
        } catch(InputMismatchException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static HashSet<Point> getInitialCentroids(int k, ArrayList<Point> points) throws InputMismatchException {
        int numPoints = points.size();

        if (numPoints < k) {
            throw new InputMismatchException("Points are not enough for this k.");
        } else if (numPoints == k) {
            return new HashSet<>(points);
        } else {
            //costruzione tabella di distanze
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
            return new HashSet<>(myCentroids);
        }
    }
}
