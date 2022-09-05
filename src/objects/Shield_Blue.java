package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EntityConstant.BLUE_SHIELD;
import static utilities.Constants.EntityConstant.SHIELD;
import static utilities.LoadSave.GetSpriteAtlas;
import static utilities.LoadSave.SHIELD_BLUE_IMAGE;

public class Shield_Blue extends Entity {
    public Shield_Blue(Scene scene) {
        super(scene);

        objectName = BLUE_SHIELD;
        entityType = SHIELD;
        down1 = GetSpriteAtlas(SHIELD_BLUE_IMAGE);
        defenseValue = 2;
        description = "[" + objectName + "]\nA shiny blue shield.";
        price = 200;
    }
}
