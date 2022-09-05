package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EntityConstant.BOOT;
import static utilities.LoadSave.BOOT_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class Boot extends Entity {
    public Boot(Scene scene) {
        super(scene);

        objectName = BOOT;
        down1 = GetSpriteAtlas(BOOT_IMAGE);
        price = 25;
    }
}
