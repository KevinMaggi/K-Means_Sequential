/**
 * Represents a Pixel, so a point in the RGB space with integer value and the coordinates x and y in an image
 */
public class RGBPixel extends RGBPoint{
    private final int x,y;

    public RGBPixel(int x, int y, int r, int g, int b) {
        super(r,g,b);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
