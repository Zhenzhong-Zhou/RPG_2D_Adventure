package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    protected int worldX, worldY, speed;
    protected BufferedImage up1, up2, left1, left2, down1, down2, right1, right2;
    protected String direction;
    protected int spriteCounter = 0;
    protected int spriteNum = 1;
    protected int animationSpeed = 60;
    protected Rectangle hitbox;
    protected boolean collision;

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
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

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
