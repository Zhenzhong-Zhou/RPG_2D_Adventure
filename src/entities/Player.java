package entities;

import input.KeyInputs;
import main.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.GameState.DIALOGUE;
import static main.GameState.gameState;
import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.WorldConstant.WORLD_HEIGHT;
import static utilities.Constants.WorldConstant.WORLD_WIDTH;
import static utilities.LoadSave.*;

public class Player extends Entity {
    private final int screenX, screenY;
    private final KeyInputs keyInputs;
    private int standCounter = 0;

    public Player(Scene scene, KeyInputs keyInputs) {
        super(scene);
        this.keyInputs = keyInputs;

        screenX = (SCENE_WIDTH / 2) - (TILE_SIZE / 2);
        screenY = (SCENE_HEIGHT / 2) - (TILE_SIZE / 2);
        hitbox = new Rectangle(8, 16, 32, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setDefaultValues();
        getPlayerImage();
    }

    private void setDefaultValues() {
//        worldX = (MAX_WORLD_COL / 2 - 1) * TILE_SIZE;
//        worldY = (MAX_WORLD_ROW / 2 - 1) * TILE_SIZE;
        // TODO: not center
        worldX = 23 * TILE_SIZE;
        worldY = 21 * TILE_SIZE;
        speed = 1;
        direction = DOWN;

        // PLAYER STATUS
        maxLives = 6;
        life = maxLives;
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
        if(keyInputs.isUpPressed() || keyInputs.isLeftPressed() || keyInputs.isDownPressed() || keyInputs.isRightPressed() || keyInputs.isEnterPressed()) {
            if(keyInputs.isUpPressed()) direction = UP;
            else if(keyInputs.isLeftPressed()) direction = LEFT;
            else if(keyInputs.isDownPressed()) direction = DOWN;
            else if(keyInputs.isRightPressed()) direction = RIGHT;

            checkCollision();
            playerCanMove();
            updateAnimation();
        } else {
            standCounter++;

            if(standCounter == animationSpeed) {
                spriteNum = 1;
                standCounter = 0;
            }
        }

        // This needs to be outside of key if statement!
        invincibleCounter();
    }

    public void playerCanMove() {
        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        if(! collision && ! keyInputs.isEnterPressed()) {
            switch(direction) {
                case UP -> worldY -= speed;
                case LEFT -> worldX -= speed;
                case DOWN -> worldY += speed;
                case RIGHT -> worldX += speed;
            }
        }
        keyInputs.setEnterPressed(false);
    }

    private void invincibleCounter() {
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private void checkCollision() {
        // CHECK TILE COLLISION
        collision = false;
        scene.getCollisionDetection().checkTile(this);

        // CHECK OBJECT COLLISION
        int objectIndex = scene.getCollisionDetection().checkObject(this, true);
        collectObject(objectIndex);

        // CHECK NPC COLLISION
        int npcIndex = scene.getCollisionDetection().checkEntity(this, scene.getNPCs());
        interactNPC(npcIndex);

        // CHECK MONSTER COLLISION
        int monsterIndex = scene.getCollisionDetection().checkEntity(this, scene.getMonsters());
        interactMonster(monsterIndex);

        // CHECK EVENT
        scene.getEventManager().checkEvent();
    }

    private void collectObject(int objectIndex) {
        if(objectIndex != 999) {

        }
    }

    private void interactNPC(int npcIndex) {
        if(npcIndex != 999) {
            if(keyInputs.isEnterPressed()) {
                gameState = DIALOGUE;
                scene.getNPCs()[npcIndex].speak();
            }
        }
    }

    private void interactMonster(int monsterIndex) {
        if(monsterIndex != 999) {
            if(! invincible) {
                life -= 1;
                invincible = true;
            }
        }
    }

    public void draw(Graphics2D graphics2D) {
        drawAnimation(graphics2D);
    }

    public void drawAnimation(Graphics2D graphics2D) {
        BufferedImage image = animate();

        int x = screenX;
        int y = screenY;
        if(screenX > worldX) {
            x = worldX;
        }
        if(screenY > worldY) {
            y = worldY;
        }

        int rightOffset = SCENE_WIDTH - screenX;
        if(rightOffset > WORLD_WIDTH - worldX) {
            x = SCENE_WIDTH - (WORLD_WIDTH - worldX);
        }

        int bottomOffset = SCENE_HEIGHT - screenY;
        if(bottomOffset > WORLD_HEIGHT - worldY) {
            y = SCENE_HEIGHT - (WORLD_HEIGHT - worldY);
        }

        if(invincible) {
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        graphics2D.drawImage(image, x, y, null);

        // REST ALPHA
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // DEBUG: Draw hitbox
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(x + hitbox.x, y + hitbox.y, hitbox.width, hitbox.height);

        // DEBUG: Invincible counter
        graphics2D.setFont(scene.getGui().getMaruMonica().deriveFont(Font.PLAIN, 25F));
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Invincible: " + invincibleCounter, 10, 550);
    }

    public void resetDirectionBoolean() {
        keyInputs.setUpPressed(false);
        keyInputs.setLeftPressed(false);
        keyInputs.setDownPressed(false);
        keyInputs.setRightPressed(false);
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }
}
