package entities;

import main.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilities.Constants.DirectionConstant.*;
import static utilities.Constants.SceneConstant.*;
import static utilities.Constants.SceneConstant.SCENE_HEIGHT;
import static utilities.Constants.WorldConstant.WORLD_HEIGHT;
import static utilities.Constants.WorldConstant.WORLD_WIDTH;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.UP_1_IMAGE;

public abstract class Entity {
    protected Scene scene;
    protected int worldX, worldY, speed;
    protected BufferedImage up1, up2, left1, left2, down1, down2, right1, right2;
    protected String direction;
    protected int spriteCounter = 0;
    protected int spriteNum = 1;
    protected int animationSpeed = 60;
    protected Rectangle hitbox;
    protected int hitboxDefaultX, hitboxDefaultY;
    protected boolean collision;
    protected int actionLockCounter = 0;

    public Entity(Scene scene) {
        this.scene = scene;

        hitbox = new Rectangle(0,0,TILE_SIZE, TILE_SIZE);
    }

    protected void setAction() {}

    public void update() {
        setAction();

        collision = false;
        scene.getCollisionDetection().checkTile(this);
        scene.getCollisionDetection().checkObject(this, false);
        scene.getCollisionDetection().checkPlayer(this);

        playerCanMove();
        updateAnimation();
    }

    protected void playerCanMove() {
        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        if(! collision) {
            switch(direction) {
                case UP -> worldY -= speed;
                case LEFT -> worldX -= speed;
                case DOWN -> worldY += speed;
                case RIGHT -> worldX += speed;
            }
        }
    }

    protected void updateAnimation() {
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

    protected BufferedImage animate() {
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
        return image;
    }

    protected void drawAnimation(Graphics2D graphics2D) {
        Player player = scene.getPlayer();
        int playerWorldX = player.getWorldX();
        int playerWorldY = player.getWorldY();
        int playerScreenX = player.getScreenX();
        int playerScreenY = player.getScreenY();

        int screenX = worldX - playerWorldX + playerScreenX;
        int screenY = worldY - playerWorldY + playerScreenY;

        int left = playerWorldX - playerScreenX;
        int right = playerWorldX + playerScreenX;
        int up = playerWorldY - playerScreenY;
        int down = playerWorldY + playerScreenY;

        // STOP MOVING CAMERA
        if(playerWorldX < playerScreenX) {
            screenX = worldX;
        }
        if(playerWorldY < playerScreenY) {
            screenY = worldY;
        }

        int rightOffset = SCENE_WIDTH - playerScreenX;
        if(rightOffset > WORLD_WIDTH - playerWorldX) {
            screenX = SCENE_WIDTH - (WORLD_WIDTH - worldX);
        }

        int bottomOffset = SCENE_HEIGHT - playerScreenY;
        if(bottomOffset > WORLD_HEIGHT - playerWorldY) {
            screenY = SCENE_HEIGHT - (WORLD_HEIGHT - worldY);
        }

        BufferedImage image = animate();

        if(worldX + TILE_SIZE > left && worldX - TILE_SIZE < right && worldY + TILE_SIZE > up && worldY - TILE_SIZE < down) {
            graphics2D.drawImage(image, screenX, screenY, null);
        } else if(playerScreenX > playerWorldX ||       //TODO: need to fix later
                playerScreenY > playerWorldY ||
                rightOffset > WORLD_WIDTH - playerWorldX ||
                bottomOffset > WORLD_HEIGHT - playerWorldY) {
            graphics2D.drawImage(image, screenX, screenY, null);
        }

        // Draw hitbox
        graphics2D.setColor(Color.BLUE);
        graphics2D.drawRect(screenX + getHitbox().x, screenY + getHitbox().y, getHitbox().width, getHitbox().height);
    }

    public int getWorldX() {
        return worldX;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public int getSpeed() {
        return speed;
    }

    public String getDirection() {
        return direction;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getHitboxDefaultX() {
        return hitboxDefaultX;
    }

    public void setHitboxDefaultX(int hitboxDefaultX) {
        this.hitboxDefaultX = hitboxDefaultX;
    }

    public int getHitboxDefaultY() {
        return hitboxDefaultY;
    }

    public void setHitboxDefaultY(int hitboxDefaultY) {
        this.hitboxDefaultY = hitboxDefaultY;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
