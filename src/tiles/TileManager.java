package tiles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilities.LoadSave.*;

public class TileManager {
    private final ArrayList<Tile> tiles;

    public TileManager() {
        tiles = new ArrayList<>();
        getTileImage();
    }

    private void getTileImage() {
        // GRASS
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(GRASS_01_IMAGE), false));

        // WATER
        tiles.add(new Tile(GetSpriteAtlas(WATER_00_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_01_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_02_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_03_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_04_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_05_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_06_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_07_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_08_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_09_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_10_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_11_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_12_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(WATER_13_IMAGE), true));

        // ROAD
        tiles.add(new Tile(GetSpriteAtlas(ROAD_00_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_01_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_02_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_03_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_04_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_05_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_06_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_07_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_08_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_09_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_10_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_11_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(ROAD_12_IMAGE), false));

        // EARTH, WALL and TREE
        tiles.add(new Tile(GetSpriteAtlas(EARTH_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(WALL_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(TREE_IMAGE), true));
        tiles.add(new Tile(GetSpriteAtlas(HUT_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(FLOOR_IMAGE), false));
        tiles.add(new Tile(GetSpriteAtlas(TABLE_IMAGE), true));
    }

    public BufferedImage getTile(int id) {
        return tiles.get(id).getSprite();
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }
}
