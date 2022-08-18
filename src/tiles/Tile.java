package tiles;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage sprite;
    private boolean collision;

    public Tile(BufferedImage sprite, boolean collision) {
        this.sprite = sprite;
        this.collision = collision;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public boolean isCollision() {
        return collision;
    }
}
