import java.lang.Math;

/**
 * To implement random map generation and Map utilities
 */
public class Map {
    private int len = 16;
    public int[][] map;
    public int startx = 0;
    public int starty = 0;

    /**
     * Creates a Map object with the default length
     */
    public Map() {
        map = new int[len][len];
        populate();
    }

    /**
     * Creates a Map object from a given square size
     * @param len Given square size
     */
    public Map(int len) {
        this.len = len;
        map = new int[len][len];
        populate();
    }

    /**
     * Getter for one-index access
     * @param index Row index
     * @return Row at index
     */
    public int[] at(int index) {
        return map[index];
    }

    /**
     * Gets the length of the map
     * @return The length
     */
    public int getLen() {
        return len;
    }

    /**
     * Getter for two-index access
     * @param index row
     * @param jindex column
     * @return value at row, column
     */
    public int at(int index, int jindex) {return map[index][jindex];}

    /**
     * Populates the map array, implementing:
     *
     * Walls along the edges
     * A given start surrounded by empty cells
     * A path from the top left cell to the bottom right cell
     */
    public void populate() {
        // Mark edges as walls
        for (int i = 0; i < len; i++) {
            if (i == 0 || i == len - 1) {
                for (int j = 0; j < len; j++) {
                    map[i][j] = (int) (4 * Math.random() + 1);
                }
            } else {
                map[i][0] = (int) (4 * Math.random() + 1);
                map[i][len - 1] = (int) (4 * Math.random() + 1);
                for (int j = 1; j < len-1; j++) {
                    map[i][j] = (int) (5 * Math.random());
                }
            }
        }

      for (int i = len - 3; i < len; i++) {
            for (int j = len - 3; j < len; j++) {
                if (i == len - 1 || j == len - 1)
                    map[i][j] = 5;
                else {
                    map[i][j] = 0;
                }
            }
        }


        // ensure good starting position (margin of one square)
        startx = (int)(Math.random() * 4 + 2);
        starty = (int)(Math.random() * 4 + 2);
        for (int i = startx - 1; i < startx + 2; i++) {
            for (int j = starty -1; j < starty + 2; j++) {
                map[i][j] = 0;
            }
        }
        // Add empty squares randomly with a probability of 50%
        for (int i = 2; i < map.length - 2; i++) {
            for (int j = 2; j < map[i].length - 2; j++) {
                int choice = (int) (Math.random() * 2);
                if (choice == 0) {
                    map[i][j] = 0;
                }
            }
        }
        // Force a path between top left corner and bottom right corner
        int[] pos = {1,1};
        map[pos[0]][pos[1]] = 0;
        while (pos[0] < len-1 && pos[1] < len-1) { // don't get too close to the wall
            int choice = (int) (Math.random() * 2);
            if (pos[0] == len-2) {
                for (int j = pos[1]; j < len - 2; j++)
                    map[pos[0]][j] = 0;
                break;
            }
            else if (pos[1] == len-2) {
                for (int i = pos[0]; i < len - 2; i++)
                    map[i][pos[1]] = 0;
                break;
            } else {
                if (choice == 0) {
                    map[pos[0] + 1][pos[1]] = 0;
                    pos[0] += 1;
                } else {
                    map[pos[0]][pos[1] + 1] = 0;
                    pos[1] += 1;
                }
            }
        }
    }
}
