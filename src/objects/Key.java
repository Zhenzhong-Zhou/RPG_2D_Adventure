package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.ObjectConstant.KEY;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.KEY_IMAGE;

public class Key extends Entity {
    public Key(Scene scene) {
        super(scene);

        objectName = KEY;
        down1 = GetSpriteAtlas(KEY_IMAGE);
    }
}
