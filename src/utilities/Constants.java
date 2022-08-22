package utilities;

import static utilities.Constants.SceneConstant.TILE_SIZE;

public class Constants {
    /**
     * GAME SETTINGS
     */
    public static class GameConstant {
        public static final int FPS_SET = 120;
        public static final int UPS_SET = 200;
    }

    /**
     * SCENE SETTINGS
     */
    public static class SceneConstant {
        public static final int ORIGINAL_TILES_SIZE = 16;       // 16*16 tile
        public static final float SCALE = 3f;

        /*
            Actual Size
         */
        public static final int TILE_SIZE = (int) (ORIGINAL_TILES_SIZE * SCALE);    // 48*48 tile
        public static final int MAX_SCREEN_COL = 24;        // ratio 4:3
        public static final int MAX_SCREEN_ROW = 18;        // ratio 4:3
        public static final int SCENE_WIDTH = MAX_SCREEN_COL * TILE_SIZE;       // 1152 pixels
        public static final int SCENE_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;      // 864 pixels
    }

    /**
     * DIRECTIONS
     */
    public static class DirectionConstant {
        public static final String UP = "UP";
        public static final String LEFT = "LEFT";
        public static final String DOWN = "DOWN";
        public static final String RIGHT = "RIGHT";
        public static final String ANY = "ANY";
    }

    /**
     * WORLD SETTINGS
     */
    public static class WorldConstant {
        public static final int MAX_WORLD_COL = 50;
        public static final int MAX_WORLD_ROW = 50;
        public static final int WORLD_WIDTH = MAX_WORLD_COL * TILE_SIZE;
        public static final int WORLD_HEIGHT = MAX_WORLD_ROW * TILE_SIZE;
    }

    /**
     * OBJECT SETTINGS
     */
    public static class ObjectConstant {
        // Object Name
        public static final String KEY = "Key";
        public static final String DOOR = "Door";
        public static final String CHEST = "Chest";
        public static final String BOOT = "Boot";
        public static final String HEART = "HEART";
    }

    /**
     * MONSTER SETTINGS
     */
    public static class MonsterConstant {
        // Monster Name
        public static final String GREEN_SLIME = "Green Slime";
    }

    /**
     * EQUIPMENT SETTINGS
     */
    public static class EquipmentConstant {
        // Equipment Name
        public static final String NORMAL_SWORD = "NORMAL_SWORD";
        public static final String WOOD_SHIELD= "WOOD_SHIELD";
        public static final String BLUE_SHIELD = "BLUE_SHIELD";
    }

    /**
     * AUDIO SETTINGS
     */
    public static class AudioManager {
        // MUSICS
        public static final int MENU = 0;
        public static final int START = 1;

        // SOUND EFFECTS
        public static final int COIN = 0;
        public static final int POWER_UP = 1;
        public static final int UNLOCK = 2;
        public static final int FAN_FARE = 3;
        public static final int HIT_MONSTER = 4;
        public static final int RECEIVED_DAMAGE = 5;
        public static final int SWING_WEAPON = 6;
        public static final int LEVEL_UP = 7;
        public static final int CURSOR = 8;
    }
}
