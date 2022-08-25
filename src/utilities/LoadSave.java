package utilities;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Scanner;

import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.Constants.WorldConstant.MAX_WORLD_COL;
import static utilities.Constants.WorldConstant.MAX_WORLD_ROW;
import static utilities.Tool.ScaleImage;

public class LoadSave {
    // PLAYER WALK
    public static final String UP_1_IMAGE = "player/walk/boy_up_1.png";
    public static final String UP_2_IMAGE = "player/walk/boy_up_2.png";
    public static final String LEFT_1_IMAGE = "player/walk/boy_left_1.png";
    public static final String LEFT_2_IMAGE = "player/walk/boy_left_2.png";
    public static final String DOWN_1_IMAGE = "player/walk/boy_down_1.png";
    public static final String DOWN_2_IMAGE = "player/walk/boy_down_2.png";
    public static final String RIGHT_1_IMAGE = "player/walk/boy_right_1.png";
    public static final String RIGHT_2_IMAGE = "player/walk/boy_right_2.png";

    // PLAYER ATTACK: Sword
    public static final String ATTACK_UP_1_IMAGE = "player/attack/sword/boy_attack_up_1.png";
    public static final String ATTACK_UP_2_IMAGE = "player/attack/sword/boy_attack_up_2.png";
    public static final String ATTACK_LEFT_1_IMAGE = "player/attack/sword/boy_attack_left_1.png";
    public static final String ATTACK_LEFT_2_IMAGE = "player/attack/sword/boy_attack_left_2.png";
    public static final String ATTACK_DOWN_1_IMAGE = "player/attack/sword/boy_attack_down_1.png";
    public static final String ATTACK_DOWN_2_IMAGE = "player/attack/sword/boy_attack_down_2.png";
    public static final String ATTACK_RIGHT_1_IMAGE = "player/attack/sword/boy_attack_right_1.png";
    public static final String ATTACK_RIGHT_2_IMAGE = "player/attack/sword/boy_attack_right_2.png";

    // PLAYER ATTACK: Axe
    public static final String AXE_UP_1_IMAGE = "player/attack/axe/boy_axe_up_1.png";
    public static final String AXE_UP_2_IMAGE = "player/attack/axe/boy_axe_up_2.png";
    public static final String AXE_LEFT_1_IMAGE = "player/attack/axe/boy_axe_left_1.png";
    public static final String AXE_LEFT_2_IMAGE = "player/attack/axe/boy_axe_left_2.png";
    public static final String AXE_DOWN_1_IMAGE = "player/attack/axe/boy_axe_down_1.png";
    public static final String AXE_DOWN_2_IMAGE = "player/attack/axe/boy_axe_down_2.png";
    public static final String AXE_RIGHT_1_IMAGE = "player/attack/axe/boy_axe_right_1.png";
    public static final String AXE_RIGHT_2_IMAGE = "player/attack/axe/boy_axe_right_2.png";

    // PLAYER LIFE
    public static final String HEART_FULL = "objects/heart_full.png";
    public static final String HEART_HALF = "objects/heart_half.png";
    public static final String HEART_BLANK = "objects/heart_blank.png";

    // PLAYER MANNA
    public static final String MANA_FULL = "objects/manacrystal_full.png";
    public static final String MANA_BLANK = "objects/manacrystal_blank.png";

    // NPC: Old Man
    public static final String OLD_MAN_UP_1_IMAGE = "npc/oldman_up_1.png";
    public static final String OLD_MAN_UP_2_IMAGE = "npc/oldman_up_2.png";
    public static final String OLD_MAN_LEFT_1_IMAGE = "npc/oldman_left_1.png";
    public static final String OLD_MAN_LEFT_2_IMAGE = "npc/oldman_left_2.png";
    public static final String OLD_MAN_DOWN_1_IMAGE = "npc/oldman_down_1.png";
    public static final String OLD_MAN_DOWN_2_IMAGE = "npc/oldman_down_2.png";
    public static final String OLD_MAN_RIGHT_1_IMAGE = "npc/oldman_right_1.png";
    public static final String OLD_MAN_RIGHT_2_IMAGE = "npc/oldman_right_2.png";

