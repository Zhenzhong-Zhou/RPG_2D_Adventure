package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EntityConstant.AXE;
import static utilities.Constants.EntityConstant.WOODCUTTER_AXE;
import static utilities.LoadSave.AXE_IMAGE;
import static utilities.LoadSave.GetSpriteAtlas;

public class Axe extends Entity {
    public Axe(Scene scene) {
        super(scene);

        objectName = WOODCUTTER_AXE;
        entityType = AXE;
        down1 = GetSpriteAtlas(AXE_IMAGE);
        attackValue = 2;
        attackBox.width = 30;
        attackBox.height = 30;
        description = "[" + objectName + "]\nA bit rusty but still \ncan cut some trees.";
        price = 75;
    }
}
