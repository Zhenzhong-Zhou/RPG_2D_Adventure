package tiles;

import java.awt.image.BufferedImage;

public class Tile {
    private final BufferedImage sprite;
    private boolean collision;

    public Tile(BufferedImage sprite, boolean collision) {
        this.sprite = sprite;
        this.collision = collision;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
