package levels;

import entities.Player;
import main.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilities.Constants.GameConstant.MAX_MAP;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.WorldConstant.MAX_WORLD_COL;
import static utilities.Constants.WorldConstant.MAX_WORLD_ROW;

public class Map extends LevelManager {
    private BufferedImage[] worldMap;
    private boolean miniMapOn;

    public Map(Scene scene) {
        super(scene);

        createWorldMap();
    }

    private void createWorldMap() {
        worldMap = new BufferedImage[MAX_MAP];
        int worldMapWidth = TILE_SIZE * MAX_WORLD_COL;
        int worldMapHeight = TILE_SIZE * MAX_WORLD_ROW;

        for(int i=0; i<MAX_MAP; i++) {
            worldMap[i] = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = worldMap[i].createGraphics();

            int col = 0;
            int row = 0;

            while(col<MAX_WORLD_COL && row < MAX_WORLD_ROW) {
                int tileId = level[i][col][row];
                int x = col *TILE_SIZE;
                int y = row*TILE_SIZE;

                graphics2D.drawImage(tileManager.getTiles()[tileId].getSprite(), x,y,null);
                col++;
                if(col == MAX_WORLD_COL) {
                    col = 0;
                    row++;
                }
            }

            graphics2D.dispose();
        }
    }

    public void drawFullMapScreen(Graphics2D graphics2D) {
        // Draw Map
        graphics2D.drawImage(worldMap[scene.currentMap], 0, 0, SCENE_WIDTH, SCENE_HEIGHT, null);

        // Draw Player


        // Hint
        graphics2D.setFont(scene.getGui().getMaruMonica().deriveFont(Font.PLAIN, 32F));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Press M to close", (int) (18.5*TILE_SIZE), 17*TILE_SIZE);
    }

    public void drawMiniMap(Graphics2D graphics2D) {
        if(miniMapOn) {
            // Draw Map
            int width = 250;
            int height = 250;
            int x = SCENE_WIDTH - width - 50;
            int y = 50;

            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            graphics2D.drawImage(worldMap[scene.currentMap],x,y,width,height,null);

            // Draw Player
            drawMiniPlayer(graphics2D, x,y);
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    private void drawMiniPlayer(Graphics2D graphics2D, int x, int y) {
        Player player = scene.getPlayer();
        int playerX = x + player.getWorldX()/ 10;
        int playerY = y + player.getWorldY()/ 10;
        int playerSize = TILE_SIZE/3;
        graphics2D.drawImage(player.getDown1(), playerX, playerY, playerSize, playerSize, null);
    }

    public boolean isMiniMapOn() {
        return miniMapOn;
    }

    public void setMiniMapOn(boolean miniMapOn) {
        this.miniMapOn = miniMapOn;
    }
}
