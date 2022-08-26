package utilities;

import static utilities.Constants.SceneConstant.TILE_SIZE;

public class Constants {
    /**
     * GAME SETTINGS
     */
    public static class GameConstant {
        public static final int FPS_SET = 120;
        public static final int UPS_SET = 60;
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
     * ENTITY SETTINGS
     */
    public static class EntityConstant {
        // Entity Type
        public static final int PLAYER = 0;
        public static final int NPC = 1;
        public static final int MONSTER = 2;
        public static final int SWORD = 3;
        public static final int AXE = 4;
        public static final int SHIELD = 5;
        public static final int CONSUMABLE = 6;
        public static final int PICKUP = 7;

        // Weapon Name
        public static final String NORMAL_SWORD = "Normal Sword";
        public static final String WOODCUTTER_AXE = "Woodcutter's Axe";

        // Shield Name
        public static final String WOOD_SHIELD = "Wood Shield";
        public static final String BLUE_SHIELD = "Blue Shield";

        // Monster Name
        public static final String GREEN_SLIME = "Green Slime";

        // Consumable Name
        public static final String KEY = "Key";
        public static final String DOOR = "Door";
        public static final String CHEST = "Chest";
        public static final String BOOT = "Boot";
        public static final String HEART = "HEART";
        public static final String MANA = "Mana Crystal";
        public static final String POTION_RED = "Red Potion";

        // Pickup Name
        public static final String BRONZE_COIN = "Bronze Coin";

        // Projectile Name
        public static final String FIREBALL = "Fireball";
        public static final String ROCK = "Rock";
    }

    /**
     * AUDIO SETTINGS
     */
    public static class AudioManager {
        // MUSICS
        public static final int MAIN_MENU = 0;
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
        public static final int BURNING = 9;
        public static final int CUT_TREE = 10;
        public static final int DIE = 11;
        public static final int GAME_OVER = 12;

        // OPTIONS CONTROL
        public static final String MUTE_ON = "-1";
        public static final String MUTE_OFF = "0";
    }
}
