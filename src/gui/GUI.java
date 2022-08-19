package gui;

import main.Scene;

import java.awt.*;

import static main.GameState.gameState;
import static utilities.Constants.SceneConstant.*;
import static utilities.LoadSave.*;

public class GUI {
    private final Scene scene;
    Graphics2D graphics2D;
    private Font maruMonica, purisaB;
    private boolean gameCompleted;
    private String currentDialogue = "";

    public GUI(Scene scene) {
        this.scene = scene;

        initFont();
    }

    private void initFont() {
        maruMonica = GetFont(MARU_MONICA);
        purisaB = GetFont(PURISA_BOLD);
    }

    public void draw(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;

        graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 40F));
        graphics2D.setColor(Color.WHITE);

        switch(gameState) {
            case PLAY -> {
            }
            case PAUSE -> drawPauseScreen();
            case DIALOGUE -> drawDialogueOverlay();
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
        int y = TILE_SIZE/2;
        int width = SCENE_WIDTH-(TILE_SIZE*4);
        int height = TILE_SIZE*5;
        drawSubWindow( x,y,width,height);

        x+= TILE_SIZE;
        y+=TILE_SIZE;

        for(String line : getCurrentDialogue().split("\n")) {
            graphics2D.drawString(line, x, y);
            y+=40;
        }
    }

    private int getHorizonCenteredText(String text) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return SCENE_WIDTH / 2 - length / 2;
    }

    private void drawSubWindow(int x, int y, int width, int height) {
        Color color = new Color(0,0,0, 210);
        graphics2D.setColor(color);
        graphics2D.fillRoundRect(x,y,width,height,35,35);

        color = new Color(255,255,255);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.drawRoundRect(x+5,y+5,width-10, height-10, 25,25);
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
}
