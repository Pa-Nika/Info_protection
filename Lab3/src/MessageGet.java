import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MessageGet {
    private String extractedMessage;
    private final String imagePath;
    private final int numberOfPixels;

    public MessageGet(String imagePath, int numberOfPixels) {
        this.imagePath = imagePath;
        this.numberOfPixels = numberOfPixels;
    }


    private void extractMessageFromImage() {
        BufferedImage img;

        try {
            img = ImageIO.read(new File(imagePath));
            StringBuilder extractedBits = new StringBuilder();

            int height = img.getHeight();
            int width = img.getWidth();

            int[] pixelData = new int[width];
            img.getRGB(0, 0, width, 1, pixelData, 0, width);

            int[][][] rgbArray = new int[1][width][3];

            for (int x = 0; x < width; x++) {
                int pixel = pixelData[x];

                rgbArray[0][x][0] = (pixel >> 16) & 0xFF;  // Red
                rgbArray[0][x][1] = (pixel >> 8) & 0xFF;   // Green
                rgbArray[0][x][2] = pixel & 0xFF;          // Blue
            }
            for (int x = 0; x < numberOfPixels; x++) {
                int green = rgbArray[0][x][1] & 1;
                extractedBits.append(green);
            }
            System.out.println(extractedBits);

            binaryStringToString(extractedBits.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void binaryStringToString(String binaryString) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < binaryString.length(); i += 8) {
            String byteStr = binaryString.substring(i, Math.min(i + 8, binaryString.length()));
            int charCode = Integer.parseInt(byteStr, 2);
            char character = (char)charCode;
            result.append(character);
        }

        extractedMessage = String.valueOf(result);
    }

    public String work() {
        extractMessageFromImage();
        return extractedMessage;
    }
}
