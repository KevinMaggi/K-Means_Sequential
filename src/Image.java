import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Contains all operation that can be applied on an image
 */
public class Image {
    /**
     * Loads an image from filesystem as Java Object
     * @param path path of the image in the filesystem
     * @return image
     * @throws IOException if an error occurs reading the file
     */
    public static BufferedImage load(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    /**
     * Convert the image in a set of pixel (seen as point)
     * @param img image to pixelize
     * @return set of pixel
     */
    public static SetOfPoints<RGBPixel> pixelize(BufferedImage img) {
        SetOfPoints<RGBPixel> data = new SetOfPoints<>(Domain.RGB());
        try {
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    Color color = new Color(img.getRGB(x, y));
                    RGBPixel p = new RGBPixel(x, y, color.getRed(), color.getGreen(), color.getBlue());
                    data.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Save on the filesystem as PNG a segmentation of an image
     * @param clusters segments
     * @param path path of the file in the filesystem
     * @param width width of the image
     * @param height height of the image
     * @throws IOException if an error occurs writing the file
     */
    public static void export(Collection<Cluster<RGBPixel>> clusters, String path, int width, int height) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (Cluster<RGBPixel> c : clusters) {
            Point center = c.getCenter();
            try {
                Color color = new Color(center.getCoordinate(1)/255, center.getCoordinate(2)/255, center.getCoordinate(3)/255);
                for (RGBPixel p : c.getPoints()) {
                    img.setRGB(p.getX(), p.getY(), color.getRGB());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ImageIO.write(img, "png", new File(path));
    }
}
