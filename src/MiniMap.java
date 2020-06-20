import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MiniMap extends JFrame{
    public static int[][] minimap;
    public Map map;
    public Camera camera;
    private int mapSize;
    private int offsetX = 10;
    private int offsetY = 10;

    public MiniMap(Camera camera, Map map) {
        minimap = new int[map.getLen()][map.getLen()];
        this.map = map;
        this.camera = camera;
        for (int i = 0; i < map.getLen(); i++) {
            for (int j = 0; j < map.getLen(); j++)
                minimap[i][j] = -1;
        }
        mapSize = 10 * map.getLen();
    }

    /**
     * Update the minimap to store saved values
     *
     * Since game fps is 60, we assume that we will not "skip" squares
     * @param camera The Camera object to store current position
     */
    public void update(Camera camera) {
        for (int i = (int)camera.xPos - 1; i < (int)camera.xPos +2; i++) {
            for (int j = (int)camera.yPos - 1; j < (int)camera.yPos +2; j++) {
                if (i == (int) camera.xPos && j == (int) camera.yPos)
                    minimap[i][j] = -2;
                else
                    minimap[i][j] = map.at(i, j);
            }
        }
    }

    /**
     * Draw the minimap
     *
     * @param g The graphics object
     */
    public void draw(Graphics g, int width, int height) {
        g.setColor(Color.green);
        g.drawString("MINIMAP", width - (mapSize/2) - offsetX - 5, (int)(height - 1.1 * mapSize) + 2);
        g.drawRect((int) (width-mapSize - 1.1 * offsetX),
                (int)(height-mapSize - 1.1 * offsetY),
                (int)(mapSize +1.1* offsetX),
                (int)(mapSize + 1.1*offsetY));
        for (int i = 0; i < map.getLen(); i++) {
            for (int j = 0; j < map.getLen(); j++) {
                if (minimap[i][j] == -1)
                    g.setColor(Color.black);
                else if (minimap[i][j] == -2)
                    g.setColor(Color.yellow);
                else
                    g.setColor(Texture.colours[minimap[i][j]]);

                g.fillRect(width - mapSize + (i * mapSize)/ map.getLen() - offsetX,
                        height - mapSize + (j * mapSize) / map.getLen() - offsetY,
                        mapSize / map.getLen() + offsetX,
                        mapSize / map.getLen() + offsetY);
            }
        }
    }

    /**
     * Getter for two-index access
     * @param index row
     * @param jindex column
     * @return value at row, column
     */
    public int at(int index, int jindex) {return minimap[index][jindex];}

}
