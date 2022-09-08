package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EntityConstant.LANTERN;
import static utilities.Constants.EntityConstant.LIGHT;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.LANTERN_IMAGE;

public class Lantern extends Entity {
    public Lantern(Scene scene) {
        super(scene);

        entityType = LIGHT;
        objectName = LANTERN;
        down1 = GetSpriteAtlas(LANTERN_IMAGE);
        description = "[" + objectName + "]\nIlluminates your\nsurroundings.";
        price = 500;
        lightRadius = 250;
    }
}
