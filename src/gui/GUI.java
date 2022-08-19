package gui;

import main.Scene;
import objects.Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import static utilities.Constants.SceneConstant.*;
import static utilities.LoadSave.*;

public class GUI {
    private Scene scene;
    private Font maruMonica, purisaB;
    private BufferedImage image;
    private boolean message;
    private String notify = "";
    private int timer = 0;
    private boolean gameCompleted;
    private double playTime;

    public GUI(Scene scene) {
        this.scene = scene;

        Key key = new Key();
        image = key.getImage();

        initFont();
    }

    private void initFont() {
        maruMonica = GetFont(MARU_MONICA);
        purisaB = GetFont(PURISA_BOLD);
    }

    public void displayNotification(String text) {
        notify = text;
        message = true;
    }

    public void draw(Graphics2D graphics2D) {
        if(gameCompleted) {
            graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 40F));
            graphics2D.setColor(Color.WHITE);

            String text;
            int x;
            int y;

            text = "You found the treasure!";
            x = getHorizonCenteredText(graphics2D, text);
            y = SCENE_HEIGHT/2 - TILE_SIZE*4;
            graphics2D.drawString(text, x,y);

            graphics2D.setFont(purisaB.deriveFont(Font.BOLD, 80F));
            graphics2D.setColor(Color.YELLOW);

            text = "Congratulation!";
            x = getHorizonCenteredText(graphics2D, text);
            y = SCENE_HEIGHT/2 + TILE_SIZE*2;
            graphics2D.drawString(text, x,y);

            graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 40F));
            graphics2D.setColor(Color.WHITE);
            DecimalFormat convert = new DecimalFormat();
            convert.setMaximumFractionDigits(2);
            text = "Your Time is: " + convert.format(playTime) + "!";
            x = getHorizonCenteredText(graphics2D, text);
            y = SCENE_HEIGHT/2 + TILE_SIZE*5;
            graphics2D.drawString(text, x,y);

            scene.setThread(null);
        } else {
            graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 40F));
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawImage(image, TILE_SIZE/2, TILE_SIZE/2, null);
            graphics2D.drawString("x " + scene.getPlayer().getHasKey(), 74, 65);

            // TIME
            playTime += (double)1/60;
            DecimalFormat convert = new DecimalFormat();
            convert.setMaximumFractionDigits(2);
            String text = "Time: " + convert.format(playTime);
            graphics2D.drawString(text, TILE_SIZE*20, 65);

            // NOTIFICATION
            if(message) {
                graphics2D.setFont(maruMonica.deriveFont(Font.PLAIN, 30F));
                graphics2D.drawString(notify, TILE_SIZE/2, TILE_SIZE*7);

                timer++;
                if(timer > 120)  {
                    timer = 0;
                    message = false;
                }
            }
        }
    }

    public int getHorizonCenteredText(Graphics2D graphics2D, String text) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return SCENE_WIDTH / 2 - length / 2;
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
}
