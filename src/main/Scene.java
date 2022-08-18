package main;

import input.KeyInputs;

import javax.swing.*;

import java.awt.*;
import java.text.DecimalFormat;

import static utilities.Constants.GameConstant.FPS_SET;
import static utilities.Constants.GameConstant.UPS_SET;
import static utilities.Constants.SceneConstant.SCENE_HEIGHT;
import static utilities.Constants.SceneConstant.SCENE_WIDTH;

public class Scene extends JPanel implements Runnable{
    private Thread thread;

    public Scene() {
        setFocusable(true);
        requestFocus();
        setSceneSize();
        initClasses();
    }

    private void initClasses() {
        // Input Class
        addKeyListener(new KeyInputs(this));
    }

    private void setSceneSize() {
        Dimension size = new Dimension(SCENE_WIDTH, SCENE_HEIGHT);
        setPreferredSize(size);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        System.out.println("Size: " + SCENE_WIDTH + ", " + SCENE_HEIGHT);
    }

    public void update() {}

    public void draw(Graphics2D graphics2D) {

    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        draw(graphics2D);
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
}
