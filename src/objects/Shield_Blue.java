package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EntityConstant.*;
import static utilities.LoadSave.*;

public class Shield_Blue extends Entity {
    public Shield_Blue(Scene scene) {
        super(scene);

        objectName = BLUE_SHIELD;
        entityType = SHIELD;
        down1 = GetSpriteAtlas(SHIELD_BLUE_IMAGE);
        defenseValue = 2;
        description = "[" + objectName + "]\nA shiny blue shield.";
    }
}
