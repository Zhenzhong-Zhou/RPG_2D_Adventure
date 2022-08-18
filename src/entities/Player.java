package entities;

import input.KeyInputs;
import main.Scene;

import java.awt.*;

import static utilities.Constants.SceneConstant.TILE_SIZE;

public class Player extends Entity{
    private Scene scene;
    private KeyInputs keyInputs;

    public Player(Scene scene, KeyInputs keyInputs) {
        this.scene = scene;
        this.keyInputs = keyInputs;

        setDefaultValues();
    }

    private void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
    }

    public void update() {
        if(keyInputs.isUpPressed()) y -= speed;
        if(keyInputs.isLeftPressed()) x -= speed;
        if(keyInputs.isDownPressed()) y += speed;
        if(keyInputs.isRightPressed()) x += speed;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(x, y, TILE_SIZE, TILE_SIZE);
    }
}
