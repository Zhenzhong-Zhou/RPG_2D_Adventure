package gui;

import entities.Entity;
import entities.Player;
import environment.Lighting;
import main.Scene;
import objects.Coin_Bronze;
import objects.Heart;
import objects.ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static main.GameState.*;
import static utilities.Constants.AudioManager.MAIN_MENU;
import static utilities.Constants.AudioManager.START;
import static utilities.Constants.EnvironmentConstant.DAY;
import static utilities.Constants.SceneConstant.*;
import static utilities.LoadSave.*;

public class GUI {
    private final Scene scene;
    private final ArrayList<String> messages = new ArrayList<>();
    private final ArrayList<Integer> messageCounter = new ArrayList<>();
    private Graphics2D graphics2D;
    private Font maruMonica, purisaB;
    private String currentDialogue = "";
    private int commandNum = 0;
    private BufferedImage heart_full, heart_half, heart_blank, manna_full, manna_blank, coin;
    private int playerSlotCol = 0;
    private int playerSlotRow = 0;
    private int npcSlotCol = 0;
    private int npcSlotRow = 0;
    private int subState = 0;
    private int transitionCounter;
    private Entity npc;

    public GUI(Scene scene) {
        this.scene = scene;

        initFont();
        getPlayerLifeImage();
        getPlayerMannaImage();
        getCoinImage();
        createDefaultLevel();
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

    private void getPlayerMannaImage() {
        Entity manaCrystal = new ManaCrystal(scene);
        manna_full = manaCrystal.getDown1();
        manna_blank = manaCrystal.getDown2();
    }

    private void getCoinImage() {
        Entity bronzeCoin = new Coin_Bronze(scene);
        coin = bronzeCoin.getDown1();
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
            case DIALOGUE -> drawDialogueOverlay();
            case CHARACTER -> {
                drawCharacterScreen();
                drawInventory(scene.getPlayer(), true);
            }
            case OPTIONS -> drawOptionsScreen();
            case DEAD -> drawDeadScreen();
            case TRANSITION -> drawTransition();
            case TRADE -> drawTradeScreen();
            case SLEEP -> drawSleepScreen();
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
            if(! scene.isMusicMute()) {
                scene.setMusicMute(true);
                scene.getAudioManager().toggleMusicMute();
            } else if(scene.isMusicMute()) {
                scene.setMusicMute(false);
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
            if(! scene.isSeMute()) {
                scene.setSeMute(true);
                scene.getAudioManager().toggleEffectMute();
            } else if(scene.isSeMute()) {
                scene.setSeMute(false);
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
        int volumeWidth = 24 * scene.getAudioManager().getVolumeBGMScale();
        graphics2D.fillRect(textX, textY, volumeWidth, TILE_SIZE / 2);

        // MUTE MUSIC CHECK BOX
        textY = frameY + TILE_SIZE * 4 + TILE_SIZE / 2;
        drawCheckBox(textX, textY);
        if(scene.isMusicMute()) {
            graphics2D.fillRect(textX, textY, TILE_SIZE / 2, TILE_SIZE / 2);
        }

        // SE VOLUME
        textY += TILE_SIZE;
        graphics2D.drawRect(textX, textY, 120, TILE_SIZE / 2);
        volumeWidth = 24 * scene.getAudioManager().getVolumeSEScale();
        graphics2D.fillRect(textX, textY, volumeWidth, TILE_SIZE / 2);

        // MUTE SE VOLUME CHECK BOX
        textY = frameY + TILE_SIZE * 6 + TILE_SIZE / 2;
        drawCheckBox(textX, textY);
        if(scene.isSeMute()) {
            graphics2D.fillRect(textX, textY, TILE_SIZE / 2, TILE_SIZE / 2);
        }

        scene.getConfig().saveConfig();
    }

    private void createDefaultLevel() {
        int[] array = new int[4];
        Arrays.fill(array, 3);
        CreateConfigFile(array);
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

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 40F));
        // TITLE
        String text = "Control";
        textX = getHorizonCenteredText(text);
        textY = frameY + TILE_SIZE;
        graphics2D.drawString(text, textX, textY + 10);

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(maruMonica.deriveFont(Font.BOLD, 30F));
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

        // BACK
        textX = frameX + TILE_SIZE;
        textY = frameY + TILE_SIZE * 11;
        graphics2D.drawString("Back", textX, textY);
        graphics2D.drawString(">", textX - 25, textY);
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
                scene.getAudioManager().playMusic(MAIN_MENU);
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

        // MAX MANA
        drawMaxMana();

        // CURRENT MANA
        drawCurrentMna();
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

    private void drawMaxMana() {
        int x = (TILE_SIZE / 2) - 5;
        int y = (int) (TILE_SIZE * 1.5);
        int i = 0;

        while(i < scene.getPlayer().getMaxMana()) {
            graphics2D.drawImage(manna_blank, x, y, null);
            i++;
            x += 35;
        }
    }

    private void drawCurrentMna() {
        int x = (TILE_SIZE / 2) - 5;
        int y = (int) (TILE_SIZE * 1.5);
        int i = 0;

        while(i < scene.getPlayer().getMana()) {
            graphics2D.drawImage(manna_full, x, y, null);
            i++;
            x += 35;
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
        int x = TILE_SIZE * 4;
        int y = TILE_SIZE / 2;
        int width = SCENE_WIDTH - (TILE_SIZE * 8);
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
        final int frameY = TILE_SIZE * 3;
        final int frameWidth = TILE_SIZE * 6;
        final int frameHeight = TILE_SIZE * 11 + 5;
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
        graphics2D.drawString("Mana", textX, textY);
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

        value = player.getMana() + "/" + player.getMaxMana();
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
    }

    private void drawInventory(Entity entity, boolean cursor) {
        int frameX;
        int frameY;
        int frameWidth;
        int frameHeight;
        int slotCol;
        int slotRow;

        if(entity == scene.getPlayer()) {
            frameX = TILE_SIZE * 16;
            frameY = TILE_SIZE * 3;
            frameWidth = TILE_SIZE * 6;
            frameHeight = TILE_SIZE * 6;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else {
            frameX = TILE_SIZE * 2;
            frameY = TILE_SIZE * 3;
            frameWidth = TILE_SIZE * 6;
            frameHeight = TILE_SIZE * 6;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }
        // CREATE A FRAME
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        ArrayList<Entity> inventory = entity.getInventory();

        // SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = TILE_SIZE + 3;

        // DRAW PLAYER'S ITEMS
        for(int i = 0; i < inventory.size(); i++) {
            // EQUIP CURSOR
            if(inventory.get(i) == entity.getCurrentWeapon() ||
                    inventory.get(i) == entity.getCurrentShield() ||
                    inventory.get(i) == entity.getCurrentLight()) {
                graphics2D.setColor(new Color(240, 190, 90));
                graphics2D.fillRoundRect(slotX, slotY, TILE_SIZE, TILE_SIZE, 10, 10);
            }

            graphics2D.drawImage(inventory.get(i).getDown1(), slotX, slotY, null);

            // DISPLAY AMOUNT
            if(entity == scene.getPlayer() && inventory.get(i).getAmount() > 1) {
                graphics2D.setFont(maruMonica.deriveFont(32F));
                int amountX;
                int amountY;

                String text = "" + inventory.get(i).getAmount();
                amountX = getHorizonForAlignToRightText(text, slotX + 44);
                amountY = slotY + TILE_SIZE;

                // SHADOW
                graphics2D.setColor(new Color(60, 60, 60));
                graphics2D.drawString(text, amountX, amountY);
                // NUMBER
                graphics2D.setColor(Color.WHITE);
                graphics2D.drawString(text, amountX - 3, amountY - 3);
            }

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14 || i == 19) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // CURSOR
        if(cursor) {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            // DRAW CURSOR
            graphics2D.setColor(Color.WHITE);
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.drawRoundRect(cursorX, cursorY, TILE_SIZE, TILE_SIZE, 10, 10);

            // DESCRIPTION FRAME
            int dFrameY = frameY + frameHeight;
            int dFrameHeight = TILE_SIZE * 3;
            // DRAW DESCRIPTION TEXT
            int textX = frameX + 20;
            int textY = dFrameY + TILE_SIZE;
            graphics2D.setFont(purisaB.deriveFont(Font.PLAIN, 20F));
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            if(itemIndex < inventory.size()) {
                drawSubWindow(frameX, dFrameY, frameWidth, dFrameHeight);
                for(String line : inventory.get(itemIndex).getDescription().split("\n")) {
                    graphics2D.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }
    }

    private void drawDeadScreen() {
        graphics2D.setColor(new Color(0, 0, 0, 150));
        graphics2D.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        int x;
        int y;
        String text;
        graphics2D.setFont(maruMonica.deriveFont(Font.BOLD, 110F));

        text = "YOU DEAD!";
        // Shadow
        graphics2D.setColor(Color.BLACK);
        x = getHorizonCenteredText(text);
        y = TILE_SIZE * 6;
        graphics2D.drawString(text, x, y);
        // Main
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(text, x - 4, y - 4);

        // Retry
        text = "Retry";
        drawMenu(text, 0);

        // Back to Main Menu
        text = "Menu";
        drawMenu(text, 1);
    }

    private void drawTransition() {
        transitionCounter++;
        graphics2D.setColor(new Color(0, 0, 0, transitionCounter * 5));
        graphics2D.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        if(transitionCounter == 50) {
            transitionCounter = 0;
            gameState = PLAY;
            scene.currentMap = scene.getEventManager().getTempMap();
            scene.getPlayer().setWorldX(TILE_SIZE * scene.getEventManager().getTempCol());
            scene.getPlayer().setWorldY(TILE_SIZE * scene.getEventManager().getTempRow());
            scene.getEventManager().setPreviousEventX(scene.getPlayer().getWorldX());
            scene.getEventManager().setPreviousEventY(scene.getPlayer().getWorldY());
        }
    }

    private void drawTradeScreen() {
        switch(subState) {
            case 0 -> trade_select();
            case 1 -> trade_buy();
            case 2 -> trade_sell();
        }
        scene.getKeyInputs().setEnterPressed(false);
    }

    private void trade_select() {
        drawDialogueOverlay();

        // DRAW WINDOW
        int x = TILE_SIZE * 17;
        int y = TILE_SIZE * 6;
        int width = (int) (TILE_SIZE * 3.5);
        int height = (int) (TILE_SIZE * 3.5);
        drawSubWindow(x, y, width, height);

        // DRAW TEXTS
        x += TILE_SIZE;
        y += TILE_SIZE;

        drawState("Buy", x, y, 0);
        if(scene.getKeyInputs().isEnterPressed() && commandNum == 0) subState = 1;
        y += TILE_SIZE;

        drawState("Sell", x, y, 1);
        if(scene.getKeyInputs().isEnterPressed() && commandNum == 1) subState = 2;
        y += TILE_SIZE;

        drawState("Leave", x, y, 2);
        if(scene.getKeyInputs().isEnterPressed() && commandNum == 2) {
            subState = 0;
            commandNum = 0;
            gameState = DIALOGUE;
            currentDialogue = "Come again, hehe!";
        }
    }

    private void trade_buy() {
        // DRAW PLAYER INVENTORY
        drawInventory(scene.getPlayer(), false);
        // DRAW MERCHANT INVENTORY
        drawInventory(npc, true);

        int x;
        int y;
        int width;
        int height;

        // DRAW HINT WINDOW
        x = TILE_SIZE * 2;
        y = TILE_SIZE * 13;
        width = TILE_SIZE * 6;
        height = TILE_SIZE * 2;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("[ESC] Back", x + 24, y + 55);

        // DRAW PLAYER COIN WINDOW
        x = TILE_SIZE * 16;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("Your Coin: " + scene.getPlayer().getCoin(), x + 24, y + 55);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if(itemIndex < npc.getInventory().size()) {
            x = (int) (TILE_SIZE * 5.5);
            y = (int) (TILE_SIZE * 8.5);
            width = (int) (TILE_SIZE * 2.5);
            height = TILE_SIZE;
            drawSubWindow(x, y, width, height);
            graphics2D.drawImage(coin, x + 10, y + 7, 32, 32, null);

            int price = npc.getInventory().get(itemIndex).getPrice();
            String text = "" + price;
            x = getHorizonForAlignToRightText(text, TILE_SIZE * 8 - 20);
            graphics2D.drawString(text, x, y + 30);

            // BUY AN ITEM
            if(scene.getKeyInputs().isEnterPressed()) {
                if(npc.getInventory().get(itemIndex).getPrice() > scene.getPlayer().getCoin()) {
                    subState = 0;
                    gameState = DIALOGUE;
                    currentDialogue = "You need more coins to buy " + npc.getInventory().get(itemIndex).getObjectName() + "!";
                    drawDeadScreen();
                } else {
                    if(scene.getPlayer().canObtainItem(npc.getInventory().get(itemIndex))) {
                        scene.getPlayer().buyItem(npc.getInventory().get(itemIndex).getPrice());
                    } else {
                        subState = 0;
                        gameState = DIALOGUE;
                        currentDialogue = "Your package is full!\nYou cannot carry " + npc.getInventory().get(itemIndex).getObjectName() + "!";
                    }
                }
            }
        }
    }

    private void trade_sell() {
        Player player = scene.getPlayer();
        ArrayList<Entity> inventory = player.getInventory();

        // DRAW PLAYER INVENTORY
        drawInventory(player, true);

        int x;
        int y;
        int width;
        int height;

        // DRAW HINT WINDOW
        x = TILE_SIZE * 2;
        y = TILE_SIZE * 13;
        width = TILE_SIZE * 6;
        height = TILE_SIZE * 2;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("[ESC] Back", x + 24, y + 55);

        // DRAW PLAYER COIN WINDOW
        x = TILE_SIZE * 16;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("Your Coin: " + player.getCoin(), x + 24, y + 55);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if(itemIndex < inventory.size()) {
            x = (int) (TILE_SIZE * 19.5);
            y = (int) (TILE_SIZE * 8.5);
            width = (int) (TILE_SIZE * 2.5);
            height = TILE_SIZE;
            drawSubWindow(x, y, width, height);
            graphics2D.drawImage(coin, x + 10, y + 7, 32, 32, null);

            int price = inventory.get(itemIndex).getPrice() / 2;
            String text = "" + price;
            x = getHorizonForAlignToRightText(text, TILE_SIZE * 22 - 20);
            graphics2D.drawString(text, x, y + 30);

            // SELL AN ITEM
            if(scene.getKeyInputs().isEnterPressed()) {
                if(inventory.get(itemIndex) == player.getCurrentWeapon() ||
                        inventory.get(itemIndex) == player.getCurrentShield()) {
                    commandNum = 0;
                    subState = 0;
                    gameState = DIALOGUE;
                    currentDialogue = "You cannot sell an equipped item!";
                } else {
                    if(inventory.get(itemIndex).getAmount() > 1) {
                        inventory.get(itemIndex).reduceAmount();
                    } else {
                        inventory.remove(itemIndex);
                    }
                    player.sellItem(price);
                }
            }
        }
    }

    private void drawSleepScreen() {
        transitionCounter++;
        Lighting lighting = scene.getEnvironmentManager().getLighting();
        if(transitionCounter < 180) {
            lighting.increaseFilterAlpha(0.01f);
            if(lighting.getFilterAlpha() > 1) {
                lighting.setFilterAlpha(1f);
            }
        }
        if(transitionCounter >= 180) {
            lighting.decreaseFilterAlpha(0.01f);
            if(lighting.getFilterAlpha() <= 0) {
                lighting.setFilterAlpha(0);
                transitionCounter = 0;
                lighting.setDayState(DAY);
                lighting.setDayCounter(0);
                gameState = PLAY;
                scene.getPlayer().getPlayerImage();
                scene.getAudioManager().playMusic(START);
            }
        }
    }

    public int getItemIndexOnSlot(int slotCol, int slotRow) {
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

    public void playerSlotColDecrease() {
        this.playerSlotCol--;
    }

    public void playerSlotColIncrease() {
        this.playerSlotCol++;
    }

    public void playerSlotRowDecrease() {
        this.playerSlotRow--;
    }

    public void playerSlotRowIncrease() {
        this.playerSlotRow++;
    }

    public int getPlayerSlotCol() {
        return playerSlotCol;
    }

    public int getPlayerSlotRow() {
        return playerSlotRow;
    }

    public void npcSlotColDecrease() {
        this.npcSlotCol--;
    }

    public void npcSlotColIncrease() {
        this.npcSlotCol++;
    }

    public void npcSlotRowDecrease() {
        this.npcSlotRow--;
    }

    public void npcSlotRowIncrease() {
        this.npcSlotRow++;
    }

    public int getNpcSlotCol() {
        return npcSlotCol;
    }

    public int getNpcSlotRow() {
        return npcSlotRow;
    }

    public int getSubState() {
        return subState;
    }

    public void setSubState(int subState) {
        this.subState = subState;
    }

    public void setNpc(Entity npc) {
        this.npc = npc;
    }
}
