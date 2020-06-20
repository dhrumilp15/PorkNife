import java.util.ArrayList;
import java.awt.Color;

/**
 * Main renderer for raycasting engine
 */
public class Screen {
    public Map map;
    public int width, height;
    public ArrayList<Texture> textures;

    /**
     * Creates a Screen object
     * @param m The Map object
     * @param tex The Texture arraylist
     * @param w The width of the window
     * @param h The height of the window
     */
    public Screen(Map m, ArrayList<Texture> tex, int w, int h) {
        map = m;
        textures = tex;
        width = w;
        height = h;
    }

    /**
     * Updates the screen based on camera position
     * Uses raycasting to solve for which pixels in the screen should represent a wall.
     * Distance to the object isn't necessarily used since we assume a light source that doesn't change with distance...
     * Lighting and shadows may come later... (There is some lighting with vertical walls to make them darker)
     * @param camera The Camera object
     * @param pixels The pixel array to change
     * @return The new pixel array
     */
    public int[] update(Camera camera, int[] pixels) {
        // sky
        for (int n = 0; n < pixels.length / 2; n++)
            if (pixels[n] != (new Color(135,206,235)).getRGB())
                pixels[n] = (new Color(135,206,235)).getRGB();
        //Floor
        for (int i = pixels.length / 2; i < pixels.length; i++)
            if (pixels[i] != Color.gray.getRGB())
                pixels[i] = Color.gray.getRGB();

        for (int x = 0; x < width; x++) {
            double cameraX = 2.0 * x / (double) (width) - 1.0; // normalized x-value of a vertical sliver of the screen

            // Calculate ray DIRECTION for vertical sliver --> not the length of the ray!
            double rayDirX = camera.xDir + camera.xPlane * cameraX;
            double rayDirY = camera.yDir + camera.yPlane * cameraX;

            //Map position
            int mapX = (int) camera.xPos;
            int mapY = (int) camera.yPos;

            //length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;

            //Unit distance for each direction based on ray vector
            double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            double perpWallDist;

            //Direction to go in x and y
            int stepX, stepY;
            boolean hit = false;//was a wall hit
            int side = 0;//was the wall vertical or horizontal
            //Figure out the step direction and initial distance to a side
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (camera.xPos - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }


            //Loop to find where the ray hits a wall
            while (!hit) {
                //Jump to next square
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
//                System.out.println("mapX: " + mapX + " mapY: " + mapY);
                //Check if ray has hit a wall                if (map.at(mapX, mapY) > 0) hit = true;
            }

            //Calculate distance to the point of impact
            if (side == 0)
                perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2.0) / rayDirX);
            else
                perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2.0) / rayDirY);
            //Now calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if (perpWallDist > 0) lineHeight = Math.abs((int) (height / perpWallDist));
            else lineHeight = height;
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + height / 2;
            if (drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight / 2 + height / 2;
            if (drawEnd >= height)
                drawEnd = height - 1;

            //add a texture
            int texNum = map.at(mapX, mapY) - 1;
            double wallX;//Exact position of where wall was hit
            if (side == 1) //If its a y-axis wall
                wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2.0) / rayDirY) * rayDirX);
            else //X-axis wall
                wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2.0) / rayDirX) * rayDirY);
            wallX -= Math.floor(wallX);
            //x coordinate on the texture
            int texX = (int) (wallX * (textures.get(texNum).SIZE));
            if ((side == 0 && rayDirX > 0) || (side == 1 && rayDirY < 0)) texX = textures.get(texNum).SIZE - texX - 1;

            //calculate y coordinate on texture
            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (((y * 2 - height + lineHeight) << 6) / lineHeight) / 2;
                int color;
                if (side == 0) color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
                else
                    color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)] >> 1) & 8355711;//Make y sides darker
                pixels[x + y * (width)] = color;
            }
        }
        return pixels;
    }
}
