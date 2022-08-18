package main;

import entities.Player;
import input.KeyInputs;
import levels.LevelManager;
import objects.GameObject;
import utilities.AssetSetter;
import utilities.CollisionDetection;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

import static utilities.Constants.GameConstant.FPS_SET;
import static utilities.Constants.GameConstant.UPS_SET;
import static utilities.Constants.SceneConstant.*;

public class Scene extends JPanel implements Runnable {
    private final KeyInputs keyInputs = new KeyInputs(this);
    private Thread thread;
    private Player player;
    private LevelManager levelManager;
    private CollisionDetection collisionDetection;
    private AssetSetter assetSetter;
    private final GameObject[] gameObject = new GameObject[10];

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
    }

    public void update() {
        player.update();
    }

    public void draw(Graphics2D graphics2D) {
        levelManager.draw(graphics2D, player);
        for(int i = 0; i < gameObject.length; i++) {
            if(gameObject[i] != null) {
                gameObject[i].draw(graphics2D, this);
            }
        }
        player.draw(graphics2D);
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
            int lineHeight = 20;
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

    public Player getPlayer() {
        return player;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public CollisionDetection getCollisionDetection() {
        return collisionDetection;
    }

    public GameObject[] getGameObject() {
        return gameObject;
    }
}
