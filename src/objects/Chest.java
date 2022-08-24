package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EntityConstant.CHEST;
import static utilities.LoadSave.CHEST_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class Chest extends Entity {
    public Chest(Scene scene) {
        super(scene);

        objectName = CHEST;
        down1 = GetSpriteAtlas(CHEST_IMAGE);
        collision = true;
    }
}
