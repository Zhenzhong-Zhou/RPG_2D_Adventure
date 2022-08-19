package main;

import audio.AudioManager;
import entities.Player;
import gui.GUI;
import input.KeyInputs;
import levels.LevelManager;
import objects.GameObject;
import utilities.AssetSetter;
import utilities.CollisionDetection;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

import static main.GameState.PLAY;
import static main.GameState.gameState;
import static utilities.Constants.AudioManager.START;
import static utilities.Constants.GameConstant.FPS_SET;
import static utilities.Constants.GameConstant.UPS_SET;
import static utilities.Constants.SceneConstant.*;

public class Scene extends JPanel implements Runnable {
    private final KeyInputs keyInputs = new KeyInputs(this);
    private final GameObject[] gameObject = new GameObject[10];
    private Thread thread;
    private Player player;
    private LevelManager levelManager;
    private CollisionDetection collisionDetection;
    private AudioManager audioManager;
    private GUI gui;
    private AssetSetter assetSetter;

    public Scene() {
        setFocusable(true);
        requestFocus();
        setSceneSize();
        initClasses();
    }

    private void initClasses() {
        // Input Class
        this.addKeyListener(keyInputs);

        // Entity Classes
        player = new Player(this, keyInputs);

        // Manager Class
        levelManager = new LevelManager();

        // Collision Detection
        collisionDetection = new CollisionDetection(this);

        // Audio Class
        audioManager = new AudioManager();

        // GUI Class
        gui = new GUI(this);

        // Setter
        assetSetter = new AssetSetter(this);
    }

    private void setSceneSize() {
        Dimension size = new Dimension(SCENE_WIDTH, SCENE_HEIGHT);
        setPreferredSize(size);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        System.out.println("Size: " + SCENE_WIDTH + ", " + SCENE_HEIGHT);
    }

    public void setupGame() {
        assetSetter.setObject();
        audioManager.playMusic(START);//TODO: change to MENU later
    }

    public void update() {
        switch(gameState) {
            case PLAY -> player.update();
            case PAUSE -> {
            }
        }
    }

    public void draw(Graphics2D graphics2D) {
        // MAP
        levelManager.draw(graphics2D, player);

        // OBJECTS
        for(GameObject object : gameObject) {
            if(object != null) {
                object.draw(graphics2D, this);
            }
        }

        // PLAYER
        player.draw(graphics2D);

        //GUI
        gui.draw(graphics2D);
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        // DEBUG
        long drawStart = 0;
        if(keyInputs.isDisplayDebugInfo()) drawStart = System.nanoTime();

        draw(graphics2D);

        // DEBUG
        if(keyInputs.isDisplayDebugInfo()) {
            // Render Time
            long drawEnd = System.nanoTime();
            long duration = drawEnd - drawStart;
            double second = (double) duration / 1000000000.0;
            DecimalFormat convert = new DecimalFormat();
            convert.setMaximumFractionDigits(8);

            // Display Info Position
            int x = 10;
            int y = 600;
            int lineHeight = 25;
            graphics2D.setFont(gui.getMaruMonica().deriveFont(Font.PLAIN, 25F));
            graphics2D.setColor(Color.WHITE);

            // Player Coordination
            graphics2D.drawString("World X: " + player.getWorldX(), x, y += lineHeight);
            graphics2D.drawString("World Y: " + player.getWorldY(), x, y += lineHeight);
            graphics2D.drawString("Column: " + (player.getWorldX() + player.getHitbox().x) / TILE_SIZE, x, y += lineHeight);
            graphics2D.drawString("Row: " + (player.getWorldY() + player.getHitbox().y) / TILE_SIZE, x, y += lineHeight);

            // Render Process Time
            graphics2D.drawString("Duration: " + convert.format(second) + " seconds", x, y + lineHeight);
        }

        graphics.dispose();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long lastTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaUpdates = 0;
        double deltaFrames = 0;

        while(thread != null) {
            long currentTime = System.nanoTime();
            deltaUpdates += (currentTime - lastTime) / timePerUpdate;
            deltaFrames += (currentTime - lastTime) / timePerFrame;

            lastTime = currentTime;
            // UPDATE: update information such as character positions
            if(deltaUpdates >= 1) {
                update();
                updates++;
                deltaUpdates--;
            }

            // DRAW: draw the screen with the updated information
            if(deltaFrames >= 1) {
                repaint();
                frames++;
                deltaFrames--;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost() {
        if(gameState == PLAY) {
            getPlayer().resetDirectionBoolean();
        }
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Player getPlayer() {
        return player;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public CollisionDetection getCollisionDetection() {
        return collisionDetection;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public GUI getGui() {
        return gui;
    }

    public GameObject[] getGameObject() {
        return gameObject;
    }
}
