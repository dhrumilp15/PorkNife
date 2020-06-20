import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {
    public int[] pixels;
    private String loc;
    public final int SIZE;

    public Texture(String location, int size) {
        loc = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    private void load() {
        try {
            BufferedImage image = ImageIO.read(new File(loc));
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0,0,w,h,pixels,0,w);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static Texture wood = new Texture("res/wood.jpg", 64);
    public static Texture brick = new Texture("res/redbrick.png", 64);
    public static Texture bluestone = new Texture("res/bluestone.jpg", 64);
    public static Texture stone = new Texture("res/greystone.png", 64);
    public static Texture green = new Texture("res/Green_Brick.png", 64);
    // floor gray, wood, brick, light brown, blue, green
    public static Color[] colours = {Color.gray, new Color(154,107,66), new Color(109,79,44), new Color(15,13,189), new Color(162,147,128), new Color(23,215,20)};
}
