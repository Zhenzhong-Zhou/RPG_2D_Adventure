package objects;

import entities.Entity;
import main.Scene;

import java.awt.*;

import static utilities.Constants.ObjectConstant.DOOR;
import static utilities.LoadSave.DOOR_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class Door extends Entity {
    public Door(Scene scene) {
        super(scene);

        objectName = DOOR;
        down1 = GetSpriteAtlas(DOOR_IMAGE);
        collision = true;

        hitbox = new Rectangle(0, 16, 48, 32);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }
}
