package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EquipmentConstant.WOOD_SHIELD;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.SHIELD_WOOD_IMAGE;

public class Shield_Wood extends Entity {
    public Shield_Wood(Scene scene) {
        super(scene);

        objectName = WOOD_SHIELD;
        down1 = GetSpriteAtlas(SHIELD_WOOD_IMAGE);
        defenseValue = 1;
        description = "[" + objectName + "]\nMade by wood.";
    }
}
