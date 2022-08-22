package gui;

import entities.Entity;
import entities.Player;
import main.Scene;
import objects.Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.GameState.gameState;
import static utilities.Constants.SceneConstant.*;
import static utilities.LoadSave.*;

public class GUI {
    private final Scene scene;
    private Graphics2D graphics2D;
    private Font maruMonica, purisaB;
    private boolean gameCompleted;
    private String currentDialogue = "";
    private int commandNum = 0;
    private BufferedImage heart_full, heart_half, heart_blank;
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<Integer> messageCounter = new ArrayList<>();

    public GUI(Scene scene) {
        this.scene = scene;

        initFont();
        getPlayerLifeImage();
    }

    private void initFont() {
        maruMonica = GetFont(MARU_MONICA);
        purisaB = GetFont(PURISA_BOLD);
    }

    private void getPlayerLifeImage() {
        Entity heart = new Heart(scene);
        heart_full = heart.getDown1();
        heart_half = heart.getDown2();
        heart_blank = heart.getLeft1();
    }

    public void addMessage(String text) {
       messages.add(text);
       messageCounter.add(0);
    }

    public void draw(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;

        graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 40F));
        graphics2D.setColor(Color.WHITE);

        switch(gameState) {
            case MENU -> drawMenuScreen();
            case PLAY -> {
                drawPlayerStatus();
                drawMessages();
            }
            case OPTIONS -> drawOptionsScreen();
            case PAUSE -> {
                drawPlayerStatus();
                drawPauseScreen();
            }
            case DIALOGUE -> {
                drawPlayerStatus();
                drawDialogueOverlay();
            }
            case CHARACTER -> {drawCharacterScreen();}
        }
    }

    private void drawMenuScreen() {
        // TITLE
        drawTitle();

        // BLUE BOY IMAGE
        drawCharacterImage();

        // MENU
        drawMenu("NEW GAME", 0);
        drawMenu("LOAD GAME", 1);
        drawMenu("OPTIONS", 2);
        drawMenu("QUIT", 3);
    }

    private void drawTitle() {
        graphics2D.setFont(maruMonica.deriveFont(Font.BOLD, 96F));
        String title = "Blue Boy Adventure";
        int x = getHorizonCenteredText(title);
        int y = 4 * TILE_SIZE;

        // SHADOW
        graphics2D.setColor(Color.GRAY);
        graphics2D.drawString(title, x + 5, y + 5);

        // MAIN COLOR
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(title, x, y);
    }

    private void drawCharacterImage() {
        int x = SCENE_WIDTH / 2 - (TILE_SIZE * 2) / 2;
        int y = 7 * TILE_SIZE;
        graphics2D.drawImage(scene.getPlayer().getDown1(), x, y, TILE_SIZE * 2, TILE_SIZE * 2, null);
    }

    private void drawMenu(String menu, int i) {
        graphics2D.setFont(maruMonica.deriveFont(Font.BOLD, 48F));
        int lineHeight = 60;
        int x = getHorizonCenteredText(menu);
        int y = (int) (10.5 * TILE_SIZE);
        graphics2D.drawString(menu, x, y + lineHeight * (i + 1));
        if(commandNum == i) {
            graphics2D.drawString(">", x - TILE_SIZE, y + lineHeight * (i + 1));
        }
    }

    //TODO: ADD later
    private void drawOptionsScreen() {

    }

    private void drawPlayerStatus() {
        // MAX LIVES
        drawMaxLives();

        // CURRENT LIFE
        drawCurrentLife();
    }

    private void drawMessages() {
        int messageY = TILE_SIZE*4;
        graphics2D.setFont(maruMonica.deriveFont(Font.BOLD, 25F));

        for(int i = 0; i < messages.size(); i++) {
            if(messages.get(i) != null) {
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(messages.get(i), TILE_SIZE+2, messageY+2);

                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString(messages.get(i), TILE_SIZE, messageY);
                int counter = messageCounter.get(i) + 1;    // messageCounter++
                messageCounter.set(i, counter);     // set the counter to the array
                messageY +=50;

                if(messageCounter.get(i) > 180) {
                    messages.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    private void drawMaxLives() {
        int x = TILE_SIZE / 2;
        int y = TILE_SIZE / 2;
        int i = 0;

        while(i < scene.getPlayer().getMaxLives() / 2) {
            graphics2D.drawImage(heart_blank, x, y, null);
            i++;
            x += TILE_SIZE;
        }
    }

    private void drawCurrentLife() {
        int x = TILE_SIZE / 2;
        int y = TILE_SIZE / 2;
        int i = 0;

        while(i < scene.getPlayer().getLife()) {
            graphics2D.drawImage(heart_half, x, y, null);
            i++;
            if(i < scene.getPlayer().getLife()) {
                graphics2D.drawImage(heart_full, x, y, null);
            }
            i++;
            x += TILE_SIZE;
        }
    }

    private void drawPauseScreen() {
        graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 80F));
        graphics2D.setColor(new Color(0, 0, 0, 150));
        graphics2D.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);
        graphics2D.setColor(Color.WHITE);
        String text = "PAUSED";
        int x = getHorizonCenteredText(text);
        int y = SCENE_HEIGHT / 2;

        graphics2D.drawString(text, x, y);
    }

    private void drawDialogueOverlay() {
        graphics2D.setFont(purisaB.deriveFont(Font.PLAIN, 23F));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // WINDOW
        int x = TILE_SIZE * 2;
        int y = TILE_SIZE / 2;
        int width = SCENE_WIDTH - (TILE_SIZE * 4);
        int height = TILE_SIZE * 5;
        drawSubWindow(x, y, width, height);

        x += TILE_SIZE;
        y += TILE_SIZE;

        for(String line : getCurrentDialogue().split("\n")) {
            graphics2D.drawString(line, x, y);
            y += 40;
        }
    }

    private void drawCharacterScreen() {
        // CREATE A FRAME
        final int frameX = TILE_SIZE *2;
        final int frameY = TILE_SIZE;
        final int frameWidth = TILE_SIZE *6;
        final int frameHeight = TILE_SIZE *11;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        // TEXT
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 32F));

        int textX = frameX +20;
        int textY = frameY +TILE_SIZE;
        final int lineHeight = 40;

        // NAME
        graphics2D.drawString("Level", textX, textY);
        textY+=lineHeight;
        graphics2D.drawString("Life", textX, textY);
        textY+=lineHeight;
        graphics2D.drawString("Strength", textX, textY);
        textY+=lineHeight;
        graphics2D.drawString("Dexterity", textX, textY);
        textY+=lineHeight;
        graphics2D.drawString("Attack", textX, textY);
        textY+=lineHeight;
        graphics2D.drawString("Defense", textX, textY);
        textY+=lineHeight;
        graphics2D.drawString("Exp", textX, textY);
        textY+=lineHeight;
        graphics2D.drawString("Next Level", textX, textY);
        textY+=lineHeight;
        graphics2D.drawString("Coin", textX, textY);
        textY+=lineHeight+5;
        graphics2D.drawString("Weapon", textX, textY);
        textY+=lineHeight+11;
        graphics2D.drawString("Shield", textX, textY);
        textY+=lineHeight;

        // VALUES
        int tailX  = (frameX + frameWidth) - 30;
        // Rest textY
        textY = frameY +TILE_SIZE;
        String value;

        Player player = scene.getPlayer();
        value = String.valueOf(player.getLevel());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        value = player.getLife() + "/" + player.getMaxLives();
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        value = String.valueOf(player.getStrength());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        value = String.valueOf(player.getDexterity());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        value = String.valueOf(player.getAttack());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        value = String.valueOf(player.getDefense());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        value = String.valueOf(player.getExp());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        value = String.valueOf(player.getNextLevelExp());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        value = String.valueOf(player.getCoin());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY+=lineHeight;

        graphics2D.drawImage(scene.getPlayer().getCurrentWeapon().getDown1(), tailX-TILE_SIZE, textY-25, null);
        textY+=TILE_SIZE;

        graphics2D.drawImage(player.getCurrentShield().getDown1(), tailX-TILE_SIZE, textY-25, null);
        textY+=TILE_SIZE;
    }

    private int getHorizonForAlignToRightText(String text, int tailX) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return tailX - length;
    }

    private int getHorizonCenteredText(String text) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return SCENE_WIDTH / 2 - length / 2;
    }

    private void drawSubWindow(int x, int y, int width, int height) {
        Color color = new Color(0, 0, 0, 210);
        graphics2D.setColor(color);
        graphics2D.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public Font getMaruMonica() {
        return maruMonica;
    }

    public Font getPurisaB() {
        return purisaB;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }

    public String getCurrentDialogue() {
        return currentDialogue;
    }

    public void setCurrentDialogue(String currentDialogue) {
        this.currentDialogue = currentDialogue;
    }

    public int getCommandNum() {
        return commandNum;
    }

    public void setCommandNum(int commandNum) {
        this.commandNum = commandNum;
    }

    public void decrementCommandNum() {
        this.commandNum--;
    }

    public void incrementCommandNum() {
        this.commandNum++;
    }
}
