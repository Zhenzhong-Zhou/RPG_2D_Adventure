package entities;

import input.KeyInputs;
import main.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.LoadSave.*;

public class Player extends Entity{
    private Scene scene;
    private KeyInputs keyInputs;

    public Player(Scene scene, KeyInputs keyInputs) {
        this.scene = scene;
        this.keyInputs = keyInputs;

        setDefaultValues();
        getPlayerImage();
    }

    private void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 1;
        direction = DOWN;
    }

    private void getPlayerImage() {
        up1 = GetSpriteAtlas(UP_1_IMAGE);
        up2 = GetSpriteAtlas(UP_2_IMAGE);
        left1 = GetSpriteAtlas(LEFT_1_IMAGE);
        left2 = GetSpriteAtlas(LEFT_2_IMAGE);
        down1 = GetSpriteAtlas(DOWN_1_IMAGE);
        down2 = GetSpriteAtlas(DOWN_2_IMAGE);
        right1 = GetSpriteAtlas(RIGHT_1_IMAGE);
        right2 = GetSpriteAtlas(RIGHT_2_IMAGE);
    }

    public void update() {
        updatePositions();
    }

    private void updatePositions() {
        if(keyInputs.isUpPressed() || keyInputs.isLeftPressed() || keyInputs.isDownPressed() || keyInputs.isRightPressed()) {
            if(keyInputs.isUpPressed()) {
                direction = UP;
                y -= speed;
            } else if(keyInputs.isLeftPressed()) {
                direction = LEFT;
                x -= speed;
            } else if(keyInputs.isDownPressed()) {
                direction = DOWN;
                y += speed;
            } else if(keyInputs.isRightPressed()) {
                direction = RIGHT;
                x += speed;
            }
            updateAnimation();
        }
    }

    private void updateAnimation() {
        spriteCounter++;
        if(spriteCounter >= animationSpeed) {
            if(spriteNum == 1) {
                spriteNum = 2;
            } else if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D graphics2D) {
        drawAnimation(graphics2D);
    }

    private void drawAnimation(Graphics2D graphics2D) {
        BufferedImage image = null;

        switch(direction) {
            case UP -> {
                if(spriteNum == 1) {
                    image = up1;
                }
                if(spriteNum == 2) {
                    image = up2;
                }
            }
            case LEFT -> {
                if(spriteNum == 1) {
                    image = left1;
                }
                if(spriteNum == 2) {
                    image = left2;
                }
            }
            case DOWN -> {
                if(spriteNum == 1) {
                    image = down1;
                }
                if(spriteNum == 2) {
                    image = down2;
                }
            }
            case RIGHT -> {
                if(spriteNum == 1) {
                    image = right1;
                }
                if(spriteNum == 2) {
                    image = right2;
                }
            }
        }
        graphics2D.drawImage(image, x, y, null);

        // Draw hitbox
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(x + 8, y +16, 32, 32);
    }
}
