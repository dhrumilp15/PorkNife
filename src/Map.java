import java.lang.Math;

public class Map {
    private int len = 15;
    public int[][] map;
    public int startx = 0;
    public int starty = 0;

    /**
     * Creates a Map object with a default length of 15
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
    public int at(int index, int jindex) {
        return map[index][jindex];
    }

    /**
     * Populates the map array, implementing:
     *
     * Walls along the edges
     * A given start surrounded by empty cells
     * A path from the top left cell to the bottom right cell
     */
    public void populate() {
        // Mark edges as walls
        for (int i = 0; i < map.length; i++) {
            if (i == 0 || i == map.length - 1) {
                for (int j = 0; j < map[i].length; j++) {
                    map[i][j] = (int) (4 * Math.random() + 1);
                }
            } else {
                map[i][0] = (int) (4 * Math.random() + 1);
                map[i][map[i].length - 1] = (int) (4 * Math.random() + 1);
                for (int j = 1; j < map[i].length - 1; j++) {
                    map[i][j] = (int) (5 * Math.random());
                }
            }
        }

        // ensure good starting position (margin of one square)
        startx = (int)(Math.random() * (len - 4) + 2);
        starty = (int)(Math.random() * (len - 4) + 2);
        for (int i = startx - 1; i < startx + 2; i++) {
            for (int j = starty -1; j < starty + 2; j++) {
                map[i][j] = 0;
            }
        }

        // binary maze algo - forces a path from top left to bottom right
        for (int i = 2; i < map.length - 2; i++) {
            for (int j = 2; j < map[i].length - 2; j++) {
                int choice = (int) (Math.random() * 2);
                if (choice == 0) {
                    map[i + 1][j] = 0;
                } else if (choice == 1) {
                    map[i][j+1] = 0;
                }
            }
        }
    }
}
