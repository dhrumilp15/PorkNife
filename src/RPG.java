import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 * Main Game Code
 */
public class RPG extends JFrame implements Runnable {
    public ArrayList<Texture> textures;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    public int[] pixels;
    public Camera camera;
    public Screen screen;
    public Map map;
    public MiniMap miniMap;
    public int mapSize = 160;

    /**
     * Creates an RPG object
     */
    public RPG() {
        map = new Map();
        thread = new Thread(this);
        image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        textures = new ArrayList<>();
        textures.add(Texture.wood);
        textures.add(Texture.brick);
        textures.add(Texture.bluestone);
        textures.add(Texture.stone);
        textures.add(Texture.green);

        camera = new Camera(map.startx + 0.5, map.starty + 0.5,1,0,0,-0.66);
        screen = new Screen(map, textures, 640, 480);
        miniMap = new MiniMap(camera,map);

        addKeyListener(camera);
        setSize(640, 480);
        setResizable(false);
        setTitle("PorkNife");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }

    /**
     * Executed as soon as the game is created
     */
    private synchronized void start() {
        running = true;
        thread.start();
    }

    /**
     * To end the game, join thread
     */
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * To show image to the screen
     *
     * Also shows minimap
     * Instructional Text
     * @param fp The filepath of an image to show (endscreen)
     */
    public void render(String fp) {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        if (fp.equals("")) {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            miniMap.draw(g, getWidth(), getHeight());
            g.setColor(Color.white);
            g.drawString("FIND THE GREEN BLOCKS! HINT: bottom right...", getWidth() / 2 - 150, getHeight() - 10);
        }
        else {
            BufferedImage img = null;
            try { img = ImageIO.read(this.getClass().getResource(fp));}
            catch (IOException e) { e.printStackTrace();}
            g.drawImage(img, 0, 0, image.getWidth(), image.getHeight(), null);
        }
        bs.show();
    }

    /**
     * Executed directly after thread.start()
     * Main Game Loop
     */
    public void run() {
        // This executes directly after `thread.start()`
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;//60 times per second
        double delta = 0;
        requestFocus();
        while(running) {
            long now = System.nanoTime();
            delta += ((now-lastTime) / ns);
            lastTime = now;
            while (delta >= 1)//Make sure update is only happening 60 times a second
            {
                //handles all of the logic restricted time
                screen.update(camera, pixels);
                camera.update(map);
                miniMap.update(camera);
                if ((int)camera.xPos == map.getLen() - 2 && (int)camera.yPos == map.getLen() - 2) {
                    running = false;
                    render("squid.png");
                }
                delta--;
            }
            render("");//displays to the screen unrestricted time
        }
        stop();
    }

    /**
     * Main Program Loop
     *
     * @param args No args needed
     */
    public static void main(String[] args) {
        RPG main = new RPG();
    }
}
