package levels;

import ai.Node;
import entities.Player;
import main.Scene;
import tiles.TileManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static utilities.Constants.GameConstant.MAX_MAP;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.WorldConstant.*;
import static utilities.LoadSave.LEVEL_1;
import static utilities.LoadSave.LEVEL_2;

public class LevelManager {
    private final Scene scene;
    private final TileManager tileManager;
    private int[][][] level;
    private boolean drawPath = true;

    public LevelManager(Scene scene) {
        this.scene = scene;
        tileManager = new TileManager();
        loadDefaultLevel();
    }

    private void loadDefaultLevel() {
        level = new int[MAX_MAP][MAX_WORLD_COL][MAX_WORLD_ROW];
        loadMap(LEVEL_1, 0);
        loadMap(LEVEL_2, 1);
    }

    public void loadMap(String filepath, int map) {
        InputStream is = getClass().getResourceAsStream(filepath);
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int col = 0;
        int row = 0;

        try {
            while(col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {
                String line = br.readLine();
                while(col < MAX_WORLD_COL) {
                    String[] numbers = line.split("\t");
                    int num = Integer.parseInt(numbers[col]);
                    level[map][col][row] = num;
                    col++;
                }

                if(col == MAX_WORLD_COL) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D graphics2D, Player player) {
        int map = 0;
        int worldCol = 0;
        int worldRow = 0;

        while(map < MAX_MAP && worldCol < MAX_WORLD_COL && worldRow < MAX_WORLD_ROW) {
            int id = level[scene.currentMap][worldCol][worldRow];

            int worldX = worldCol * TILE_SIZE;
            int worldY = worldRow * TILE_SIZE;
            int playerWorldX = player.getWorldX();
            int playerWorldY = player.getWorldY();
            int playerScreenX = player.getScreenX();
            int playerScreenY = player.getScreenY();

            int screenX = worldX - playerWorldX + playerScreenX;
            int screenY = worldY - playerWorldY + playerScreenY;

            int left = playerWorldX - playerScreenX;
            int right = playerWorldX + playerScreenX;
            int up = playerWorldY - playerScreenY;
            int down = playerWorldY + playerScreenY;

            // Stop moving the camera at the edge of map
            if(playerScreenX > playerWorldX) {
                screenX = worldX;
            }
            if(playerScreenY > playerWorldY) {
                screenY = worldY;
            }

            int rightOffset = SCENE_WIDTH - playerScreenX;
            if(rightOffset > WORLD_WIDTH - playerWorldX) {
                screenX = SCENE_WIDTH - (WORLD_WIDTH - worldX);
            }

            int bottomOffset = SCENE_HEIGHT - playerScreenY;
            if(bottomOffset > WORLD_HEIGHT - playerWorldY) {
                screenY = SCENE_HEIGHT - (WORLD_HEIGHT - worldY);
            }

            if(worldX + TILE_SIZE > left && worldX - TILE_SIZE < right && worldY + TILE_SIZE > up && worldY - TILE_SIZE < down) {
                graphics2D.drawImage(tileManager.getTile(id), screenX, screenY, null);
            } else if(playerScreenX > playerWorldX ||       //TODO: need to fix later
                    playerScreenY > playerWorldY ||
                    rightOffset > WORLD_WIDTH - playerWorldX ||
                    bottomOffset > WORLD_HEIGHT - playerWorldY) {
                graphics2D.drawImage(tileManager.getTile(id), screenX, screenY, null);
            }
            worldCol++;

            if(worldCol == MAX_WORLD_COL) {
                worldCol = 0;
                worldRow++;
                if(worldRow == MAX_WORLD_ROW) {
                    worldRow = 0;
                    map++;
                }
            }
        }
        //TODO: Draw NPC pathfinding
        if(scene.getLevelManager().isDrawPath()) {
            ArrayList<Node> pathList = scene.getPathFinder().getPathList();
            graphics2D.setColor(new Color(255, 0, 0, 70));
            for(Node node : pathList) {
                int worldX = node.getCol() * TILE_SIZE;
                int worldY = node.getRow() * TILE_SIZE;
                int screenX = worldX - player.getWorldX() + player.getScreenX();
                int screenY = worldY - player.getWorldY() + player.getScreenY();

                graphics2D.fillRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public int[][][] getTileId() {
        return level;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public boolean isDrawPath() {
        return drawPath;
    }

    public void setDrawPath(boolean drawPath) {
        this.drawPath = drawPath;
    }
}
