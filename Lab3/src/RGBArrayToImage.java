import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RGBArrayToImage {
    private final int[][][] rgbArray;

    public RGBArrayToImage(int[][][] pixelData_) {
        rgbArray = pixelData_;
    }

    public void work() {
        BufferedImage img = createImage();
        saveImage(img, "output.png");
    }

    private BufferedImage createImage() {
        int height = rgbArray.length;
        int width = rgbArray[0].length;


        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = rgbArray[y][x][0];
                int green = rgbArray[y][x][1];
                int blue = rgbArray[y][x][2];

                int rgb = (red << 16) | (green << 8) | blue;
                img.setRGB(x, y, rgb);
            }
        }

        return img;
    }

    private static void saveImage(BufferedImage img, String filePath) {
        try {
            File output = new File(filePath);
            ImageIO.write(img, "png", output);
            System.out.println("Image saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
