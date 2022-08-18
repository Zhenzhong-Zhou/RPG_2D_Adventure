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

    // PLAYER LIFE
    public static final String HEART_FULL = "objects/heart_full.png";
    public static final String HEART_HALF = "objects/heart_half.png";
    public static final String HEART_BLANK = "objects/heart_blank.png";

    // NPC: Old Man
    public static final String OLD_MAN_UP_1_IMAGE = "npc/oldman_up_1.png";
    public static final String OLD_MAN_UP_2_IMAGE = "npc/oldman_up_2.png";
    public static final String OLD_MAN_LEFT_1_IMAGE = "npc/oldman_left_1.png";
    public static final String OLD_MAN_LEFT_2_IMAGE = "npc/oldman_left_2.png";
    public static final String OLD_MAN_DOWN_1_IMAGE = "npc/oldman_down_1.png";
    public static final String OLD_MAN_DOWN_2_IMAGE = "npc/oldman_down_2.png";
    public static final String OLD_MAN_RIGHT_1_IMAGE = "npc/oldman_right_1.png";
    public static final String OLD_MAN_RIGHT_2_IMAGE = "npc/oldman_right_2.png";


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
    public static final String EARTH_IMAGE = "tiles/earth.png";
    public static final String WALL_IMAGE = "tiles/wall.png";
    public static final String TREE_IMAGE = "tiles/tree.png";

    // OBJECTS
    public static final String KEY_IMAGE = "objects/key.png";
    public static final String DOOR_IMAGE = "objects/door.png";
    public static final String CHEST_IMAGE = "objects/chest.png";
    public static final String BOOT_IMAGE = "objects/boots.png";

    // FONTS
    public static final String MARU_MONICA = "fonts/x12y16pxMaruMonica.ttf";
    public static final String PURISA_BOLD = "fonts/Purisa Bold.ttf";

    // Level File Path Config
    public static String homePath = System.getProperty("user.home");
    public static String saveFolder = "The Legend of Zelda";
    public static String levelFile = "default_level.txt";
    public static String filePath = homePath + File.separator + saveFolder + File.separator + levelFile;
    private static final File dataFile = new File(filePath);

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            assert is != null;
            image = ImageIO.read(is);
            image = ScaleImage(image, TILE_SIZE, TILE_SIZE);
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
        URL url = LoadSave.class.getResource("/audios/" + filename + ".wav");
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

    public static void CreatedFolder() {
        File folder = new File(homePath + File.separator + saveFolder);
        if(! folder.exists()) {
            folder.mkdir();
        }
    }

    public static void CreateLevel(int[][] idArray) {
        if(dataFile.exists()) {
            System.out.println("File: " + dataFile + " is already exists.");
        } else {
            try {
                dataFile.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }

            WriteToFile(idArray);
        }
    }

    private static void WriteToFile(int[][] idArray) {
        try {
            PrintWriter printWriter = new PrintWriter(dataFile);
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

    public static void SaveLevel(int[][] idArray) {
        if(dataFile.exists()) {
            WriteToFile(idArray);
        } else {
            //TODO: new level
            System.out.println("File: " + dataFile + " is already exists.");
        }
    }

    private static int[][] ReadFromFile() {
        int[][] matrix = new int[MAX_WORLD_COL][MAX_WORLD_ROW];
        try {
            Scanner scanner = new Scanner(dataFile);
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

    public static int[][] GetLevelData() {
        if(dataFile.exists()) {
            return ReadFromFile();
        } else {
            System.out.println("File: " + dataFile + " does not exist!");
            return null;
        }
    }
}
