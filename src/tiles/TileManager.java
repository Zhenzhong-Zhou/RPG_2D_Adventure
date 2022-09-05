package tiles;

import java.awt.image.BufferedImage;

import static utilities.LoadSave.*;
import static utilities.LoadSave.GetSpriteAtlas;

public class TileManager {
    private Tile[] tiles;

    public TileManager() {
        tiles = new Tile[50];
        getTileImage();
    }

    private void getTileImage() {
        int i = 0;
        // GRASS
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_00_IMAGE, false); i++;
        setup(i, GRASS_01_IMAGE, false); i++;

        // WATER
        setup(i, WATER_00_IMAGE, true); i++;
        setup(i, WATER_01_IMAGE, true); i++;
        setup(i, WATER_02_IMAGE, true); i++;
        setup(i, WATER_03_IMAGE, true); i++;
        setup(i, WATER_04_IMAGE, true); i++;
        setup(i, WATER_05_IMAGE, true); i++;
        setup(i, WATER_06_IMAGE, true); i++;
        setup(i, WATER_07_IMAGE, true); i++;
        setup(i, WATER_08_IMAGE, true); i++;
        setup(i, WATER_09_IMAGE, true); i++;
        setup(i, WATER_10_IMAGE, true); i++;
        setup(i, WATER_11_IMAGE, true); i++;
        setup(i, WATER_12_IMAGE, true); i++;
        setup(i, WATER_13_IMAGE, true); i++;

        // ROAD
        setup(i, ROAD_00_IMAGE, false); i++;
        setup(i, ROAD_01_IMAGE, false); i++;
        setup(i, ROAD_02_IMAGE, false); i++;
        setup(i, ROAD_03_IMAGE, false); i++;
        setup(i, ROAD_04_IMAGE, false); i++;
        setup(i, ROAD_05_IMAGE, false); i++;
        setup(i, ROAD_06_IMAGE, false); i++;
        setup(i, ROAD_07_IMAGE, false); i++;
        setup(i, ROAD_08_IMAGE, false); i++;
        setup(i, ROAD_09_IMAGE, false); i++;
        setup(i, ROAD_10_IMAGE, false); i++;
        setup(i, ROAD_11_IMAGE, false); i++;
        setup(i, ROAD_12_IMAGE, false); i++;

        // EARTH, WALL and TREE
        setup(i, EARTH_IMAGE, false); i++;
        setup(i, WALL_IMAGE, true); i++;
        setup(i, TREE_IMAGE, true); i++;
        setup(i, HUT_IMAGE, false); i++;
        setup(i, FLOOR_IMAGE, false); i++;
        setup(i, TABLE_IMAGE, true);
    }

    private void setup(int index, String imageName, boolean collision ) {
        tiles[index] = new Tile();
        tiles[index].setSprite(GetSpriteAtlas(imageName));
        tiles[index].setCollision(collision);
    }

    public BufferedImage getTile(int id) {
        return tiles[id].getSprite();
    }

    public Tile[] getTiles() {
        return tiles;
    }
}
