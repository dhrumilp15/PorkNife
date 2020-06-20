import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Handler for game textures, texture icons for minimap
 */
public class Texture {
    public int[] pixels;
    private String loc;
    public final int SIZE;

    /**
     * Creates a Texture object
     *
     * @param location The file location of the image
     * @param size The size of the image (in px)
     */
    public Texture(String location, int size) {
        loc = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    /**
     * Load this texture object's image
     */
    private void load() {
        try {
            BufferedImage image = ImageIO.read(this.getClass().getResource(loc));
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0,0,w,h,pixels,0,w);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    // Given textures
    public static Texture wood = new Texture("wood.jpg", 64);
    public static Texture brick = new Texture("redbrick.png", 64);
    public static Texture bluestone = new Texture("bluestone.jpg", 64);
    public static Texture stone = new Texture("greystone.png", 64);
    public static Texture green = new Texture("Green_Brick.png", 64);

    // texture icons - floor gray, wood, brick, light brown, blue, green
    public static Color[] colours = {Color.gray, new Color(154,107,66), new Color(109,79,44), new Color(15,13,189), new Color(162,147,128), new Color(23,215,20)};
}
