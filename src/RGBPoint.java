/**
 * Represents a point in the RGB space. The value of R, G and B can be float
 */
class RGBPoint extends Point{
    public RGBPoint(float r, float g, float b) {
        super(new float[]{r, g, b});
    }

    public float getR() throws IndexOutOfBoundsException{
        return super.getCoordinate(1);
    }

    public float getG() throws IndexOutOfBoundsException {
        return super.getCoordinate(2);
    }

    public float getB() throws IndexOutOfBoundsException {
        return super.getCoordinate(3);
    }
}