    // PROJECTILE: Fireball
    public static final String FIREBALL_UP_1_IMAGE = "projectile/fireball_up_1.png";
    public static final String FIREBALL_UP_2_IMAGE = "projectile/fireball_up_2.png";
    public static final String FIREBALL_LEFT_1_IMAGE = "projectile/fireball_left_1.png";
    public static final String FIREBALL_LEFT_2_IMAGE = "projectile/fireball_left_2.png";
    public static final String FIREBALL_DOWN_1_IMAGE = "projectile/fireball_down_1.png";
    public static final String FIREBALL_DOWN_2_IMAGE = "projectile/fireball_down_2.png";
    public static final String FIREBALL_RIGHT_1_IMAGE = "projectile/fireball_right_1.png";
    public static final String FIREBALL_RIGHT_2_IMAGE = "projectile/fireball_right_2.png";

    // PROJECTILE: Rock
    public static final String ROCK_IMAGE = "projectile/rock_down_1.png";

    // MONSTER: Green Slime
    public static final String GREEN_SLIME_DOWN_1_IMAGE = "monster/greenslime_down_1.png";
    public static final String GREEN_SLIME_DOWN_2_IMAGE = "monster/greenslime_down_2.png";

    // TILES: Grass
    public static final String GRASS_00_IMAGE = "tile/grass/grass00.png";
    public static final String GRASS_01_IMAGE = "tile/grass/grass01.png";

    // TILES: Water
    public static final String WATER_00_IMAGE = "tile/water/water00.png";
    public static final String WATER_01_IMAGE = "tile/water/water01.png";
    public static final String WATER_02_IMAGE = "tile/water/water02.png";
    public static final String WATER_03_IMAGE = "tile/water/water03.png";
    public static final String WATER_04_IMAGE = "tile/water/water04.png";
    public static final String WATER_05_IMAGE = "tile/water/water05.png";
    public static final String WATER_06_IMAGE = "tile/water/water06.png";
    public static final String WATER_07_IMAGE = "tile/water/water07.png";
    public static final String WATER_08_IMAGE = "tile/water/water08.png";
    public static final String WATER_09_IMAGE = "tile/water/water09.png";
    public static final String WATER_10_IMAGE = "tile/water/water10.png";
    public static final String WATER_11_IMAGE = "tile/water/water11.png";
    public static final String WATER_12_IMAGE = "tile/water/water12.png";
    public static final String WATER_13_IMAGE = "tile/water/water13.png";

    // TILES: Road
    public static final String ROAD_00_IMAGE = "tile/road/road00.png";
    public static final String ROAD_01_IMAGE = "tile/road/road01.png";
    public static final String ROAD_02_IMAGE = "tile/road/road02.png";
    public static final String ROAD_03_IMAGE = "tile/road/road03.png";
    public static final String ROAD_04_IMAGE = "tile/road/road04.png";
    public static final String ROAD_05_IMAGE = "tile/road/road05.png";
    public static final String ROAD_06_IMAGE = "tile/road/road06.png";
    public static final String ROAD_07_IMAGE = "tile/road/road07.png";
    public static final String ROAD_08_IMAGE = "tile/road/road08.png";
    public static final String ROAD_09_IMAGE = "tile/road/road09.png";
    public static final String ROAD_10_IMAGE = "tile/road/road10.png";
    public static final String ROAD_11_IMAGE = "tile/road/road11.png";
    public static final String ROAD_12_IMAGE = "tile/road/road12.png";

    // TILES: Earth, Wall and Tree
    public static final String EARTH_IMAGE = "tile/earth.png";
    public static final String WALL_IMAGE = "tile/wall.png";
    public static final String TREE_IMAGE = "tile/tree.png";

    // INTERACTIVE TILE: Tree
    public static final String DRY_TREE_IMAGE = "tiles_interactive/drytree.png";
    public static final String TRUNK_IMAGE = "tiles_interactive/trunk.png";

    // OBJECTS
    public static final String KEY_IMAGE = "objects/key.png";
    public static final String DOOR_IMAGE = "objects/door.png";
    public static final String CHEST_IMAGE = "objects/chest.png";
    public static final String BOOT_IMAGE = "objects/boots.png";

    // EQUIPMENTS
    public static final String NORMAL_SWORD_IMAGE = "objects/sword_normal.png";
    public static final String AXE_IMAGE = "objects/axe.png";
    public static final String SHIELD_WOOD_IMAGE = "objects/shield_wood.png";
    public static final String SHIELD_BLUE_IMAGE = "objects/shield_blue.png";

