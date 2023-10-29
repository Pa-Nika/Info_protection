import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        in.close();

        LSB lsb = new LSB(text);
        lsb.algorithm();
    }
}
