import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a set of points all belonging the same domain
 */
public class SetOfPoints<T extends Point> {
    protected final HashSet<T> points;
    private final Domain domain;

    public SetOfPoints(Domain domain) {
        this.points = new HashSet<T>();
        this.domain = domain;
    }

    public Domain getDomain() {
        return domain;
    }

    public Set<T> getPoints() {
        return Set.copyOf(points);
    }

    /**
     * Add a point to the set
     * @param point point to add
     * @return true if success, false otherwise
     * @throws Exception if the point doesn't belong to the domain
     */
    public boolean add(T point) throws Exception {
        if (domain.contains(point))
            return points.add(point);
        else
            throw new Exception("Incompatible point");
    }

    /**
     * Add some point to the set
     * @param points points to add
     * @return true if success, false otherwise
     * @throws Exception if some point doesn't belong to the domain
     */
    public boolean addAll(Collection<T> points) throws Exception {
        boolean result = true;
        for (T point : points) {
            result &= this.add(point);
        }
        return result;
    }


}
