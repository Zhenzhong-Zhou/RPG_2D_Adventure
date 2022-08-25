package objects;

import entities.Entity;
import main.Scene;

import static utilities.Constants.EntityConstant.MANA;
import static utilities.LoadSave.*;

public class ManaCrystal extends Entity {
    public ManaCrystal(Scene scene) {
        super(scene);

        objectName = MANA;
        down1 = GetSpriteAtlas(MANA_FULL);
        down2 = GetSpriteAtlas(MANA_BLANK);
    }
}
