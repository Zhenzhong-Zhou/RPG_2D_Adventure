package gui;

import main.Scene;

import java.awt.*;

import static main.GameState.SETTINGS;
import static main.GameState.gameState;
import static utilities.Constants.SceneConstant.TILE_SIZE;

public class Options {
    private final Scene scene;
    private Graphics2D graphics2D;

    public Options(Scene scene) {
        this.scene = scene;
    }

    public void draw(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;

        graphics2D.setFont(scene.getGui().getMaruMonica().deriveFont(Font.PLAIN, 40F));
        graphics2D.setColor(Color.WHITE);

        if(gameState == SETTINGS) {
            drawOptionMenu();
        }
    }

    public void drawOptionMenu() {
        graphics2D.setFont(scene.getGui().getMaruMonica().deriveFont(Font.BOLD, 96F));
        String title = "Options";
        int y = 4 * TILE_SIZE;
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(title, 100, y);
    }
}
