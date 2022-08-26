package tile_interactive;

import main.Scene;

import java.awt.*;

import static utilities.Constants.SceneConstant.TILE_SIZE;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.TRUNK_IMAGE;

public class Trunk extends InteractiveTile {
    public Trunk(Scene scene, int col, int row) {
        super(scene);
        this.worldX = col * TILE_SIZE;
        this.worldY = row * TILE_SIZE;

        down1 = GetSpriteAtlas(TRUNK_IMAGE);
        hitbox = new Rectangle();
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }
}
