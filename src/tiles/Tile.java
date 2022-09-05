package tiles;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage sprite;
    private boolean collision;

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
