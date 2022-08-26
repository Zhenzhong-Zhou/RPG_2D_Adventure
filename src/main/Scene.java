package main;

import audio.AudioManager;
import entities.Entity;
import entities.Player;
import events.EventManager;
import gui.GUI;
import gui.Options;
import input.KeyInputs;
import levels.LevelManager;
import tile_interactive.InteractiveTile;
import utilities.AssetSetter;
import utilities.CollisionDetection;
import utilities.Config;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

import static main.GameState.*;
import static utilities.Constants.GameConstant.FPS_SET;
import static utilities.Constants.GameConstant.UPS_SET;
import static utilities.Constants.SceneConstant.*;

public class Scene extends JPanel implements Runnable {
    private final KeyInputs keyInputs = new KeyInputs(this);
    private final Entity[] gameObjects = new Entity[50];
    private final Entity[] NPCs = new Entity[10];
    private final Entity[] monsters = new Entity[30];
    private final ArrayList<Entity> entityArrayList = new ArrayList<>();
    private final ArrayList<Entity> projectileArrayList = new ArrayList<>();
    private final InteractiveTile[] interactiveTiles = new InteractiveTile[100];
    private final ArrayList<Entity> particleArrayList = new ArrayList<>();
    private Thread thread;
    private Player player;
    private LevelManager levelManager;
    private EventManager eventManager;
    private CollisionDetection collisionDetection;
    private AudioManager audioManager;
    private GUI gui;
    private Options options;
    private AssetSetter assetSetter;
    private boolean fullScreen, musicMute, seMute;
    private Config config;
    public int currentMap;

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

        // Manager Classes
        levelManager = new LevelManager();
        eventManager = new EventManager(this);

        // Collision Detection
        collisionDetection = new CollisionDetection(this);

        // Audio Class
        audioManager = new AudioManager();

        // GUI Class
        gui = new GUI(this);
        options = new Options(this);

        // Setter Class
        assetSetter = new AssetSetter(this);

        // Config Class
        config = new Config(this);
    }

    private void setSceneSize() {
        Dimension size = new Dimension(SCENE_WIDTH, SCENE_HEIGHT);
        setPreferredSize(size);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        System.out.println("Size: " + SCENE_WIDTH + ", " + SCENE_HEIGHT);
    }

    public void setupGame() {
        assetSetter.setObjects();
        assetSetter.setNPCs();
        assetSetter.setMonsters();
        assetSetter.setInteractiveTile();
    }

    public void retry() {
        player.resetDefaultPositions();
        player.restoreLifeAndMana();
        assetSetter.setNPCs();
        assetSetter.setMonsters();
    }

    public void restart() {
        player.resetDefaultPositions();
        player.restoreLifeAndMana();
        player.selectItem();
        setupGame();
    }

    public void update() {
        switch(gameState) {
            case PLAY -> {
                // PLAYER
                player.update();

                // NPC
                for(Entity npc : NPCs) {
                    if(npc != null) {
                        npc.update();
                    }
                }

                // MONSTER
                for(int i = 0; i < monsters.length; i++) {
                    Entity monster = monsters[i];
                    if(monster != null) {
                        if(monster.isAlive() && ! monster.isDead()) {
                            monster.update();
                        }
                        if(! monster.isAlive()) {
                            monster.checkDrop();
                            monsters[i] = null;
                        }
                    }
                }

                // PROJECTILE
                arrayList(projectileArrayList);


                // PARTICLE
                arrayList(particleArrayList);

                // INTERACTIVE TILE
                for(Entity interactiveTile : interactiveTiles) {
                    if(interactiveTile != null) {
                        interactiveTile.update();
                    }
                }
            }
            case PAUSE -> {
            }
        }
    }

    private void arrayList(ArrayList<Entity> particleArrayList) {
        for(int i = 0; i < particleArrayList.size(); i++) {
            Entity particle = particleArrayList.get(i);
            if(particle != null) {
                if(particle.isAlive()) {
                    particle.update();
                }
                if(! particle.isAlive()) {
                    particleArrayList.remove(particle);
                }
            }
        }
    }

    public void draw(Graphics2D graphics2D) {
        if(gameState == MENU) {
            gui.draw(graphics2D);
        } else if(gameState == SETTINGS) {
            options.draw(graphics2D);
        } else {
            // MAP
            levelManager.draw(graphics2D, player);

            // INTERACTIVE TILE
            for(InteractiveTile interactiveTile : interactiveTiles) {
                if(interactiveTile != null) {
                    interactiveTile.draw(graphics2D);
                }
            }

            // ADD ENTITIES TO THE LIST
            entityArrayList.add(player);
            for(Entity npc : NPCs) {
                if(npc != null) {
                    entityArrayList.add(npc);
                }
            }

            for(Entity gameObject : gameObjects) {
                if(gameObject != null) {
                    entityArrayList.add(gameObject);
                }
            }

            for(Entity monster : monsters) {
                if(monster != null) {
                    entityArrayList.add(monster);
                }
            }

            for(Entity projectile : projectileArrayList) {
                if(projectile != null) {
                    entityArrayList.add(projectile);
                }
            }

            for(Entity particle : particleArrayList) {
                if(particle != null) {
                    entityArrayList.add(particle);
                }
            }

            // SORT
            entityArrayList.sort(Comparator.comparingInt(Entity :: getWorldY));

            // DRAW ENTITIES
            for(Entity entity : entityArrayList) {
                entity.draw(graphics2D);
            }

            // EMPTY ENTITY LIST
            entityArrayList.clear();

            //GUI
            gui.draw(graphics2D);
        }
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

    public KeyInputs getKeyInputs() {
        return keyInputs;
    }

    public Player getPlayer() {
        return player;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public EventManager getEventManager() {
        return eventManager;
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

    public Options getOptions() {
        return options;
    }

    public AssetSetter getAssetSetter() {
        return assetSetter;
    }

    public Config getConfig() {
        return config;
    }

    public Entity[] getGameObjects() {
        return gameObjects;
    }

    public Entity[] getNPCs() {
        return NPCs;
    }

    public Entity[] getMonsters() {
        return monsters;
    }

    public InteractiveTile[] getInteractiveTiles() {
        return interactiveTiles;
    }

    public ArrayList<Entity> getProjectileArrayList() {
        return projectileArrayList;
    }

    public ArrayList<Entity> getParticleArrayList() {
        return particleArrayList;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isMusicMute() {
        return musicMute;
    }

    public void setMusicMute(boolean musicMute) {
        this.musicMute = musicMute;
    }

    public boolean isSeMute() {
        return seMute;
    }

    public void setSeMute(boolean seMute) {
        this.seMute = seMute;
    }
}