    // CONSUMABLE
    public static final String RED_POTION_IMAGE = "objects/potion_red.png";

    // PICKUP
    public static final String COIN_IMAGE = "objects/coin_bronze.png";

    // FONTS
    public static final String MARU_MONICA = "fonts/x12y16pxMaruMonica.ttf";
    public static final String PURISA_BOLD = "fonts/Purisa Bold.ttf";

    // FILE NAME
    public static final String DEFAULT_LEVEL = "maps/default_level.txt";

    // Level File Path Config
    public static String homePath = System.getProperty("user.home");
    public static String saveFolder = "Blue Boy Adventure";
    public static String configFile = "config.txt";
    public static String filePath = homePath + File.separator + saveFolder + File.separator + configFile;
    public static final File dataFile = new File(filePath);

    public static void CreatedFolder() {
        File folder = new File(homePath + File.separator + saveFolder);
        if(! folder.exists()) {
            folder.mkdir();
        }
    }

    public static BufferedImage GetSpriteAtlas(String fileName) {
        return getImage(fileName, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage GetAttackImage(String fileName, int width, int height) {
        return getImage(fileName, width, height);
    }

    private static BufferedImage getImage(String fileName, int width, int height) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            assert is != null;
            image = ImageIO.read(is);
            image = ScaleImage(image, width, height);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public static Font GetFont(String fileName) {
        Font font = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            assert is != null;
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch(IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert is != null;
                is.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return font;
    }

    public static Clip GetClip(String filename) {
        URL url = LoadSave.class.getResource("/audio/" + filename + ".wav");
        AudioInputStream audio;
        try {
            assert url != null;
            audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private static File GetFile(String filename) {
        File file = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
        try {
            assert is != null;
            file = File.createTempFile(String.valueOf(is.hashCode()), ".tmp");
            file.deleteOnExit();

            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];

            int bytesRead;
            while((bytesRead = is.read(buffer)) != - 1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void CreateConfigFile(int[] array) {
        if(dataFile.exists()) {
            System.out.println("File: " + dataFile + " is already exists.");
        } else {
            try {
                dataFile.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
            WriteToConfigFile(array);
        }
    }

    private static void WriteToConfigFile(int[] id_Array) {
        try {
            PrintWriter printWriter = new PrintWriter(dataFile);
            for(Integer id : id_Array) {
                printWriter.println(id);
            }

            printWriter.close();
        } catch(FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public static void CreateLevel(String filename, int[][] idArray) {
        if(GetFile(filename).exists()) {
            System.out.println("File: " + GetFile(filename) + " is already exists.");
        } else {
            try {
                GetFile(filename).createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }

            WriteToFile(GetFile(filename), idArray);
        }
    }

    private static void WriteToFile(File file, int[][] idArray) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for(int y = 0; y < idArray.length; y++) {
                for(int x = 0; x < idArray[y].length; x++) {
                    if(x < idArray[y].length) {
                        printWriter.print(idArray[x][y] + "\t");
                    }
                }
                printWriter.append("\n");
            }
            printWriter.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void SaveLevel(String filename, int[][] idArray) {
        if(GetFile(filename).exists()) {
            WriteToFile(GetFile(filename), idArray);
        } else {
            //TODO: new level
            System.out.println("File: " + GetFile(filename) + " is already exists.");
        }
    }

    private static int[][] ReadFromFile(File file) {
        int[][] matrix = new int[MAX_WORLD_COL][MAX_WORLD_ROW];
        try {
            Scanner scanner = new Scanner(file);
            int col = 0;
            int row = 0;
            while(col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {
                String line = scanner.nextLine();
                while(col < MAX_WORLD_COL) {
                    String[] numbers = line.split("\t");
                    int num = Integer.parseInt(numbers[col]);
                    matrix[col][row] = num;
                    col++;
                }
                if(col == MAX_WORLD_COL) {
                    col = 0;
                    row++;
                }
            }
            scanner.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public static int[][] GetLevelData(String filename) {
        if(GetFile(filename).exists()) {
            return ReadFromFile(GetFile(filename));
        } else {
            System.out.println("File: " + GetFile(filename) + " does not exist!");
            return null;
        }
    }
}
