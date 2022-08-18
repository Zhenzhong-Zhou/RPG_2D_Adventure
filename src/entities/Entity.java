package entities;

import java.awt.image.BufferedImage;

public abstract class Entity {
    protected int x,y ,speed;
    protected BufferedImage up1, up2, left1, left2, down1, down2, right1, right2;
    protected String direction;
    protected int spriteCounter = 0;
    protected int spriteNum = 1;
    protected int animationSpeed = 60;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
