package entities;

import input.KeyInputs;
import main.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.WorldConstant.MAX_WORLD_COL;
import static utilities.Constants.WorldConstant.MAX_WORLD_ROW;
import static utilities.LoadSave.*;

public class Player extends Entity{
    private Scene scene;
    private KeyInputs keyInputs;
    private final int screenX, screenY;

    public Player(Scene scene, KeyInputs keyInputs) {
        this.scene = scene;
        this.keyInputs = keyInputs;

        screenX = (SCENE_WIDTH / 2) - (TILE_SIZE / 2);
        screenY = (SCENE_HEIGHT / 2) - (TILE_SIZE / 2);
        setDefaultValues();
        getPlayerImage();
    }

    private void setDefaultValues() {
        worldX = (MAX_WORLD_COL / 2 - 1) * TILE_SIZE;
        worldY = (MAX_WORLD_ROW / 2 - 1) * TILE_SIZE;
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
                worldY -= speed;
            } else if(keyInputs.isLeftPressed()) {
                direction = LEFT;
                worldX -= speed;
            } else if(keyInputs.isDownPressed()) {
                direction = DOWN;
                worldY += speed;
            } else if(keyInputs.isRightPressed()) {
                direction = RIGHT;
                worldX += speed;
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
        graphics2D.drawImage(image, screenX, screenY, null);

        // Draw hitbox
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(screenX + 8, screenY +16, 32, 32);
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }
}
