import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class LSB {
    private int n = 0;
    private String bits = new String("");
    private String text = new String("");
    private StringBuilder imageBits = new StringBuilder();
    private int[][][] pixelData;

    public LSB(String text_) {
        text = text_;
    }

    public String stringToBinary() {
        StringBuilder answer = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            answer.append("0".repeat(Math.max(0, 8 - Integer.toBinaryString(c).length())));
            answer.append(Integer.toBinaryString(c));
        }
        n = text.length() * 8;
        bits = answer.toString();
        return answer.toString();
    }

    private void getPixelData(BufferedImage img, int y, int x, int[][][] rgb) {
        int argb = img.getRGB(x, y);

        rgb[y][x][0] = (argb >> 16) & 0xff; // Red
        rgb[y][x][1] = (argb >> 8) & 0xff;  // Green
        rgb[y][x][2] = argb & 0xff;         // Blue
    }


    public void fillImageArr() {
        BufferedImage img;

        try {
            img = ImageIO.read(new File("img.jpg"));

            int height = img.getHeight();
            int width = img.getWidth();

            pixelData = new int[height][width][3];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    getPixelData(img, i, j, pixelData);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String replaceCharAtIndex(String str, int index, char replacement) {
        char[] charArray = str.toCharArray();
        charArray[index - 1] = replacement;

        return new String(charArray);
    }


    public void algorithm() {
        stringToBinary();
        fillImageArr();

        for (int i = 0; i < bits.length(); i++) {
            String color = Integer.toBinaryString(pixelData[0][i][1]);
            String modifiedString = replaceCharAtIndex(color, color.length(), bits.charAt(i));
            pixelData[0][i][1] = Integer.parseInt(modifiedString, 2);
        }

        RGBArrayToImage converter = new RGBArrayToImage(pixelData);
        converter.work();

        MessageGet strGet = new MessageGet("output.png", n);
        System.out.println("Extracted Message: " + strGet.work());
    }
}