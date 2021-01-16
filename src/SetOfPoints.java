import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.InputMismatchException;

/**
 * Represents a set of points all belonging the same domain
 */
public class SetOfPoints<T extends Point> {
    protected final ArrayList<T> points;
    private final Domain domain;

    public SetOfPoints(Domain domain) {
        this.points = new ArrayList<T>();
        this.domain = domain;
    }

    public Domain getDomain() {
        return domain;
    }

    public ArrayList<T> getPoints() {
        return new ArrayList<T>(points);
    }

    /**
     * Add a point to the set
     * @param point point to add
     * @return true if success, false otherwise
     * @throws InputMismatchException if the point doesn't belong to the domain
     */
    public boolean add(T point) throws InputMismatchException {
        if (domain.contains(point))
            return points.add(point);
        else
            throw new InputMismatchException("Incompatible point");
    }

    /**
     * Add some point to the set
     * @param points points to add
     * @return true if success, false otherwise
     * @throws InputMismatchException if some point doesn't belong to the domain
     */
    public boolean addAll(Collection<T> points) throws InputMismatchException {
        boolean result = true;
        for (T point : points) {
            result &= this.add(point);
        }
        return result;
    }

    /**
     * Gets the size of the set
     * @return size
     */
    public int size() {
        return points.size();
    }

    /**
     * Gets the set of points as array
     * @return array of generic points
     */
    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(Point.class, points.size());
        return points.toArray(array);
    }
}
