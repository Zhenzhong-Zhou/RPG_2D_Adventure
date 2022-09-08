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
//        // Background Color
//        graphics2D.setColor(Color.BLACK);
//        graphics2D.fillRect(0,0,SCENE_WIDTH, SCENE_HEIGHT); //TODO: SCENE_WIDTH, SCENE_HEIGHT

        // Draw Map
//        int width = 800;    // 500
//        int height = 800;   // 500
//        int x = SCENE_WIDTH/2 - width/2;
//        int y = SCENE_HEIGHT/2-height/2;
        int x = 0;
        int y = 0;
        graphics2D.drawImage(worldMap[scene.currentMap], x,y, SCENE_WIDTH,SCENE_HEIGHT, null);

        // Draw Player
        drawPlayer(graphics2D, x, y);

        // Hint
        graphics2D.setFont(scene.getGui().getMaruMonica().deriveFont(Font.PLAIN, 32F));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Press M to close", (int) (18.5*TILE_SIZE), 17*TILE_SIZE);
    }

    public void drawMiniMap(Graphics2D graphics2D) {
        if(miniMapOn) {
            // Draw Map
            int width = 200;
            int height = 200;
            int x = SCENE_WIDTH - width -50;
            int y = 50;

            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            graphics2D.drawImage(worldMap[scene.currentMap],x,y,width,height,null);
        }
    }

    private void drawPlayer(Graphics2D graphics2D, int x, int y) {
        Player player = scene.getPlayer();
        double scale = (double) (MAX_WORLD_COL * TILE_SIZE) / SCENE_WIDTH;
        int playerX = (int) (x + player.getWorldX() / scale);
        int playerY = (int) (y + player.getWorldY() / scale);
        int playerSize = (int) (TILE_SIZE/scale);
        graphics2D.drawImage(player.getDown1(), playerX, playerY, playerSize, playerSize, null);
    }

    public boolean isMiniMapOn() {
        return miniMapOn;
    }

    public void setMiniMapOn(boolean miniMapOn) {
        this.miniMapOn = miniMapOn;
    }
}
