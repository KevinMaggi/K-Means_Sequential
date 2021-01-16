/**
 * Represents a point in the RGB space. The value of R, G and B can be float
 */
public class RGBPoint extends Point{
    public RGBPoint(float r, float g, float b) {
        super(new float[]{r, g, b});
    }

    public float getR() throws Exception {
        return super.getCoordinate(1);
    }

    public float getG() throws Exception {
        return super.getCoordinate(2);
    }

    public float getB() throws Exception {
        return super.getCoordinate(3);
    }
}
