package gui;

import entities.Entity;
import entities.Player;
import main.Scene;
import objects.Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.GameState.*;
import static utilities.Constants.SceneConstant.*;
import static utilities.LoadSave.*;

public class GUI {
    private final Scene scene;
    private final ArrayList<String> messages = new ArrayList<>();
    private final ArrayList<Integer> messageCounter = new ArrayList<>();
    private Graphics2D graphics2D;
    private Font maruMonica, purisaB;
    private boolean gameCompleted;
    private String currentDialogue = "";
    private int commandNum = 0;
    private BufferedImage heart_full, heart_half, heart_blank;
    private int slotCol = 0;
    private int slotRow = 0;
    private int subState = 0;

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
            case PAUSE -> {
                drawPlayerStatus();
                drawPauseScreen();
            }
            case DIALOGUE -> {
                drawPlayerStatus();
                drawDialogueOverlay();
            }
            case CHARACTER -> {
                drawCharacterScreen();
                drawInventory();
            }
            case OPTIONS -> drawOptionsScreen();
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
        // SUB WINDOW
        int frameX = TILE_SIZE * 7;
        int frameY = TILE_SIZE * 3;
        int frameWidth = TILE_SIZE * 10;
        int frameHeight = TILE_SIZE * 12;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState) {
            case 0 -> options_top(frameX, frameY);
            case 1 -> options_fullScreenNotification(frameX, frameY);
            case 2 -> options_control(frameX, frameY);
            case 3 -> options_endGameConfirmation(frameX, frameY);
        }
        scene.getKeyInputs().setEnterPressed(false);
    }

    private void options_top(int frameX, int frameY) {
        int textX;
        int textY;

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 40F));
        // TITLE
        String text = "Options";
        textX = getHorizonCenteredText(text);
        textY = frameY + TILE_SIZE;
        graphics2D.drawString(text, textX, textY + 10);

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 30F));
        // FULL SCREEN ON/OFF
        textX = frameX + TILE_SIZE;
        textY += TILE_SIZE * 2;
        int i = 0;
        drawState("Full Screen", textX, textY, i);
        i++;
        if(commandNum == 0 && scene.getKeyInputs().isEnterPressed()) {
            if(! scene.isFullScreen()) {
                scene.setFullScreen(true);
            } else if(scene.isFullScreen()) {
                scene.setFullScreen(false);
            }
            subState = 1;
        }

        // MUSIC
        textY += TILE_SIZE;
        drawState("Music", textX, textY, i);
        i++;

        // MUTE MUSIC
        textY += TILE_SIZE;
        drawState("Mute Music", textX, textY, i);
        i++;
        if(commandNum == 2 && scene.getKeyInputs().isEnterPressed()) {
            if(! scene.isMusic()) {
                scene.setMusic(true);
                scene.getAudioManager().toggleMusicMute();
            } else if(scene.isMusic()) {
                scene.setMusic(false);
                scene.getAudioManager().toggleMusicMute();
            }
        }

        // SE
        textY += TILE_SIZE;
        drawState("Sound Effect", textX, textY, i);
        i++;

        // MUTE SE
        textY += TILE_SIZE;
        drawState("Mute Sound Effect", textX, textY, i);
        i++;
        if(commandNum == 4 && scene.getKeyInputs().isEnterPressed()) {
            if(! scene.isSe()) {
                scene.setSe(true);
                scene.getAudioManager().toggleEffectMute();
            } else if(scene.isSe()) {
                scene.setSe(false);
                scene.getAudioManager().toggleEffectMute();
            }
        }

        // CONTROL
        textY += TILE_SIZE;
        drawState("Control", textX, textY, i);
        i++;
        if(commandNum == 5 && scene.getKeyInputs().isEnterPressed()) {
            subState = 2;
            commandNum = 0;
        }

        // END GAME
        textY += TILE_SIZE;
        drawState("End Game", textX, textY, i);
        i++;
        if(commandNum == 6 && scene.getKeyInputs().isEnterPressed()) {
            subState = 3;
            commandNum = 0;
        }

        // BACK
        textY += TILE_SIZE * 2;
        drawState("Back", textX, textY, i);
        i++;
        if(commandNum == 7 && scene.getKeyInputs().isEnterPressed()) {
            gameState = PLAY;
            commandNum = 0;
        }

        // FULL SCREEN CHECK BOX
        textX = frameX + (int) (TILE_SIZE * 6.5);
        textY = frameY + TILE_SIZE * 2 + TILE_SIZE / 2;
        drawCheckBox(textX, textY);
        if(scene.isFullScreen()) {
            graphics2D.fillRect(textX, textY, TILE_SIZE / 2, TILE_SIZE / 2);
        }

        // MUSIC VOLUME
        textY += TILE_SIZE;
        graphics2D.drawRect(textX, textY, 120, TILE_SIZE / 2);    // 120/5 =24
        int volumeWidth = 24 * scene.getAudioManager().getVolumeScale();
        graphics2D.fillRect(textX, textY, volumeWidth, TILE_SIZE / 2);

        // MUTE MUSIC CHECK BOX
        textY = frameY + TILE_SIZE * 4 + TILE_SIZE / 2;
        drawCheckBox(textX, textY);
        if(scene.isMusic()) {
            graphics2D.fillRect(textX, textY, TILE_SIZE / 2, TILE_SIZE / 2);
        }

        // SE VOLUME
        textY += TILE_SIZE;
        graphics2D.drawRect(textX, textY, 120, TILE_SIZE / 2);
        volumeWidth = 24 * scene.getAudioManager().getVolumeScale();
        graphics2D.fillRect(textX, textY, volumeWidth, TILE_SIZE / 2);

        // MUTE SE VOLUME CHECK BOX
        textY = frameY + TILE_SIZE * 6 + TILE_SIZE / 2;
        drawCheckBox(textX, textY);
        if(scene.isSe()) {
            graphics2D.fillRect(textX, textY, TILE_SIZE / 2, TILE_SIZE / 2);
        }
    }

    private void drawState(String state, int textX, int textY, int i) {
        graphics2D.setFont(maruMonica.deriveFont(Font.BOLD, 30F));
        graphics2D.drawString(state, textX, textY);
        if(commandNum == i) {
            graphics2D.drawString(">", textX - 25, textY);
        }
    }

    private void drawCheckBox(int textX, int textY) {
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(textX, textY, TILE_SIZE / 2, TILE_SIZE / 2);
    }

    private void options_fullScreenNotification(int frameX, int frameY) {
        int textX = frameX + TILE_SIZE;
        int textY = frameY + TILE_SIZE * 3;

        currentDialogue = "The change will take\neffect after restarting \nthe game.";
        for(String line : currentDialogue.split("\n")) {
            graphics2D.drawString(line, textX, textY);
            textY += 40;
        }

        // BACK
        textY = frameY + TILE_SIZE * 9;
        graphics2D.drawString("Back", textX, textY);
        if(commandNum == 0) {
            graphics2D.drawString(">", textX - 25, textY);
            if(scene.getKeyInputs().isEnterPressed()) {
                subState = 0;
            }
        }
    }

    private void options_control(int frameX, int frameY) {
        int textX;
        int textY;

        // TITLE
        String text = "Control";
        textX = getHorizonCenteredText(text);
        textY = frameY + TILE_SIZE;
        graphics2D.drawString(text, textX, textY + 10);

        textX = frameX + TILE_SIZE;
        textY += TILE_SIZE * 2;
        graphics2D.drawString("Move", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("Confirm/Attack", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("Shoot/Cast", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("Player Status", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("Pause", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("Options", textX, textY);
        textY += TILE_SIZE;

        textX = frameX + (int) (TILE_SIZE * 6.5);
        textY = frameY + TILE_SIZE * 3;
        graphics2D.drawString("WASD", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("ENTER", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("F", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("C", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("P", textX, textY);
        textY += TILE_SIZE;
        graphics2D.drawString("ESC", textX, textY);
        textY += TILE_SIZE;

        // BACK
        textX = frameX + TILE_SIZE;
        textY = frameY + TILE_SIZE * 9;
        drawState("Back", textX, textY, 0);
        if(scene.getKeyInputs().isEnterPressed()) {
            subState = 0;
            commandNum = 5;
        }
    }

    private void options_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + TILE_SIZE;
        int textY = frameY + TILE_SIZE * 3;

        currentDialogue = "Do you want to quite game \nand return to the main menu?";

        for(String line : currentDialogue.split("\n")) {
            graphics2D.drawString(line, textX, textY);
            textY += 40;
        }

        // YES
        String text = "Yes";
        textX = getHorizonCenteredText(text);
        textY += TILE_SIZE * 3;
        graphics2D.drawString(text, textX, textY);
        if(commandNum == 0) {
            graphics2D.drawString(">", textX - 25, textY);
            if(scene.getKeyInputs().isEnterPressed()) {
                subState = 0;
                gameState = MENU;
            }
        }

        // NO
        text = "No";
        textX = getHorizonCenteredText(text);
        textY += TILE_SIZE;
        graphics2D.drawString(text, textX, textY);
        if(commandNum == 1) {
            graphics2D.drawString(">", textX - 25, textY);
            if(scene.getKeyInputs().isEnterPressed()) {
                subState = 0;
                commandNum = 6;
            }
        }
    }

    private void drawPlayerStatus() {
        // MAX LIVES
        drawMaxLives();

        // CURRENT LIFE
        drawCurrentLife();
    }

    private void drawMessages() {
        int messageY = TILE_SIZE * 4;
        graphics2D.setFont(maruMonica.deriveFont(Font.BOLD, 25F));

        for(int i = 0; i < messages.size(); i++) {
            if(messages.get(i) != null) {
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(messages.get(i), TILE_SIZE + 2, messageY + 2);

                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString(messages.get(i), TILE_SIZE, messageY);
                int counter = messageCounter.get(i) + 1;    // messageCounter++
                messageCounter.set(i, counter);     // set the counter to the array
                messageY += 50;

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
        final int frameX = TILE_SIZE * 2;
        final int frameY = TILE_SIZE;
        final int frameWidth = TILE_SIZE * 6;
        final int frameHeight = TILE_SIZE * 11;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 32F));

        int textX = frameX + 20;
        int textY = frameY + TILE_SIZE;
        final int lineHeight = 40;

        // NAME
        graphics2D.drawString("Level", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Life", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Strength", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Attack", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Defense", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Exp", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Next Level", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Coin", textX, textY);
        textY += lineHeight + 5;
        graphics2D.drawString("Weapon", textX, textY);
        textY += lineHeight + 11;
        graphics2D.drawString("Shield", textX, textY);
        textY += lineHeight;

        // VALUES
        int tailX = (frameX + frameWidth) - 30;
        // Rest textY
        textY = frameY + TILE_SIZE;
        String value;

        Player player = scene.getPlayer();
        value = String.valueOf(player.getLevel());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = player.getLife() + "/" + player.getMaxLives();
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getStrength());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getDexterity());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getAttack());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getDefense());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getExp());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getNextLevelExp());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getCoin());
        textX = getHorizonForAlignToRightText(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        graphics2D.drawImage(scene.getPlayer().getCurrentWeapon().getDown1(), tailX - TILE_SIZE, textY - 25, null);
        textY += TILE_SIZE;

        graphics2D.drawImage(player.getCurrentShield().getDown1(), tailX - TILE_SIZE, textY - 25, null);
        textY += TILE_SIZE;
    }

    private void drawInventory() {
        // CREATE A FRAME
        int frameX = TILE_SIZE * 16;
        int frameY = TILE_SIZE;
        int frameWidth = TILE_SIZE * 6;
        int frameHeight = TILE_SIZE * 6;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = TILE_SIZE + 3;

        // DRAW PLAYER'S ITEMS
        for(int i = 0; i < scene.getPlayer().getInventory().size(); i++) {
            graphics2D.drawImage(scene.getPlayer().getInventory().get(i).getDown1(), slotX, slotY, null);
            slotX += slotSize;
            if(i == 4 || i == 9 || i == 14 || i == 19) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = TILE_SIZE;
        int cursorHeight = TILE_SIZE;
        // DRAW CURSOR
        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = TILE_SIZE * 3;
        // DRAW DESCRIPTION TEXT
        int textX = dFrameX + 20;
        int textY = dFrameY + TILE_SIZE;
        graphics2D.setFont(purisaB.deriveFont(Font.PLAIN, 23F));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int itemIndex = getItemIndexOnSlot();
        if(itemIndex < scene.getPlayer().getInventory().size()) {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            for(String line : scene.getPlayer().getInventory().get(itemIndex).getDescription().split("\n")) {
                graphics2D.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    private int getItemIndexOnSlot() {
        return slotCol + (slotRow * 5);
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

    public void slotColDecrease() {
        this.slotCol--;
    }

    public void slotColIncrease() {
        this.slotCol++;
    }

    public void slotRowDecrease() {
        this.slotRow--;
    }

    public void slotRowIncrease() {
        this.slotRow++;
    }

    public int getSlotCol() {
        return slotCol;
    }

    public int getSlotRow() {
        return slotRow;
    }

    public int getSubState() {
        return subState;
    }

    public void setSubState(int subState) {
        this.subState = subState;
    }
}
